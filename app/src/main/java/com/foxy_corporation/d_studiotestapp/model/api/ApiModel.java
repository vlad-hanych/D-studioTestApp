package com.foxy_corporation.d_studiotestapp.model.api;

/**
 * Created by Vlad on 25.05.2017.
 */

public interface ApiModel {
    <S> S createService(Class<S> serviceClass);
    <S> S createService(Class<S> serviceClass, String authToken);
}
