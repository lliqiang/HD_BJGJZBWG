package com.example.lenovo.hd_beijing_meseum.serveinterface;

import android.database.Observable;

import com.example.lenovo.hd_beijing_meseum.model.ResponseBody;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;


/**
 * Created by lenovo on 2016/11/10.
 */

public interface StrInterface {
    @FormUrlEncoded
    @POST("index.php?g=Mapi&m=Positions&a=request_deviceno")
    Call<ResponseBody> getResult(@Field("app_kind") int app_kind);
}
