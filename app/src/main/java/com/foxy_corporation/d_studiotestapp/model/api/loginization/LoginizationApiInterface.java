package com.foxy_corporation.d_studiotestapp.model.api.loginization;

import com.foxy_corporation.d_studiotestapp.utils.Constants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Vlad on 26.05.2017.
 */

public interface LoginizationApiInterface {
    @POST(Constants.LOGINIZATION_ENDPOINT)

    @FormUrlEncoded
    Call<ResponseBody> executeUserLogin(@Field(Constants.USERNAME_PARAM) String userName,
                                        @Field(Constants.PASSWORD_PARAM) String userPassword);
}

