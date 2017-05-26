package com.foxy_corporation.d_studiotestapp.view.authorization.loginization;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.foxy_corporation.d_studiotestapp.R;
import com.foxy_corporation.d_studiotestapp.presenter.authorization.loginization.LoginisationPresenter;
import com.foxy_corporation.d_studiotestapp.presenter.authorization.loginization.LoginisationPresenterImp;
import com.foxy_corporation.d_studiotestapp.utils.PreferencesUtils;
import com.foxy_corporation.d_studiotestapp.view.authorization.AuthorizationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Vlad on 25.05.2017.
 */

public class LoginizationFragment extends Fragment implements LoginizationView {
    private AuthorizationActivity mParentActivity;

    private Unbinder mUnbinder;

    @BindView(R.id.edtxtUsername_FL) EditText mEdtxtUsername;

    @BindView(R.id.edtxtPassword_FL) EditText mEdtxtPassword;

    private LoginisationPresenter mLoginizationPresenter = new LoginisationPresenterImp();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mParentActivity = (AuthorizationActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loginization, container, false);

        mUnbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mLoginizationPresenter.onAttachView(this);
    }


    @OnClick(R.id.buttLogin_FL)
    public void doLogin() {
        /*Log.d("LoginizationFragment...doLogin", "!");*/

        mLoginizationPresenter.loginUser();
    }

    @OnClick(R.id.txtGoToRegister_FL)
    public void goToRegister() {
        /*Log.d("LoginizationFragment...goToRegister", "!");*/

        mParentActivity.setRegistration();
    }

    @Override
    public String[] getUserInput() {
        String[] params = new String[2];
        params[0] = String.valueOf(mEdtxtUsername.getText());
        params[1] = String.valueOf(mEdtxtPassword.getText());

        return params;
    }

    @Override
    public void setResult(boolean isResultSuccessful, String resultString) {
        if (isResultSuccessful) {
            mParentActivity.setLoggedIn(resultString);

            Toast.makeText(mParentActivity, getResources().getString(R.string.registration_successful) + resultString, Toast.LENGTH_SHORT).show();

            PreferencesUtils myPreferencesUtils = PreferencesUtils.getInstance(mParentActivity);
            myPreferencesUtils.setAccessToken(resultString);
            myPreferencesUtils.setIsUserLoggedIn(true);
        } else {
            Toast.makeText(mParentActivity, getResources().getString(R.string.registration_failed) + resultString, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setMessage(String message) {
        Toast.makeText(mParentActivity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();

        mLoginizationPresenter.onDetachView();
    }

    @Override
    public void onDestroyView() {
        /*Log.d("LoginizationFragment...onDestroyView()", String.valueOf(this));*/

        super.onDestroyView();

        mUnbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mParentActivity = null;
    }
}
