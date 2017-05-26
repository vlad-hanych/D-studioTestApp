package com.foxy_corporation.d_studiotestapp.view.authorization;

/**
 * Created by Vlad on 25.05.2017.
 */

public interface AuthorizationView extends BaseAuthorizationView {
    void setRegistration();

    void setLoggedIn(String accessToken);
}
