package com.obss.jcp.sinandogan.agileexpress.application.services.auth.google;

import com.obss.jcp.sinandogan.agileexpress.application.services.auth.shared.dtos.LoginResponseModel;
import com.obss.jcp.sinandogan.agileexpress.application.services.auth.shared.dtos.TokenDto;

public interface CustomOAuth2Service {
    LoginResponseModel googleLogin(String idToken);
}
