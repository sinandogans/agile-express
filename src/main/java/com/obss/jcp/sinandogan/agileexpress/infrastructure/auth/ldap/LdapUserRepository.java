package com.obss.jcp.sinandogan.agileexpress.infrastructure.auth.ldap;

import com.obss.jcp.sinandogan.agileexpress.infrastructure.auth.ldap.user.LdapUser;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LdapUserRepository extends LdapRepository<LdapUser> {
    Optional<LdapUser> findByUsername(String username);

    Optional<LdapUser> findByEmail(String email);
}
