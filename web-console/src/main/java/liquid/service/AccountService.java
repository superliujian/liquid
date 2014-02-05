package liquid.service;

import liquid.config.LdapConfig;
import liquid.persistence.domain.Account;
import liquid.persistence.domain.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.ldap.core.*;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.ldap.userdetails.Person;
import org.springframework.stereotype.Service;

import javax.naming.Name;
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
    @Autowired
    private LdapOperations ldapOperations;

    public void register(Account account) {
        // throw NameAlreadyBoundException
        createPerson(account);

        // add the person to group in ldap.
        Group group = findGroupByName(account.getGroup());
        group.getUniqueMembers().add(buildAccountDn(account).toString() + ",dc=suncapital-logistics,dc=com");
        updateGroup(group);
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
        searchControls.setReturningAttributes(new String[]{"uid", "ou", "pwdAccountLockedTime"});

        List<Account> accounts = ldapOperations.search(
                "", "(objectclass=person)", searchControls,
                new AttributesMapper() {
                    public Object mapFromAttributes(Attributes attrs)
                            throws NamingException {
                        Account account = new Account();
                        account.setUid((String) attrs.get("uid").get());
                        account.setGroup((String) attrs.get("ou").get());

                        Attribute pwdAccountLockedTime = attrs.get("pwdAccountLockedTime");
                        if (null != pwdAccountLockedTime)
                            account.setPwdAccountLockedTime(pwdAccountLockedTime.get().toString());
                        return account;
                    }
                });

        return accounts;
    }

    public void unlock(String uid) {
        Account account = new Account();
        account.setUid(uid);

        Name dn = buildAccountDn(account);
        Attribute attr = new BasicAttribute("pwdAccountLockedTime");
        ModificationItem item = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attr);
        ldapOperations.modifyAttributes(dn, new ModificationItem[]{item});
    }

    public void lock(String uid) {
        Account account = new Account();
        account.setUid(uid);

        Name dn = buildAccountDn(account);
        Attribute attr = new BasicAttribute("pwdAccountLockedTime", date2string());
        ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr);
        ldapOperations.modifyAttributes(dn, new ModificationItem[]{item});
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

