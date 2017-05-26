package com.foxy_corporation.d_studiotestapp.view.authorization;

import com.foxy_corporation.d_studiotestapp.view.BaseView;

/**
 * Created by Vlad on 25.05.2017.
 */

public interface BaseAuthorizationView extends BaseView {
    void setResult(boolean isResultSuccessful, String resultString);
}
