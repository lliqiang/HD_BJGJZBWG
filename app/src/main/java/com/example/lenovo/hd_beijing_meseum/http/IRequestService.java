package com.example.lenovo.hd_beijing_meseum.http;




import com.example.lenovo.hd_beijing_meseum.update.CheckResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 作者：Tailyou （祝文飞）
 * 时间：2016/6/11 16:17
 * 邮箱：tailyou@163.com
 * 描述：
 */
public interface IRequestService {

    /**
     * 检查App版本更新
     *
     * @param appKey
     * @param appSecret
     * @param appKind
     * @param versionCode
     * @param deviceId
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?g=&m=Api&a=checkVersion")
    Observable<CheckResponse> checkVersion(@Field("appKey") String appKey,
                                           @Field("appSecret") String appSecret,
                                           @Field("appKind") int appKind,
                                           @Field("versionCode") int versionCode,
                                           @Field("deviceId") String deviceId);

    /**
     * 请求机器号
     *
     * @param app_kind
     * @return
     */
    @FormUrlEncoded
    @POST("index.php?a=request_deviceno")
    Observable<Response<String>> reqDeviceNo(@Field("app_kind") String app_kind);

}
