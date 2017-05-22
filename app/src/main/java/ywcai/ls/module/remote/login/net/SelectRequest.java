package ywcai.ls.module.remote.login.net;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ywcai.ls.module.remote.login.model.DeviceInfo;
import ywcai.ls.module.remote.login.model.LoginUser;
import ywcai.ls.module.remote.login.model.OperationDeviceType;
import ywcai.ls.module.remote.login.model.RequestResult;
import ywcai.ls.util.MyConfig;

/**
 * Created by zmy_11 on 2017/1/21.
 */

public class SelectRequest implements Runnable {
    LoginUser loginUser;
    RequestHttp request;
    RequestEntry requestEntry;
    Handler temp;
    OperationDeviceType op;
    int deviceId;
    String deviceName;

    public SelectRequest(LoginUser _loginUser, Handler _temp, OperationDeviceType _op) {
//        request=new RequestHttp();
        requestEntry=new RequestEntry();
        loginUser = _loginUser;
        temp = _temp;
        op=_op;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public void run() {
        switch (op)
        {
            case LOGIN:
                get();
                break;
            case ADD:
                add(deviceName);
                break;
            case UPDATE:
                update(deviceId,deviceName);
                break;
            case DEL:
                del(deviceId);
                break;
            case OUT:
                out();
                break;
        }
    }
    private void get() {
        RequestResult result=requestEntry.getDevices(loginUser.openId,loginUser.token);
        sendMsg(result.resultCode,result.resultContent);
    }
    private void add(String  deviceName) {
        RequestResult result=requestEntry.addDevice(loginUser.openId,loginUser.token,deviceName);
        sendMsg(result.resultCode,result.resultContent);
    }
    private void update(int deviceId,String deviceName) {
        RequestResult result=requestEntry.modifyDeviceName(loginUser.openId,loginUser.token,deviceId,deviceName);
        sendMsg(result.resultCode,result.resultContent);
    }
    private void del(int deviceId) {
        RequestResult result=requestEntry.delDevice(loginUser.openId,loginUser.token,deviceId);
        sendMsg(result.resultCode,result.resultContent);
    }
    private void out() {

    }
    private void sendMsg(int code,Object obj)
    {
        Message message = new Message();
        message.what = code;
        message.obj = obj;
        temp.sendMessage(message);
    }
}
