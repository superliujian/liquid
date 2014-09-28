package liquid.config;

import liquid.user.persistence.domain.Account;
import liquid.user.persistence.domain.Group;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.ldap.core.*;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/21/13
 * Time: 9:43 PM
 */
@RunWith(JUnit4.class)
public class LdapConfigTest {
    @Test
    public void authenticate() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(LdapConfig.class);
        ctx.refresh();

        LdapTemplate ldapTemplate = ctx.getBean(LdapTemplate.class);

        boolean authenticated = ldapTemplate.authenticate("", "(uid=管理员)", "111111");
    }

    @Test
    public void findAll() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(LdapConfig.class);
        ctx.refresh();

        LdapOperations ldapOperations = ctx.getBean(LdapOperations.class);

        EqualsFilter filter = new EqualsFilter("objectclass", "person");
        List<Account> accounts = ldapOperations.search(DistinguishedName.EMPTY_PATH, filter.encode(), new AbstractContextMapper() {
            @Override
            protected Object doMapFromContext(DirContextOperations context) {
                Account account = new Account();
                account.setUid(context.getStringAttribute("cn"));
                account.setSurname(context.getStringAttribute("sn"));
                account.setDescription(context.getStringAttribute("description"));
                account.setPwdAccountLockedTime(context.getStringAttribute("pwdAccountLockedTime"));
                return account;
            }
        });

        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    @Test
    public void find() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(LdapConfig.class);
        ctx.refresh();

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setDerefLinkFlag(true);
        searchControls.setReturningAttributes(new String[]{"uid", "ou", "mail", "pwdAccountLockedTime"});

        LdapOperations ldapOperations = ctx.getBean(LdapOperations.class);

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person"));
        filter.and(new EqualsFilter("uid", "马小二"));

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
    }

    @Test
    public void findAllGroup() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(LdapConfig.class);
        ctx.refresh();

        LdapOperations ldapOperations = ctx.getBean(LdapOperations.class);

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setDerefLinkFlag(true);
        searchControls.setReturningAttributes(new String[]{"cn", "ou", "uniqueMember"});

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "groupOfUniqueNames"));
        filter.and(new EqualsFilter("cn", "sales"));

        String filterEncoding = filter.encode();
        List<Group> groups = ldapOperations.search(
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

    @Test
    public void getAllPersonNames() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(LdapConfig.class);
        ctx.refresh();

        LdapOperations ldapOperations = ctx.getBean(LdapOperations.class);

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setDerefLinkFlag(true);
        searchControls.setReturningAttributes(new String[]{"uid", "pwdAccountLockedTime"});

        List<Account> accounts = ldapOperations.search(
                "", "(objectclass=person)", searchControls,
                new AttributesMapper() {
                    public Object mapFromAttributes(Attributes attrs)
                            throws NamingException {
                        Account account = new Account();
                        account.setUid((String) attrs.get("uid").get());

                        Attribute pwdAccountLockedTime = attrs.get("pwdAccountLockedTime");
                        if (null != pwdAccountLockedTime)
                            account.setPwdAccountLockedTime(pwdAccountLockedTime.get().toString());
                        return account;
                    }
                });

        for (Account account : accounts) {
            System.out.println(account);
        }
    }

    @Test
    public void lock() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(LdapConfig.class);
        ctx.refresh();

        LdapOperations ldapOperations = ctx.getBean(LdapOperations.class);

        Account account = new Account();
        account.setUid("汪贵发");

        Name dn = buildAccountDn(account);
        Attribute attr = new BasicAttribute("pwdAccountLockedTime", date2string());
        ModificationItem item = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr);
        ldapOperations.modifyAttributes(dn, new ModificationItem[]{item});
    }

    @Test
    public void unlock() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(LdapConfig.class);
        ctx.refresh();

        LdapOperations ldapOperations = ctx.getBean(LdapOperations.class);

        Account account = new Account();
        account.setUid("汪贵发");

        Name dn = buildAccountDn(account);
        Attribute attr = new BasicAttribute("pwdAccountLockedTime");
        ModificationItem item = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, attr);
        ldapOperations.modifyAttributes(dn, new ModificationItem[]{item});
    }

    private DistinguishedName buildAccountDn(Account account) {
        DistinguishedName dn = new DistinguishedName();
        dn.add("ou", "people");
        dn.add("uid", account.getUid());
        return dn;
    }

    @Test
    public void findUserByName() {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(LdapConfig.class);
        ctx.refresh();

        LdapOperations ldapOperations = ctx.getBean(LdapOperations.class);

        String userName = "汪贵发";

        DistinguishedName dn = new DistinguishedName("ou=people");
        dn.add("uid", userName);
        Account user = (Account) ldapOperations.lookup(dn, new String[]{"pwdAccountLockedTime"}, new ContextMapper() {
            @Override
            public Object mapFromContext(Object context) {
                DirContextOperations dirContext = (DirContextOperations) context;
                Account account = new Account();
                account.setUid(dirContext.getStringAttribute("cn"));
                account.setPwdAccountLockedTime(dirContext.getStringAttribute("pwdAccountLockedTime"));
                return account;
            }
        });
        System.out.println(user);
    }

    @Test
    public void stringToDate() {
        String date = "20140205031034.035Z";
        String pattern = "yyyyMMddHHmmss.SSS'Z'";

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            System.out.println(dateFormat.parse(date));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
    }

    private String date2string() {
        String pattern = "yyyyMMddHHmmss.SSS'Z'";

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(new Date(Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTimeInMillis()));
    }
}
