package com.foxy_corporation.d_studiotestapp.view.inside;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.foxy_corporation.d_studiotestapp.R;
import com.foxy_corporation.d_studiotestapp.model.data.UserData;
import com.foxy_corporation.d_studiotestapp.presenter.inside.InsidePresenter;
import com.foxy_corporation.d_studiotestapp.presenter.inside.InsidePresenterImp;
import com.foxy_corporation.d_studiotestapp.utils.Constants;
import com.foxy_corporation.d_studiotestapp.utils.PreferencesUtils;
import com.foxy_corporation.d_studiotestapp.utils.Utils;
import com.foxy_corporation.d_studiotestapp.view.authorization.AuthorizationActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Vlad on 25.05.2017.
 */

public class InsideActivity extends AppCompatActivity implements InsideView {
    private static final String LIST_DATA_KEY = "list_data";

    private static final String LIST_POSITION_KEY = "list_state";

    private InsidePresenter mInsidePresenter = new InsidePresenterImp();

    private String mAccessToken;

    private UserDatasAdapter mUserDatasAdapter;

    @BindView(R.id.rvUserDatas_AI) RecyclerView mRVUserDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inside);

        ButterKnife.bind(this);

        mAccessToken = getIntent().getStringExtra(Constants.ACCESS_TOKEN);

        setUpListMainParams();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mInsidePresenter.onAttachView(this);

        if (Utils.isInternetAvailable(this))
            mInsidePresenter.downloadAllUsers();
        else {
            mInsidePresenter.getDataFromDataBase();
            Toast.makeText(this, getResources().getString(R.string.getting_data_from_database), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public String getAccessToken() {
        return mAccessToken;
    }

    private void setUpListMainParams() {
        mRVUserDatas.setLayoutManager(new LinearLayoutManager(this));

        mUserDatasAdapter = new UserDatasAdapter();

        mRVUserDatas.setAdapter(mUserDatasAdapter);
    }

    @Override
    public void setUserDatasList(ArrayList<UserData> userDatasList) {

        mUserDatasAdapter.setAdapterList(userDatasList);

    }



    @Override
    public void setMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnLogout_AI)
    public void logout() {
        /*Log.d("InsideActivity...logout", "");*/

        Intent autorizationActivityIntent = new Intent(this, AuthorizationActivity.class);

        startActivity(autorizationActivityIntent);

        PreferencesUtils myPreferencesUtils = PreferencesUtils.getInstance(this);
        myPreferencesUtils.setAccessToken(Constants.EMPTY_STRING);
        myPreferencesUtils.setIsUserLoggedIn(false);

        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mInsidePresenter.onDetachView();
    }


}
