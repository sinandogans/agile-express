package com.obss.jcp.sinandogan.agileexpress.application.shared.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "spring.ldap")
public record LdapProperties(
        String url,
        String base,
        String username,
        String search,
        String password
) {
}
