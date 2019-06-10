package com.example.user.music.lib;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyRequest {
    private static final int TIME_OUT = 30;
    private static OkHttpClient mOkHttpClient;

    //为mOkHttpClient去配置参数  类加载的时候开始创建静态代码块，并且只执行一次
    static {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.followRedirects(true); //设置重定向 其实默认也是true

        //添加https支持
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
        /**
         * trust all the https point
         */
        mOkHttpClient = okHttpClientBuilder.build();
    }


    public static Request createPostRequest(String url, Map<String, String> params) {

        FormBody.Builder mFormBodybuilder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                // 将请求参数逐一添加到请求体中
                mFormBodybuilder.add(entry.getKey(), entry.getValue());
            }
        }
        FormBody mFormBody = mFormBodybuilder.build();
        return new Request.Builder()
                .url(url)
                .post(mFormBody)
                .build();
    }

    /**
     *  发送具体的http/https的请求
     * @param request
     * @param commonCallback
     * @return Call
     */
    public  static Call sendRequest(Request request, Callback commonCallback){
        Call call=mOkHttpClient.newCall(request);
        call.enqueue(commonCallback);
        return  call;
    }
    public  static Response sendRequest(Request request) throws IOException {
        Call call=mOkHttpClient.newCall(request);
        return  call.execute();
    }

}
