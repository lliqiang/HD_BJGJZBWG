package com.example.lenovo.hd_beijing_meseum.serveinterface;

import com.example.lenovo.hd_beijing_meseum.model.ResponseBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by lenovo on 2016/11/10.
 */

public class BaseInterface {
//    http://192.168.10.20/bjgdjzbwg/
    public void retrofitKind(BaseInfo baseInfo, final OnSuccessResult onSuccessResult, final OnFailtureResult onFailtureResult) {
        Retrofit retrofit = new Retrofit.Builder()
//        http:// 101.200.234.14:8091/bjgdjzbwg/
                .baseUrl("http://101.200.234.14:8091/bjgdjzbwg/")
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();
        //第四步：通过retrofit获取动态服务代理对象
        StrInterface strInterface = retrofit.create(StrInterface.class);
        //第五步：通过实体调用请求方法，获取Call对象
        Call<ResponseBody> call = baseInfo.getResult();
        //第六步： Call执行异步请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                 onSuccessResult.getSuccessResult(call,response);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                    onFailtureResult.getFailtureResult(call,t);
            }
        });
    }

    interface BaseInfo {
        Call<ResponseBody> getResult();
    }
    interface OnSuccessResult{
      void  getSuccessResult(Call<ResponseBody> call, Response<ResponseBody> response);
    }
    interface OnFailtureResult{
        void  getFailtureResult(Call<ResponseBody> call, Throwable t);
    }
}
