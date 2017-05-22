package ywcai.ls.module.remote.login.net;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by zmy_11 on 2017/4/15.
 */

public class RequestHttp {
     Response requestHttp(String url)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setWriteTimeout(10000, TimeUnit.MILLISECONDS);
        okHttpClient.setConnectTimeout(10000, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(10000, TimeUnit.MILLISECONDS);
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
