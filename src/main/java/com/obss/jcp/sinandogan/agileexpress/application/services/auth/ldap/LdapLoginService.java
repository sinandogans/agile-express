package com.obss.jcp.sinandogan.agileexpress.application.services.auth.ldap;

import com.obss.jcp.sinandogan.agileexpress.application.services.auth.shared.dtos.LoginResponseModel;

public interface LdapLoginService {
    LoginResponseModel ldapLogin(LdapLoginRequestDto ldapLoginRequestDto);
}
