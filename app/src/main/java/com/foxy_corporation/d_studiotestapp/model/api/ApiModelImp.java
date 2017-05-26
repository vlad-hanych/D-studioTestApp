package com.foxy_corporation.d_studiotestapp.model.api;

import com.foxy_corporation.d_studiotestapp.utils.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vlad on 25.05.2017.
 */

public class ApiModelImp implements ApiModel {

    private static final String AUTHORIZATION_KEY = "Authorization";

    private static final String BASE_SERVER_IP = "http://174.138.54.52/";

    private static ApiModelImp instance;

    private Retrofit mRetrofit;

    private OkHttpClient.Builder mOkHttpClientBuilder = new OkHttpClient.Builder();

    /// need local singleton, use Dagger 2
    public static ApiModelImp getInstance() {
        if (instance == null) {
            instance = new ApiModelImp();
        }

        return instance;
    }

    private ApiModelImp() {
        OkHttpClient okHttpClient = mOkHttpClientBuilder
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_SERVER_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient);


        mRetrofit = builder.client(okHttpClient).build();
    }

    @Override
    public <S> S createService(Class<S> serviceClass) {
        return mRetrofit.create(serviceClass);
    }

    public <S> S createService(Class<S> serviceClass, final String authToken) {
        if (authToken != null) {
            mOkHttpClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header(AUTHORIZATION_KEY, authToken)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        return mRetrofit.create(serviceClass);
    }
}
