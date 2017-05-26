package com.foxy_corporation.d_studiotestapp.model.api.registration;

import com.foxy_corporation.d_studiotestapp.utils.Constants;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Vlad on 25.05.2017.
 */

public interface RegistrationApiInterface {
    @POST(Constants.REGISTRATION_ENDPOINT)

    @FormUrlEncoded
    Call<ResponseBody> executeUserRegistration (@Field(Constants.USERNAME_PARAM) String userName,
                                              @Field(Constants.PASSWORD_PARAM) String userPassword,
                                              @Field(Constants.EMAIL_PARAM) String userEmail);
}
