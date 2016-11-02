package com.lionsquare.canisovismanager.Api;

import com.lionsquare.canisovismanager.Beans.Lista_Dispositivo;
import com.lionsquare.canisovismanager.Beans.ResponseDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by archivaldo on 26/08/16.
 */
public interface ServiceApi {

    @GET("listaReiting.php")
    Call<List<Lista_Dispositivo>> listaDivice();

    @FormUrlEncoded
    @POST("insertto.php")
    Call<ResponseDto> updatedata(@Field("marca") String marca, @Field("reiting") int raiting);


    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://caguametro.16mb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
