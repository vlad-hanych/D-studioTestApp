package com.foxy_corporation.d_studiotestapp.view.authorization.registration;

import com.foxy_corporation.d_studiotestapp.view.authorization.BaseAuthorizationView;

/**
 * Created by Vlad on 25.05.2017.
 */

public interface RegistrationView extends BaseAuthorizationView {
    void needRegistrate();

    String[] getUserInput();
}
