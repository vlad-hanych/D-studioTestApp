package com.foxy_corporation.d_studiotestapp.presenter;

import com.foxy_corporation.d_studiotestapp.view.BaseView;

/**
 * Created by Vlad on 25.05.2017.
 */

public interface BasePresenter {
    void onAttachView(BaseView view);

    void onDetachView();
}
