package com.foxy_corporation.d_studiotestapp.presenter.authorization.registration;

import android.util.Log;

import com.foxy_corporation.d_studiotestapp.model.api.ApiModelImp;
import com.foxy_corporation.d_studiotestapp.model.api.registration.RegistrationApiInterface;
import com.foxy_corporation.d_studiotestapp.utils.Constants;
import com.foxy_corporation.d_studiotestapp.view.BaseView;
import com.foxy_corporation.d_studiotestapp.view.authorization.registration.RegistrationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vlad on 25.05.2017.
 */

public class RegistrationPresenterImp implements RegistrationPresenter {
    private static final String MESSAGE_KEY = "message";

    private static final String TOKEN_KEY = "token";

    private RegistrationView mRegistrationView;

    private RegistrationApiInterface mRegistrationApiInterface;

    private Call<ResponseBody> mRegistrationCall;

    @Override
    public void onAttachView(BaseView view) {
        mRegistrationView = (RegistrationView) view;

        mRegistrationApiInterface = ApiModelImp.getInstance().createService(RegistrationApiInterface.class);
    }

    @Override
    public void registerUser() {
        /*Log.d("RegistrationPresenterImp...onResponse: ", "!");*/

        String[] params = mRegistrationView.getUserInput();

        if (params[0].equals(Constants.EMPTY_STRING) ||
            params[1].equals(Constants.EMPTY_STRING) ||
            params[2].equals(Constants.EMPTY_STRING)) {
            mRegistrationView.setMessage(Constants.PROVIDE_NEEDED_DATA_TEXT);
            return;
        }

        mRegistrationCall = mRegistrationApiInterface.executeUserRegistration(params[0], params[1], params[2]);
        mRegistrationCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!call.isCanceled()) {
                    try {
                        if (response.isSuccessful()) {
                            String responseString = response.body().string();

                            /*Log.d("RegistrationPresenterImp...onResponse: ", responseString);*/

                            JSONObject responseJSON = new JSONObject(responseString);

                            String message = responseJSON.optString(TOKEN_KEY);

                            if (!message.equals(Constants.EMPTY_STRING)) {
                                mRegistrationView.setResult(true, message);
                            } else
                                mRegistrationView.setResult(false, responseJSON.optString(MESSAGE_KEY));

                        } else {
                            mRegistrationView.setMessage(response.errorBody().string());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (!call.isCanceled()) {
                    /*Log.d("RegistrationPresenterImp...onResponse: ", t.getMessage());*/

                    mRegistrationView.setMessage(t.getMessage());
                }
            }
        });
    }

    @Override
    public void onDetachView() {
        stopRegistrationCall();

        mRegistrationApiInterface = null;

        mRegistrationView = null;
    }

    private void stopRegistrationCall() {
        if (mRegistrationCall != null) {
            mRegistrationCall.cancel();

            mRegistrationCall = null;
        }
    }
}
