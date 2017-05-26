package com.foxy_corporation.d_studiotestapp.presenter.authorization.loginization;

import android.util.Log;

import com.foxy_corporation.d_studiotestapp.model.api.ApiModelImp;
import com.foxy_corporation.d_studiotestapp.model.api.loginization.LoginizationApiInterface;
import com.foxy_corporation.d_studiotestapp.utils.Constants;
import com.foxy_corporation.d_studiotestapp.view.BaseView;
import com.foxy_corporation.d_studiotestapp.view.authorization.loginization.LoginizationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vlad on 26.05.2017.
 */

public class LoginisationPresenterImp implements LoginisationPresenter {
    private LoginizationView mLoginizationView;

    private LoginizationApiInterface mLoginizationApiInterface;

    private Call<ResponseBody> mLoginizationCall;

    @Override
    public void onAttachView(BaseView view) {
        mLoginizationView = (LoginizationView) view;

        mLoginizationApiInterface = ApiModelImp.getInstance().createService(LoginizationApiInterface.class);
    }

    @Override
    public void loginUser() {
        String[] params = mLoginizationView.getUserInput();

        if (params[0].equals(Constants.EMPTY_STRING) || params[1].equals(Constants.EMPTY_STRING)) {
            mLoginizationView.setMessage(Constants.PROVIDE_NEEDED_DATA_TEXT);

            return;
        }
        mLoginizationCall = mLoginizationApiInterface.executeUserLogin(params[0], params[1]);
        mLoginizationCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!call.isCanceled()) {
                    try {
                        if (response.isSuccessful()) {
                            String responseString = response.body().string();

                            Log.d("LoginisationPresenterImp...onResponse: ", responseString);

                            JSONObject responseJSON = new JSONObject(responseString);

                            String message = responseJSON.optString("token");

                            if (!message.equals(Constants.EMPTY_STRING)) {
                                mLoginizationView.setResult(true, message);
                            }
                            else
                                mLoginizationView.setResult(false, responseJSON.optString("message"));

                        }
                        else {
                            mLoginizationView.setMessage(response.errorBody().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!call.isCanceled()) {
                    Log.d("LoginisationPresenterImp...onResponse: ", t.getMessage());
                }
            }
        });

    }

    @Override
    public void onDetachView() {
        stopRegistrationCall();

        mLoginizationApiInterface = null;

        mLoginizationView = null;
    }

    private void stopRegistrationCall() {
        if (mLoginizationCall != null) {
            mLoginizationCall.cancel();

            mLoginizationCall = null;
        }
    }


}
