package ywcai.ls.module.remote.presenter.inf;

import com.tencent.tauth.IUiListener;

import ywcai.ls.module.remote.model.LoginUser;

public interface LoginActionInf {
    void LoginIn( );
    void LoginOut( );
    void ConnServer();
    void RegDevice();
    IUiListener getListener();

//    void createCore(int deviceId);
//    void operationDeviceInfo(OperationDeviceType operationDeviceType,int deviceId,String deviceName);
//    void requestControl(int deviceId);
//    void disconnectLink();

}
