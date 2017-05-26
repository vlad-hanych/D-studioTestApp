package com.foxy_corporation.d_studiotestapp.view.inside;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private static final String LIST_STATE_KEY = "list_state";

    public static final String LIST_DATA_KEY = "list_data";

    private InsidePresenter mInsidePresenter = new InsidePresenterImp();

    private String mAccessToken;

    private boolean mThereIsData = false;

    private UserDatasAdapter mUserDatasAdapter;

    @BindView(R.id.swipLSwiper_AI) SwipeRefreshLayout mSwipSwiper;

    @BindView(R.id.rvUserDatas_AI) RecyclerView mRVUserDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inside);

        ButterKnife.bind(this);

        mAccessToken = getIntent().getStringExtra(Constants.ACCESS_TOKEN);

        setUpSwiper();

        setUpListMainParams();

        if (savedInstanceState != null) {
            restoreListState(savedInstanceState);
            mThereIsData = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        mInsidePresenter.onAttachView(this);

        if (!mThereIsData)
            getData();

    }

    private void setUpSwiper() {
        mSwipSwiper.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        /*Log.d("onRefresh()", "onRefresh called from SwipeRefreshLayout");*/

                        getData();
                    }
                }
        );
    }

    private void setUpListMainParams() {
        mRVUserDatas.setLayoutManager(new LinearLayoutManager(this));

        mUserDatasAdapter = new UserDatasAdapter();

        mRVUserDatas.setAdapter(mUserDatasAdapter);
    }

    private void getData() {
        if (Utils.isInternetAvailable(this))
            mInsidePresenter.launchDownloadingAllUsers();
        else {
            mInsidePresenter.getDataFromDataBase();
            Toast.makeText(this, getResources().getString(R.string.getting_data_from_database), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public String getAccessToken() {
        return mAccessToken;
    }

    @Override
    public void setUserDatasList(ArrayList<UserData> userDatasList) {
        mUserDatasAdapter.setAdapterList(userDatasList);

        mSwipSwiper.setRefreshing(false);
    }

    @Override
    public void setMessage(String message) {
        mSwipSwiper.setRefreshing(false);

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
    protected void onSaveInstanceState(Bundle outState) {

        Parcelable listState = mRVUserDatas.getLayoutManager().onSaveInstanceState();

        outState.putParcelableArrayList(LIST_DATA_KEY, mUserDatasAdapter.getAdapterList());

        outState.putParcelable(LIST_STATE_KEY, listState);

        super.onSaveInstanceState(outState);
    }

    private void restoreListState(Bundle savedState) {
        Parcelable restoredListState = savedState.getParcelable(LIST_STATE_KEY);

        ArrayList <UserData> restoredListData = savedState.getParcelableArrayList(LIST_DATA_KEY);

        mUserDatasAdapter.setAdapterList(restoredListData);

        mRVUserDatas.getLayoutManager().onRestoreInstanceState(restoredListState);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mInsidePresenter.onDetachView();
    }


}
