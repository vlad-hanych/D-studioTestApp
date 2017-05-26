package com.foxy_corporation.d_studiotestapp.model.data_base;

import com.foxy_corporation.d_studiotestapp.model.data.UserData;

import java.util.ArrayList;

/**
 * Created by Vlad on 26.05.2017.
 */

public interface DataBaseModel {
    void saveUserDatas (ArrayList <UserData> userDatasList);
    void getUserDatas ();
}
