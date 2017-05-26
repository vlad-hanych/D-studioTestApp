package com.foxy_corporation.d_studiotestapp.model.api.inside;

import com.foxy_corporation.d_studiotestapp.utils.Constants;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Vlad on 25.05.2017.
 */

public interface GetAllUsersApiInterface {
    @GET(Constants.GET_ALL_USERS_ENDPOINT)

    Call<ResponseBody> downloadAllUsers();
}
