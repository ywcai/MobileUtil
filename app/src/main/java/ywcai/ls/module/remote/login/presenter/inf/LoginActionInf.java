package ywcai.ls.module.remote.login.presenter.inf;

import com.tencent.tauth.IUiListener;
import org.json.JSONObject;
import ywcai.ls.module.remote.login.model.LoginType;
import ywcai.ls.module.remote.login.model.OperationDeviceType;

public interface LoginActionInf {
    void login(LoginType loginType, String accountInfo, String psw);
    void loginOut(LoginType loginType, String accountInfo, String psw);
    void createCore(int deviceId);
    void operationDeviceInfo(OperationDeviceType operationDeviceType,int deviceId,String deviceName);
    void setUserInfo(JSONObject object);
    void requestControl(int deviceId);
    void disconnectLink();
    IUiListener getListener();
}
