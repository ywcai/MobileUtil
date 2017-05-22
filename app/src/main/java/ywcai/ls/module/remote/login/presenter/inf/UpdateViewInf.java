package ywcai.ls.module.remote.login.presenter.inf;

import android.app.Activity;
import android.content.Context;
import java.util.List;
import ywcai.ls.module.remote.login.model.DeviceInfo;
import ywcai.ls.module.remote.login.model.LoginUser;

/**
 * Created by zmy_11 on 2017/1/15.
 */

public interface UpdateViewInf {
    void loadIng(String msg);
    void loaded(String msg);
    void loginEnd(boolean result,LoginUser userInfo);
    void createEnd(boolean result,String err);
    void updateDeviceList(List<DeviceInfo> list);
    void addDeviceSuccess(DeviceInfo deviceInfo);
    void delDeviceSuccess();
    void updateDevice(DeviceInfo deviceInfo);
    void updateDevList();
    Activity getMyActivity();
    Context getMyContext();
    void showInfo(String str);
    void clickList(int pos);
    void turnOnDev(DeviceInfo deviceInfo);
    void turnOffDev(String deviceID);
    void linkUpDev(String deviceID);
    void linkDownDev(String deviceID);
}
