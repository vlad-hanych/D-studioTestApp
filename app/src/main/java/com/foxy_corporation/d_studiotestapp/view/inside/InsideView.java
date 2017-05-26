package com.foxy_corporation.d_studiotestapp.view.inside;

import com.foxy_corporation.d_studiotestapp.model.data.UserData;
import com.foxy_corporation.d_studiotestapp.view.BaseView;

import java.util.ArrayList;

/**
 * Created by Vlad on 25.05.2017.
 */

public interface InsideView extends BaseView {
    String getAccessToken();

    void setUserDatasList(ArrayList<UserData> userDatasList);
}
