package com.example.lenovo.hd_beijing_meseum.serveinterface;

import com.example.lenovo.hd_beijing_meseum.model.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lenovo on 2016/11/12.
 */

public interface ScanInterface {
    @FormUrlEncoded
    @POST("index.php?g=Mapi&m=Positions&a=exhibit")
    Call<ResponseBody> getResult(@Field("fileno") String fileno, @Field("type")int type,@Field("deviceno") String deviceno);

}
