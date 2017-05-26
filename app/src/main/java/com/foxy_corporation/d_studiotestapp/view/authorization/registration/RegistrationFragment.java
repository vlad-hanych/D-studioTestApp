package com.foxy_corporation.d_studiotestapp.view.authorization.registration;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.foxy_corporation.d_studiotestapp.R;
import com.foxy_corporation.d_studiotestapp.model.data.UserData;
import com.foxy_corporation.d_studiotestapp.model.data_base.DataBaseModel;
import com.foxy_corporation.d_studiotestapp.model.data_base.DataBaseModelImp;
import com.foxy_corporation.d_studiotestapp.presenter.authorization.registration.RegistrationPresenter;
import com.foxy_corporation.d_studiotestapp.presenter.authorization.registration.RegistrationPresenterImp;
import com.foxy_corporation.d_studiotestapp.utils.PreferencesUtils;
import com.foxy_corporation.d_studiotestapp.view.authorization.AuthorizationActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Vlad on 25.05.2017.
 */

public class RegistrationFragment extends Fragment implements RegistrationView {
    private AuthorizationActivity mParentActivity;

    private Unbinder mUnbinder;

    @BindView(R.id.edtxtUsername_FR) EditText mEdtxtUsername;

    @BindView(R.id.edtxtPassword_FR) EditText mEdtxtPassword;

    @BindView(R.id.edtxtEmail_FR) EditText mEdtxtEmail;

    private RegistrationPresenter mRegistrationPresenter = new RegistrationPresenterImp();

    /// private DataBaseModel mDataBaseModel; /// test

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mParentActivity = (AuthorizationActivity) getActivity();

        /// test 1
        /*mDataBaseModel = new DataBaseModelImp(null);

        ArrayList<UserData> testList = new ArrayList<>();
                            testList.add(new UserData("qest3", "qest3"));
                            testList.add(new UserData("qest4", "qest4"));
                            testList.add(new UserData("qest5", "qest5"));
        mDataBaseModel.saveUserDatas(testList);*/

        /// test 2
        /*mDataBaseModel = new DataBaseModelImp(null);

        mDataBaseModel.getUserDatas();*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);

        mUnbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        mRegistrationPresenter.onAttachView(this);
    }

    @OnClick(R.id.buttRegisterUser_FR)
    public void doRegister() {
        /*Log.d("RegistrationFragment...doRegister", "!");*/

        needRegistrate();
    }


    @Override
    public void needRegistrate() {
        mRegistrationPresenter.registerUser();
    }

    @Override
    public String[] getUserInput() {

        String[] params = new String[3];
        params[0] = String.valueOf(mEdtxtUsername.getText());
        params[1] = String.valueOf(mEdtxtPassword.getText());
        params[2] = String.valueOf(mEdtxtEmail.getText());

        return params;
    }


    @Override
    public void setResult(boolean isSuccessful, String resultString) {
        if (isSuccessful) {
            mParentActivity.setLoggedIn(resultString);

            Toast.makeText(mParentActivity, getResources().getString(R.string.registration_successful) + resultString, Toast.LENGTH_SHORT).show();
            /// TODO Here?!
            PreferencesUtils myPreferencesUtils = PreferencesUtils.getInstance(mParentActivity);
            myPreferencesUtils.setAccessToken(resultString);
            myPreferencesUtils.setIsUserLoggedIn(true);
        } else {
            Toast.makeText(mParentActivity, getResources().getString(R.string.registration_failed) + resultString, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void setMessage(String message) {
        Toast.makeText(mParentActivity, getResources().getString(R.string.registration_failed) + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();

        mRegistrationPresenter.onDetachView();
    }

    @Override
    public void onDestroyView() {
        /*Log.d("RegistrationFragment...onDestroyView()", String.valueOf(this));*/

        super.onDestroyView();

        mUnbinder.unbind();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mParentActivity = null;
    }
}
