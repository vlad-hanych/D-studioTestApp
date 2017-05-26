package com.foxy_corporation.d_studiotestapp.presenter.inside;

import com.foxy_corporation.d_studiotestapp.model.api.ApiModelImp;
import com.foxy_corporation.d_studiotestapp.model.api.inside.GetAllUsersApiInterface;
import com.foxy_corporation.d_studiotestapp.model.data.UserData;
import com.foxy_corporation.d_studiotestapp.model.data_base.DataBaseModel;
import com.foxy_corporation.d_studiotestapp.model.data_base.DataBaseModelImp;
import com.foxy_corporation.d_studiotestapp.utils.Constants;
import com.foxy_corporation.d_studiotestapp.view.BaseView;
import com.foxy_corporation.d_studiotestapp.view.inside.InsideView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Vlad on 25.05.2017.
 */

public class InsidePresenterImp implements InsidePresenter {
    private static final String NAME_KEY = "username";

    private static final String LAST_LOGIN_KEY = "last_login";

    private InsideView mInsideView;

    private DataBaseModel mDataBaseModel;

    private GetAllUsersApiInterface mGetAllUsersApiInterface;

    private Call<ResponseBody> mGetAllUsersCall;

    @Override
    public void onAttachView(BaseView view) {
        mInsideView = (InsideView) view;

        mGetAllUsersApiInterface = ApiModelImp.getInstance().createService(GetAllUsersApiInterface.class, mInsideView.getAccessToken());

        mGetAllUsersCall = mGetAllUsersApiInterface.downloadAllUsers();

        mDataBaseModel = new DataBaseModelImp(this);
    }

    @Override
    public void launchDownloadingAllUsers() {
        mGetAllUsersCall.clone().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (!call.isCanceled()) {
                    try {
                        if (response.isSuccessful()) {
                            String responseString = response.body().string();

                            JSONArray usersJSONArray = new JSONArray(responseString);

                            Type listType = new TypeToken<ArrayList<UserData>>() {
                            }.getType();
                            ArrayList<UserData> userDatasList = new Gson().fromJson(usersJSONArray.toString(), listType);

                            /*ArrayList<UserData> userDatasList = new ArrayList<>();

                            for (int i = 0; i < usersJSONArray.length(); i++) {
                                JSONObject iJSONObject = usersJSONArray.optJSONObject(i);

                                userDatasList.add(new UserData(iJSONObject.optString(NAME_KEY), iJSONObject.optString(LAST_LOGIN_KEY)));
                            }*/

                            mInsideView.setUserDatasList(userDatasList);

                            mDataBaseModel.saveUserDatas(userDatasList);

                        } else {
                            mInsideView.setMessage(response.errorBody().string());
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
                    mInsideView.setMessage(t.getMessage());
                }
            }
        });
    }

    @Override
    public void getDataFromDataBase() {
        mDataBaseModel.getUserDatas();
    }

    @Override
    public void setDataFromDataBase(ArrayList<UserData> userDatasList) {
        mInsideView.setUserDatasList(userDatasList);
    }

    @Override
    public void setDataBaseUpdateFinished() {
        mInsideView.setMessage(Constants.DATABASE_UPDATED_SUCCESSFULLY);
    }

    @Override
    public void onDetachView() {
        stopRegistrationCall();

        mGetAllUsersApiInterface = null;

        mDataBaseModel = null;

        mInsideView = null;
    }

    private void stopRegistrationCall() {
        if (mGetAllUsersCall != null) {
            mGetAllUsersCall.cancel();

            mGetAllUsersCall = null;
        }
    }
}
