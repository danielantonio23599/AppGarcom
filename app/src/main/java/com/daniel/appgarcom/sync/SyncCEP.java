package com.daniel.appgarcom.sync;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncCEP {
    public static final Retrofit RETROFIT_CEP(Context c) {
        return new Retrofit.Builder().
                baseUrl("https://viacep.com.br/ws/").
                addConverterFactory(GsonConverterFactory.create()).
                build();

    }
}
