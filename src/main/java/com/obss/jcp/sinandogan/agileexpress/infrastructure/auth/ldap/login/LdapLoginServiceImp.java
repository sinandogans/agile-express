package com.obss.jcp.sinandogan.agileexpress.infrastructure.auth.ldap.login;

import com.obss.jcp.sinandogan.agileexpress.application.services.auth.exceptions.LoginFailedException;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.exceptions.UserNotFoundException;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.ldap.LdapLoginRequestDto;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.shared.dtos.LoginResponseModel;
import com.obss.jcp.sinandogan.agileexpress.application.security.tokens.jwt.CustomUserDetailsService;
import com.obss.jcp.sinandogan.agileexpress.application.security.tokens.jwt.JwtService;
import com.obss.jcp.sinandogan.agileexpress.application.security.tokens.refreshtoken.RefreshTokenService;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.ldap.LdapLoginService;
import com.obss.jcp.sinandogan.agileexpress.application.services.user.UserService;
import com.obss.jcp.sinandogan.agileexpress.application.shared.configs.LdapProperties;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import java.util.Hashtable;

@Service
class LdapLoginServiceImp implements LdapLoginService {
    private final UserService userService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService customUserDetailsService;
    private final LdapProperties ldapProperties;

    @Value("${refresh.token.expiration.standart}")
    private long refreshTokenNotRememberDuration;
    @Value("${refresh.token.expiration.remember}")
    private long refreshTokenRememberDuration;

    public LdapLoginServiceImp(UserService userService, JwtService jwtService, RefreshTokenService refreshTokenService, CustomUserDetailsService customUserDetailsService, LdapProperties ldapProperties) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.customUserDetailsService = customUserDetailsService;
        this.ldapProperties = ldapProperties;
    }

    @Override
    public LoginResponseModel ldapLogin(LdapLoginRequestDto ldapLoginRequestDto) {
        String userDn = null;
        try {
            Hashtable<String, String> adminEnv = new Hashtable<>();
            adminEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            adminEnv.put(Context.PROVIDER_URL, ldapProperties.url());
            adminEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
            adminEnv.put(Context.SECURITY_PRINCIPAL, ldapProperties.username());
            adminEnv.put(Context.SECURITY_CREDENTIALS, ldapProperties.password());

            DirContext adminCtx = new InitialDirContext(adminEnv);

            String searchFilter = "(mail=" + ldapLoginRequestDto.getEmail() + ")";

            SearchControls searchControls = new SearchControls();
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> results = adminCtx.search(ldapProperties.search(), searchFilter, searchControls);

            if (results.hasMore()) {
                SearchResult searchResult = results.next();
                userDn = searchResult.getNameInNamespace();
            } else {
                throw new UserNotFoundException("User not found in LDAP.", ldapLoginRequestDto.getEmail());
            }
            adminCtx.close();

        } catch (NamingException e) {
            throw new RuntimeException("LDAP connection error: " + e.getMessage());
        }

        try {
            Hashtable<String, String> userEnv = new Hashtable<>();
            userEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            userEnv.put(Context.PROVIDER_URL, ldapProperties.url());
            userEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
            userEnv.put(Context.SECURITY_PRINCIPAL, userDn);
            userEnv.put(Context.SECURITY_CREDENTIALS, ldapLoginRequestDto.getPassword());

            DirContext userCtx = new InitialDirContext(userEnv);
            userCtx.close();

            long refreshTokenDuration = ldapLoginRequestDto.isRememberMe() ?
                    refreshTokenRememberDuration :
                    refreshTokenNotRememberDuration;

            var user = userService.findUserByEmail(ldapLoginRequestDto.getEmail());

            return new LoginResponseModel(
                    jwtService.generateToken(customUserDetailsService.loadUserByUsername(user.getEmail())),
                    refreshTokenService.createRefreshToken(user.getEmail(), refreshTokenDuration).getToken(),
                    user.getTitle(), user.getEmail(), user.getFirstName(), user.getLastName());

        } catch (NamingException e) {
            throw new LoginFailedException("Login failed: Password is incorrect.", ldapLoginRequestDto.getEmail());
        }
    }
}
