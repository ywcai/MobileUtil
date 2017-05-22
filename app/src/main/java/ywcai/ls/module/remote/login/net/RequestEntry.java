package ywcai.ls.module.remote.login.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ywcai.ls.module.remote.login.model.DeviceInfo;
import ywcai.ls.module.remote.login.model.RequestResult;
import ywcai.ls.util.MyConfig;

/**
 * Created by zmy_11 on 2017/4/15.
 */

public class RequestEntry {
    RequestHttp request=new RequestHttp();
    public RequestResult getDevices(String openId,String token)
    {
        RequestResult result=new RequestResult();
        String url="http://" + MyConfig.STR_HTTP_IP +
                ":" + MyConfig.INT_HTTP_PORT + "/UserCenter/account/devices/" +
                openId + "/" +
                token + "/";
        Response response=request.requestHttp(url);
        result.resultCode=-3;
        if (response == null) {
            return result;
        }
        String json = "";
        JSONObject jsonTemp=null;
        try {
            json = response.body().string();
            jsonTemp = new JSONObject(json);
            result.resultCode = jsonTemp.getInt("resultCode");
        } catch (Exception e) {
            result.resultCode = -2;
            return result;
        }
        if(result.resultCode>=1) {
            result.resultContent = anaJson(jsonTemp, result.resultCode);
        }
        return result;
    }
    private List anaJson(JSONObject json,int code) {
        String device="";
        List devices=new ArrayList();
        try {
            device=json.getString("devices");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        if(code==1) {
            DeviceInfo deviceInfo = gson.fromJson(device, DeviceInfo.class);
            devices.add(deviceInfo);
        }
        if(code>1) {
            Type type = new TypeToken<List<DeviceInfo>>() {
            }.getType();
            devices = gson.fromJson(device, type);
        }
        return devices;
    }

    public RequestResult addDevice(String openId,String token,String deviceName)
    {
        RequestResult result=new RequestResult();
        String url="http://" + MyConfig.STR_HTTP_IP +
                ":" + MyConfig.INT_HTTP_PORT + "/UserCenter/account/add/" +
                openId + "/" +
                token + "/"+
                deviceName+"/";
        Response response=request.requestHttp(url);
        result.resultCode=-40;
        if (response == null) {
            return result;
        }
        String json = "";
        JSONObject jsonTemp=null;
        String device="";
        try {
            json = response.body().string();
            jsonTemp = new JSONObject(json);
            result.resultCode = jsonTemp.getInt("resultCode");
            device=jsonTemp.getString("devices");
            Gson gson = new Gson();
            DeviceInfo deviceInfo=gson.fromJson(device,DeviceInfo.class);
            result.resultContent.add(deviceInfo);
        } catch (Exception e) {
            result.resultCode = -40;
        }
        return result;
    }
    public RequestResult modifyDeviceName(String openId,String token,int deviceId,String deviceName) {
        RequestResult result=new RequestResult();
        String url="http://" + MyConfig.STR_HTTP_IP +
                ":" + MyConfig.INT_HTTP_PORT + "/UserCenter/account/update/" +
                openId + "/" +
                token + "/"+
                deviceId + "/"+
                deviceName+"/";
        Response response=request.requestHttp(url);
        result.resultCode=-50;
        if (response == null) {
            return result;
        }

        String json = "";
        JSONObject jsonTemp=null;
        String device="";
        try {
            json = response.body().string();
            jsonTemp = new JSONObject(json);
            result.resultCode = jsonTemp.getInt("resultCode");
            device=jsonTemp.getString("devices");
            Gson gson = new Gson();
            DeviceInfo deviceInfo=gson.fromJson(device,DeviceInfo.class);
            result.resultContent.add(deviceInfo);
        } catch (Exception e) {
            result.resultCode = -50;
        }
        return  result;
    }


    public RequestResult delDevice(String openId,String token,int deviceId) {
        RequestResult result=new RequestResult();
        String url="http://" + MyConfig.STR_HTTP_IP +
                ":" + MyConfig.INT_HTTP_PORT + "/UserCenter/account/del/" +
                openId + "/" +
                token + "/"+
                deviceId+"/";
        Response response=request.requestHttp(url);
        result.resultCode=-60;
        if (response == null) {
            return result;
        }
        try {
            String json = response.body().string();
            JSONObject jsonTemp = new JSONObject(json);
            result.resultCode = jsonTemp.getInt("resultCode");
        } catch (Exception e) {
            result.resultCode = -60;
        }
        return  result;
    }

}
