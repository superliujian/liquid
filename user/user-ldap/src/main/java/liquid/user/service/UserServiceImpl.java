package liquid.user.service;

import liquid.user.domain.Group;
import liquid.user.domain.GroupMember;
import liquid.user.model.PasswordChange;
import liquid.user.model.User;
import liquid.user.persistence.domain.GroupType;
import liquid.user.persistence.domain.PasswordPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ldap.core.*;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/6/13
 * Time: 12:22 PM
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private LdapContextSource contextSource;

    @Autowired
    private Locale locale;

    @Autowired
    private LdapOperations ldapOperations;

    @Autowired
    protected MessageSource messageSource;

//    @Autowired
//    private MailNotificationService mailNotificationService;

//    @Autowired
//    private MessageSource messageSource;

    @Override
    public Group addGroup(Group group) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Group> findGroups() {
        Collection<Group> groups = new ArrayList<>();
        GroupType[] values = GroupType.values();
        for (GroupType value : values) {
            Group group = null;
            String name = messageSource.getMessage(value.getI18nKey(), null, locale);
            switch (value.getType()) {
                case "sales":
                    group = new Group(1, name);
                    break;
                case "marketing":
                    group = new Group(2, name);
                    break;
                case "operating":
                    group = new Group(3, name);
                    break;
                case "container":
                    group = new Group(4, name);
                    break;
                case "field":
                    group = new Group(5, name);
                    break;
                case "commerce":
                    group = new Group(6, name);
                    break;
                case "admin":
                    group = new Group(7, name);
                    break;
            }
            if (null != group) {
                groups.add(group);
            }
        }
        return groups;
    }

    @Override
    public GroupMember findByUsername(String username) {
        throw new UnsupportedOperationException();
    }

    private Integer groupTypeToId(String type) {
        switch (type) {
            case "sales":
                return 1;
            case "marketing":
                return 2;
            case "operating":
                return 3;
            case "container":
                return 4;
            case "field":
                return 5;
            case "commerce":
                return 6;
            case "admin":
                return 7;
            default:
                return 0;
        }
    }

    private String groupIdToType(Integer id) {
        switch (id) {
            case 1:
                return "sales";
            case 2:
                return "marketing";
            case 3:
                return "operating";
            case 4:
                return "container";
            case 5:
                return "field";
            case 6:
                return "commerce";
            case 7:
                return "admin";
            default:
                return "";
        }
    }

    @Override
    public void register(User user) {
        // gid -> group name. TODO: temporary solution
        user.setGroup(groupIdToType(Integer.valueOf(user.getGroup())));
        // throw NameAlreadyBoundException
        createPerson(user);

        // add the person to group in ldap.
        Group group = findGroupByName(user.getGroup());
        GroupMember member = new GroupMember();
        member.setUsername(buildAccountDn(user).toString() + ",dc=suncapital-logistics,dc=com");
        member.setGroup(group);
        group.getMembers().add(member);
        updateGroup(group);

        // Send mail notification
        List<User> users = findAll(GroupType.ADMIN.getType());
        String[] mailTo = new String[users.size()];
        for (int i = 0; i < mailTo.length; i++) {
            mailTo[i] = users.get(i).getEmail();
        }
        logger.debug("The mail list of the administrator role is {}.", mailTo);

//        mailNotificationService.send(messageSource.getMessage("mail.registration", null, Locale.CHINA),
//                messageSource.getMessage("mail.registration.content",
//                        new String[]{account.getSurname() + account.getGivenName()},
//                        Locale.CHINA), mailTo
//        );
//        mailNotificationService.send(messageSource.getMessage("mail.registration", null, Locale.CHINA),
//                messageSource.getMessage("mail.registration.content.for.account", null, Locale.CHINA),
//                account.getEmail());
    }

    public void createPerson(User user) {
        ldapOperations.bind(buildAccountDn(user), setAccountAttributes(new DirContextAdapter(), user), null);
    }

    private LdapName buildAccountDn(User user) {
        try {
            List<Rdn> rdns = new ArrayList<>();
            rdns.add(new Rdn("ou", "people"));
            rdns.add(new Rdn("uid", user.getUid()));
            LdapName dn = new LdapName(rdns);
            return dn;
        } catch (InvalidNameException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> findAll() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setDerefLinkFlag(true);
        searchControls.setReturningAttributes(new String[]{"uid", "ou", "mail", "pwdAccountLockedTime"});

        List<User> users = ldapOperations.search(
                "", "(objectclass=person)", searchControls,
                new AttributesMapper<User>() {
                    public User mapFromAttributes(Attributes attrs)
                            throws NamingException {
                        User user = new User();
                        user.setUid(attr2Str(attrs.get("uid")));
                        user.setGroup(attr2Str(attrs.get("ou")));
                        user.setEmail(attr2Str(attrs.get("mail")));

                        Attribute pwdAccountLockedTime = attrs.get("pwdAccountLockedTime");
                        if (null != pwdAccountLockedTime)
                            user.setPwdAccountLockedTime(pwdAccountLockedTime.get().toString());
                        return user;
                    }
                }
        );

        return users;
    }

    public User find(String uid) {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setDerefLinkFlag(true);
        searchControls.setReturningAttributes(new String[]{"uid", "ou", "givenName", "sn", "mail", "pwdAccountLockedTime"});

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person"));
        filter.and(new EqualsFilter("uid", uid));

        List<User> users = ldapOperations.search(
                "", filter.encode(), searchControls,
                new AttributesMapper<User>() {
                    public User mapFromAttributes(Attributes attrs)
                            throws NamingException {
                        User user = new User();
                        user.setUid(attr2Str(attrs.get("uid")));
                        user.setGivenName(attr2Str(attrs.get("givenName")));
                        user.setSurname(attr2Str(attrs.get("sn")));
                        user.setGroup(attr2Str(attrs.get("ou")));
                        user.setEmail(attr2Str(attrs.get("mail")));

                        Attribute pwdAccountLockedTime = attrs.get("pwdAccountLockedTime");
                        if (null != pwdAccountLockedTime)
                            user.setPwdAccountLockedTime(pwdAccountLockedTime.get().toString());
                        return user;
                    }
                }
        );

        if (null != users && users.size() > 0) {
            return users.get(0);
        } else {
            throw new RuntimeException(String.format("Account %s not found.", uid));
        }
    }

    public PasswordPolicy fetchPasswordPolicy() {
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setDerefLinkFlag(true);
        searchControls.setReturningAttributes(new String[]{"pwdMaxFailure"});

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "pwdPolicy"));
        filter.and(new EqualsFilter("cn", "ppolicy"));

        List<PasswordPolicy> policies = ldapOperations.search(
                "", filter.encode(), searchControls,
                new AttributesMapper<PasswordPolicy>() {
                    public PasswordPolicy mapFromAttributes(Attributes attrs)
                            throws NamingException {
                        PasswordPolicy policy = new PasswordPolicy();
                        policy.setPwdMaxFailure(attr2Int(attrs.get("pwdMaxFailure")));
                        return policy;
                    }
                }
        );

        if (null != policies && policies.size() > 0) {
            return policies.get(0);
        } else {
            throw new RuntimeException("PasswordPolicy not found.");
        }
    }

    private String attr2Str(Attribute attr) {
        String result = null;
        try {
            result = null == attr ? "null" : String.valueOf(attr.get());
        } catch (NamingException e) {
            // Ignore.
        }
        return result;
    }

    private int attr2Int(Attribute attr) {
        int result = 0;

        if (null != attr) {
            try {
                String value = String.valueOf(attr.get());
                result = Integer.valueOf(value);
            } catch (NamingException | NumberFormatException e) {
                logger.warn(e.getMessage(), e);
            }
        }

        return result;
    }

    public List<User> findAll(String groupName) {
        Group group = findGroup(groupName);
        Collection<GroupMember> uniqueMembers = group.getMembers();
        if (null == uniqueMembers || uniqueMembers.size() < 1) {
            return Collections.emptyList();
        }

        List<User> users = new ArrayList<>(uniqueMembers.size());
        for (GroupMember uniqueMember : uniqueMembers) {
            String[] fields = uniqueMember.getUsername().split(",");
            String[] uid = fields[0].split("=");
            User user = find(uid[1]);
            users.add(user);
        }
        return users;
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
                new AttributesMapper<Group>() {
                    public Group mapFromAttributes(Attributes attrs)
                            throws NamingException {
                        Group group = new Group();
                        group.setName((String) attrs.get("cn").get());

                        Collection<GroupMember> uniqueMembers = new HashSet<>();
                        Attribute attribute = attrs.get("uniqueMember");
                        if (null != attribute) {
                            NamingEnumeration<?> values = attribute.getAll();
                            if (null != values) {
                                while (values.hasMore()) {
                                    Object memberObj = values.next();
                                    if (null != memberObj) {
                                        GroupMember member = new GroupMember();
                                        member.setUsername(memberObj.toString());
                                        member.setGroup(group);
                                        uniqueMembers.add(member);
                                    }
                                }
                            }
                        }
                        group.setMembers(uniqueMembers);
                        return group;
                    }
                }
        );
    }

    public void unlock(String uid) {
        User user = find(uid);
        Name dn = buildAccountDn(user);
        Attribute attr = new BasicAttribute("pwdAccountLockedTime");
        ModificationItem item = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attr);
        ldapOperations.modifyAttributes(dn, new ModificationItem[]{item});

//        mailNotificationService.send(messageSource.getMessage("mail.account.unlock", null, Locale.CHINA),
//                messageSource.getMessage("mail.account.unlock.content", null, Locale.CHINA),
//                account.getEmail());
    }

    public void lock(String uid) {
        User user = find(uid);
        Name dn = buildAccountDn(user);
        Attribute attr = new BasicAttribute("pwdAccountLockedTime", date2string());
        ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr);
        ldapOperations.modifyAttributes(dn, new ModificationItem[]{item});

//        mailNotificationService.send(messageSource.getMessage("mail.account.lock", null, Locale.CHINA),
//                messageSource.getMessage("mail.account.lock.content", null, Locale.CHINA),
//                account.getEmail());
    }

    public void edit(User newOne) {
        // gid -> group name. TODO: temporary solution
        newOne.setGroup(groupIdToType(Integer.valueOf(newOne.getGroup())));
        User user = find(newOne.getUid());
        Name dn = buildAccountDn(user);

        DirContextOperations context = ldapOperations.lookupContext(dn);
        context.setAttributeValues("objectclass", new String[]{"top",
                "person", "organizationalPerson", "inetOrgPerson"});
        context.setAttributeValue("mail", newOne.getEmail());

        ldapOperations.modifyAttributes(context);
    }

    public void resetPassword(String uid, PasswordChange passwordChange) {
        User user = find(uid);
        Name dn = buildAccountDn(user);

        DirContextOperations context = ldapOperations.lookupContext(dn);
        context.setAttributeValues("objectclass", new String[]{"top",
                "person", "organizationalPerson", "inetOrgPerson"});

        context.setAttributeValue("userPassword", passwordChange.getNewPassword());

        ldapOperations.modifyAttributes(context);
    }

    public boolean authenticate(String userDn, String credentials) {
        DirContext ctx = null;
        try {
            ctx = contextSource.getContext(userDn, credentials);
            return true;
        } catch (Exception e) {
            // Context creation failed - authentication did not succeed
            logger.error("Login failed", e);
            return false;
        } finally {
            // It is imperative that the created DirContext instance is always closed
            LdapUtils.closeContext(ctx);
        }
    }

    private DirContextOperations setAccountAttributes(DirContextOperations adapter, User user) {
        adapter.setAttributeValues("objectclass", new String[]{"top",
                "person", "organizationalPerson", "inetOrgPerson"});
        adapter.setAttributeValue("sn", user.getSurname());
        adapter.setAttributeValue("gn", user.getGivenName());
        adapter.setAttributeValue("ou", user.getGroup());
        adapter.setAttributeValue("userPassword", user.getPassword());
        adapter.setAttributeValue("description", user.getDescription());
        adapter.setAttributeValue("telephoneNumber", user.getCell());
        adapter.setAttributeValue("cn", user.getSurname() + user.getGivenName());
        adapter.setAttributeValue("mail", user.getEmail());

        adapter.setAttributeValue("pwdAccountLockedTime", date2string());
        return adapter;
    }

    public Group findGroupByName(String groupName) {
        LdapName dn = null;
        try {
            List<Rdn> rdns = new ArrayList<>();
            rdns.add(new Rdn("ou", "groups"));
            rdns.add(new Rdn("cn", groupName));
            dn = new LdapName(rdns);
        } catch (InvalidNameException e) {
            throw new RuntimeException(e);
        }
        return ldapOperations.lookup(dn, new ContextMapper<Group>() {
            @Override
            public Group mapFromContext(Object ctx) {
                DirContextOperations dirContext = (DirContextOperations) ctx;
                Group group = new Group();
                group.setName(dirContext.getStringAttribute("cn"));
                String[] membersArray = dirContext.getStringAttributes("uniqueMember");
                if (membersArray != null) {
                    Collection<GroupMember> members = new ArrayList<GroupMember>();
                    List<String> list = Arrays.asList(membersArray);
                    for (String s : list) {
                        GroupMember member = new GroupMember();
                        member.setUsername(s);
                        member.setGroup(group);
                        members.add(member);
                    }
                    group.setMembers(members);
                }
                return group;
            }
        });
    }

    public void updateGroup(Group group) {
        LdapName dn = buildGroupDn(group);
        DirContextOperations adapter = (DirContextOperations) ldapOperations
                .lookup(dn);
        adapter = setGroupAttributes(adapter, group);
        ldapOperations.modifyAttributes(dn, adapter.getModificationItems());
    }

    private LdapName buildGroupDn(Group group) {
        try {
            List<Rdn> rdns = new ArrayList<>();
            rdns.add(new Rdn("ou", "groups"));
            rdns.add(new Rdn("cn", group.getName()));
            LdapName dn = new LdapName(rdns);
            return dn;
        } catch (InvalidNameException e) {
            throw new RuntimeException(e);
        }
    }

    DirContextOperations setGroupAttributes(DirContextOperations adapter, Group group) {
        adapter.setAttributeValues("objectclass", new String[]{"top",
                "groupOfUniqueNames"});
        adapter.setAttributeValue("cn", group.getName());
        if (group.getMembers() != null && group.getMembers().size() > 0) {
            String[] usernames = new String[group.getMembers().size()];
            int i = 0;
            for (GroupMember groupMember : group.getMembers()) {
                usernames[i] = groupMember.getUsername();
                i++;
            }
            adapter.setAttributeValues("uniqueMember", usernames);
        }
        return adapter;
    }

    private static class PersonContextMapper extends AbstractContextMapper {
        public Object doMapFromContext(DirContextOperations context) {
            User user = new User();
            user.setUid(context.getStringAttribute("cn"));
            user.setSurname(context.getStringAttribute("sn"));
            user.setDescription(context.getStringAttribute("description"));
            user.setPwdAccountLockedTime(context.getStringAttribute("pwdAccountLockedTime"));
            return user;
        }
    }

    private String date2string() {
        String pattern = "yyyyMMddHHmmss.SSS'Z'";

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date(Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis()));
    }
}

