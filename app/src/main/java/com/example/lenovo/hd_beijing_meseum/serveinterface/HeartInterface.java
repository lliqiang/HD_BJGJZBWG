package com.example.lenovo.hd_beijing_meseum.serveinterface;

import com.example.lenovo.hd_beijing_meseum.model.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lenovo on 2016/11/10.
 */

public interface HeartInterface{
    @FormUrlEncoded
    @POST("index.php?g=Mapi&m=Positions&a=heartbeat")

    Call<ResponseBody> getResult(@Field("app_kind") int app_kind,@Field("deviceno")String deviceno);


}
