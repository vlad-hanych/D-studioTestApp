package com.foxy_corporation.d_studiotestapp.presenter.inside;

import com.foxy_corporation.d_studiotestapp.model.data.UserData;
import com.foxy_corporation.d_studiotestapp.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by Vlad on 25.05.2017.
 */

public interface InsidePresenter extends BasePresenter {
    void launchDownloadingAllUsers();

    void getDataFromDataBase();

    void setDataFromDataBase(ArrayList <UserData> userDatasList);

    void setDataBaseUpdateFinished();

    void setNoDataInDatabase();
}
