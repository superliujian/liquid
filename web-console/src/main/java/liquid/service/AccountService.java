package liquid.service;

import liquid.persistence.domain.Account;
import liquid.persistence.domain.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

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

    public void register(Account accout) {
        // throw NameAlreadyBoundException
        createPerson(accout);

        // add the person to group in ldap.
        Group group = findGroupByName(accout.getGroup());
        group.getUniqueMembers().add(buildAccountDn(accout).toString() + ",dc=suncapital-logistics,dc=com");
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
}

