package liquid.service;

import liquid.metadata.GroupType;
import liquid.persistence.domain.Account;
import liquid.persistence.domain.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ldap.core.*;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/6/13
 * Time: 12:22 PM
 */
@Service
public class AccountService {
    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private LdapOperations ldapOperations;

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private MessageSource messageSource;

    public void register(Account account) {
        // throw NameAlreadyBoundException
        createPerson(account);

        // add the person to group in ldap.
        Group group = findGroupByName(account.getGroup());
        group.getUniqueMembers().add(buildAccountDn(account).toString() + ",dc=suncapital-logistics,dc=com");
        updateGroup(group);

        // Send mail notification
        List<Account> accounts = findAll(GroupType.ADMIN.getType());
        String[] mailTo = new String[accounts.size()];
        for (int i = 0; i < mailTo.length; i++) {
            mailTo[i] = accounts.get(i).getEmail();
        }
        logger.debug("The mail list of the administrator role is {}.", mailTo);

        mailNotificationService.send(messageSource.getMessage("mail.registration", null, Locale.CHINA),
                messageSource.getMessage("mail.registration.content",
                        new String[]{account.getSurname() + account.getGivenName()},
                        Locale.CHINA), mailTo);
        mailNotificationService.send(messageSource.getMessage("mail.registration", null, Locale.CHINA),
                messageSource.getMessage("mail.registration.content.for.account", null, Locale.CHINA),
                account.getEmail());
    }

    public void createPerson(Account account) {
        ldapOperations.bind(buildAccountDn(account), setAccountAttributes(new DirContextAdapter(), account), null);
    }

    private DistinguishedName buildAccountDn(Account account) {
        DistinguishedName dn = new DistinguishedName();
        dn.add("ou", "people");
        dn.add("uid", account.getUid());
        return dn;
    }

    public List<Account> findAll() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setDerefLinkFlag(true);
        searchControls.setReturningAttributes(new String[]{"uid", "ou", "mail", "pwdAccountLockedTime"});

        List<Account> accounts = ldapOperations.search(
                "", "(objectclass=person)", searchControls,
                new AttributesMapper() {
                    public Object mapFromAttributes(Attributes attrs)
                            throws NamingException {
                        Account account = new Account();
                        account.setUid((String) attrs.get("uid").get());
                        account.setGroup((String) attrs.get("ou").get());
                        account.setEmail((String) attrs.get("mail").get());

                        Attribute pwdAccountLockedTime = attrs.get("pwdAccountLockedTime");
                        if (null != pwdAccountLockedTime)
                            account.setPwdAccountLockedTime(pwdAccountLockedTime.get().toString());
                        return account;
                    }
                });

        return accounts;
    }

    public Account find(String uid) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setDerefLinkFlag(true);
        searchControls.setReturningAttributes(new String[]{"uid", "ou", "mail", "pwdAccountLockedTime"});

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person"));
        filter.and(new EqualsFilter("uid", uid));

        List<Account> accounts = ldapOperations.search(
                "", filter.encode(), searchControls,
                new AttributesMapper() {
                    public Object mapFromAttributes(Attributes attrs)
                            throws NamingException {
                        Account account = new Account();
                        account.setUid((String) attrs.get("uid").get());
                        account.setGroup((String) attrs.get("ou").get());
                        account.setEmail((String) attrs.get("mail").get());

                        Attribute pwdAccountLockedTime = attrs.get("pwdAccountLockedTime");
                        if (null != pwdAccountLockedTime)
                            account.setPwdAccountLockedTime(pwdAccountLockedTime.get().toString());
                        return account;
                    }
                });

        if (null != accounts && accounts.size() > 0) {
            return accounts.get(0);
        } else {
            throw new RuntimeException(String.format("Account %s not found.", uid));
        }
    }

    public List<Account> findAll(String groupName) {
        Group group = findGroup(groupName);
        Set<String> uniqueMembers = group.getUniqueMembers();
        if (null == uniqueMembers || uniqueMembers.size() < 1) {
            return Collections.emptyList();
        }

        List<Account> accounts = new ArrayList<>(uniqueMembers.size());
        for (String uniqueMember : uniqueMembers) {
            String[] fields = uniqueMember.split(",");
            String[] uid = fields[0].split("=");
            Account account = find(uid[1]);
            accounts.add(account);
        }
        return accounts;
    }

    public List<Group> findAllGroups() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setDerefLinkFlag(true);
        searchControls.setReturningAttributes(new String[]{"cn", "ou", "uniqueMember"});

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));

        List<Group> groups = filterGroups(searchControls, filter);

        return groups;
    }


    public Group findGroup(String groupName) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setDerefLinkFlag(true);
        searchControls.setReturningAttributes(new String[]{"cn", "ou", "uniqueMember"});

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
        filter.and(new EqualsFilter("cn", groupName));

        List<Group> groups = filterGroups(searchControls, filter);
        if (null != groups && groups.size() > 0) {
            return groups.get(0);
        } else {
            throw new RuntimeException(String.format("Group %s not found.", groupName));
        }
    }

    private List<Group> filterGroups(SearchControls searchControls, AndFilter filter) {
        return ldapOperations.search(
                "", filter.encode(), searchControls,
                new AttributesMapper() {
                    public Object mapFromAttributes(Attributes attrs)
                            throws NamingException {
                        Group group = new Group();
                        group.setName((String) attrs.get("cn").get());

                        Set<String> uniqueMembers = new HashSet<>();
                        Attribute attribute = attrs.get("uniqueMember");
                        if (null != attribute) {
                            NamingEnumeration<?> values = attribute.getAll();
                            if (null != values) {
                                while (values.hasMore()) {
                                    Object memberObj = values.next();
                                    if (null != memberObj) {
                                        uniqueMembers.add(memberObj.toString());
                                    }
                                }
                            }
                        }
                        group.setUniqueMembers(uniqueMembers);
                        return group;
                    }
                });
    }

    public void unlock(String uid) {
        Account account = find(uid);
        Name dn = buildAccountDn(account);
        Attribute attr = new BasicAttribute("pwdAccountLockedTime");
        ModificationItem item = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attr);
        ldapOperations.modifyAttributes(dn, new ModificationItem[]{item});

        mailNotificationService.send(messageSource.getMessage("mail.account.unlock", null, Locale.CHINA),
                messageSource.getMessage("mail.account.unlock.content", null, Locale.CHINA),
                account.getEmail());
    }

    public void lock(String uid) {
        Account account = find(uid);
        Name dn = buildAccountDn(account);
        Attribute attr = new BasicAttribute("pwdAccountLockedTime", date2string());
        ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr);
        ldapOperations.modifyAttributes(dn, new ModificationItem[]{item});

        mailNotificationService.send(messageSource.getMessage("mail.account.lock", null, Locale.CHINA),
                messageSource.getMessage("mail.account.lock.content", null, Locale.CHINA),
                account.getEmail());
    }

    private DirContextOperations setAccountAttributes(DirContextOperations adapter, Account account) {
        adapter.setAttributeValues("objectclass", new String[]{"top",
                "person", "organizationalPerson", "inetOrgPerson"});
        adapter.setAttributeValue("sn", account.getSurname());
        adapter.setAttributeValue("gn", account.getGivenName());
        adapter.setAttributeValue("ou", account.getGroup());
        adapter.setAttributeValue("userPassword", account.getPassword());
        adapter.setAttributeValue("description", account.getDescription());
        adapter.setAttributeValue("telephoneNumber", account.getCell());
        adapter.setAttributeValue("cn", account.getSurname() + account.getGivenName());
        adapter.setAttributeValue("mail", account.getEmail());

        adapter.setAttributeValue("pwdAccountLockedTime", date2string());
        return adapter;
    }

    public Group findGroupByName(String groupName) {
        DistinguishedName dn = new DistinguishedName("ou=groups");
        dn.add("cn", groupName);
        return (Group) ldapOperations.lookup(dn, new ContextMapper() {
            @Override
            public Object mapFromContext(Object ctx) {
                DirContextOperations dirContext = (DirContextOperations) ctx;
                Group group = new Group();
                group.setName(dirContext.getStringAttribute("cn"));
                String[] membersArray = dirContext.getStringAttributes("uniqueMember");
                if (membersArray != null) {
                    List list = Arrays.asList(membersArray);
                    group.setUniqueMembers(new TreeSet(list));
                }
                return group;
            }
        });
    }

    public void updateGroup(Group group) {
        DistinguishedName dn = buildGroupDn(group);
        DirContextOperations adapter = (DirContextOperations) ldapOperations
                .lookup(dn);
        adapter = setGroupAttributes(adapter, group);
        ldapOperations.modifyAttributes(dn, adapter.getModificationItems());
    }

    private DistinguishedName buildGroupDn(Group group) {
        DistinguishedName dn = new DistinguishedName();
        dn.add("ou", "groups");
        dn.add("cn", group.getName());
        return dn;
    }

    DirContextOperations setGroupAttributes(DirContextOperations adapter, Group group) {
        adapter.setAttributeValues("objectclass", new String[]{"top",
                "groupOfUniqueNames"});
        adapter.setAttributeValue("cn", group.getName());
        if (group.getUniqueMembers() != null && group.getUniqueMembers().size() > 0) {
            adapter.setAttributeValues("uniqueMember", group.getUniqueMembers()
                    .toArray(new String[0]));
        }
        return adapter;
    }

    private static class PersonContextMapper extends AbstractContextMapper {
        public Object doMapFromContext(DirContextOperations context) {
            Account account = new Account();
            account.setUid(context.getStringAttribute("cn"));
            account.setSurname(context.getStringAttribute("sn"));
            account.setDescription(context.getStringAttribute("description"));
            account.setPwdAccountLockedTime(context.getStringAttribute("pwdAccountLockedTime"));
            return account;
        }
    }

    private String date2string() {
        String pattern = "yyyyMMddHHmmss.SSS'Z'";

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date(Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis()));
    }
}

