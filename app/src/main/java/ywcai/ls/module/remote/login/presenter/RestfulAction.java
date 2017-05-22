package ywcai.ls.module.remote.login.presenter;


import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.apache.mina.core.session.IoSession;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ywcai.ls.controls.pull.inf.WorkTask;
import ywcai.ls.mina.socket.ClientSocket;
import ywcai.ls.mina.socket.SocketEventListener;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.module.mouse.lsenum.MsgType;
import ywcai.ls.module.mouse.lsenum.OnlineType;
import ywcai.ls.module.mouse.lsenum.ViewType;
import ywcai.ls.module.mouse.model.ComponentStatus;
import ywcai.ls.module.mouse.model.net.RequestMsg;
import ywcai.ls.module.mouse.model.net.ResponseMsg;
import ywcai.ls.module.mouse.model.net.SocketOperation;
import ywcai.ls.module.mouse.model.util.TimeCounter;
import ywcai.ls.module.mouse.presenter.inf.TimerCountInf;
import ywcai.ls.module.remote.login.model.DeviceInfo;
import ywcai.ls.module.remote.login.model.LoginType;
import ywcai.ls.module.remote.login.model.LoginUser;
import ywcai.ls.module.remote.login.model.OperationDeviceType;
import ywcai.ls.module.remote.login.model.RequestResult;
import ywcai.ls.module.remote.login.model.SelectDevice;
import ywcai.ls.module.remote.login.net.RequestEntry;
import ywcai.ls.module.remote.login.net.SelectRequest;
import ywcai.ls.module.remote.login.presenter.inf.LoginActionInf;
import ywcai.ls.module.remote.login.presenter.inf.UpdateViewInf;
import ywcai.ls.util.MyConfig;
import ywcai.ls.util.MyUtil;
import ywcai.ls.util.ResultCode;

public class RestfulAction implements LoginActionInf, SocketEventListener, TimerCountInf, WorkTask {
    private UpdateViewInf updateViewInf;
    private Tencent mTencent;
    private String SCOPE = "all";
    private IUiListener uiListener;
    private LoginUser loginUser = new LoginUser();

    private ResHandler handler = new ResHandler();
    private SocketHandler sHandler = new SocketHandler();
    private ComponentStatus status = ComponentStatus.getInstance();
    private TimeCounter timeCounter;
    private int countNum = 10;

    public RestfulAction(UpdateViewInf _updateViewInf) {
        updateViewInf = _updateViewInf;
        uiListener = new BaseUiListener();
    }


    @Override
    public void login(LoginType loginType, String accountInfo, String psw) {
        switch (loginType) {
            case QQ:
                loginForQQ();
                break;
        }
    }

    private void loginForQQ() {
        if (mTencent == null) {
            mTencent = Tencent.createInstance("1105582955", MyApplication.getInstance().getApplicationContext());
        }
        if (!mTencent.isSessionValid()) {
            mTencent.login(updateViewInf.getMyActivity(), SCOPE, uiListener);
        } else {
            reqUserDetailInfo();
        }
    }

    @Override
    public void loginOut(LoginType loginType, String accountInfo, String psw) {
        outStatus();
        switch (loginType) {
            case QQ:
                outForQQ();
                break;
        }
    }

    private void outStatus() {
        status.onlineType = OnlineType.offline;
        if (status.internetSocket != null) {
            status.internetSocket.CloseSession();
            status.internetSocket = null;
        }
    }

    private void outForQQ() {
        if (mTencent != null) {
            mTencent.logout(updateViewInf.getMyContext());
            updateViewInf.loaded("退出登录!");
            updateViewInf.loginEnd(false, null);
        }
    }


    @Override
    public void createCore(int did) {
        //UI加载
        if (status.viewType == ViewType.NONE && status.onlineType == OnlineType.offline) {
            updateViewInf.loadIng("正在建立链接");
            createSocket();
        } else {
            updateViewInf.loaded("你已在本地建立了连接");
        }
    }

    private void createSocket() {
        ClientSocket temp = new ClientSocket();
        temp.addListener(this);
        status.internetSocket = temp;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        SocketOperation socketOperation = new SocketOperation(temp, MyConfig.STR_HTTP_IP);
        executorService.execute(socketOperation);
    }

    @Override
    public void operationDeviceInfo(OperationDeviceType operationDeviceType, int deviceId, String deviceName) {
        updateViewInf.loadIng("Loading");
        switch (operationDeviceType) {
            case ADD:
                addDevice(deviceName);
                break;
            case UPDATE:
                updateDevice(deviceId, deviceName);
                break;
            case DEL:
                delDevice(deviceId);
                break;
            case OUT:
                break;
        }
    }


    @Override
    public void setUserInfo(JSONObject object) {
        try {
            loginUser.openId = object.getString(Constants.PARAM_OPEN_ID);
            loginUser.expires = object.getString(Constants.PARAM_EXPIRES_IN);
            loginUser.token = object.getString(Constants.PARAM_ACCESS_TOKEN);
            mTencent.setOpenId(loginUser.openId);
            mTencent.setAccessToken(loginUser.token, loginUser.expires);
            reqUserDetailInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestControl(int deviceId) {
        //to request control the device
        RequestMsg requestMsg = new RequestMsg();
        requestMsg.sendJson(status.internetSocket,ResultCode.JSON_TYPE_REQUEST_LINK+"",deviceId+"");
//        timeCounter.setStop(false);
//        countNum=10;
//        timeCounter.startCounter(countNum);
//
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        SocketOperation socketOperation = new SocketOperation(temp, MyConfig.STR_HTTP_IP);
//        executorService.execute(socketOperation);
    }

    @Override
    public void disconnectLink() {

    }

    private void reqUserDetailInfo() {
        new UserInfo(updateViewInf.getMyActivity(), mTencent.getQQToken()).getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                if (o == null) {
                    outForQQ();
                    return;
                }
                JSONObject jsonObject = (JSONObject) o;
                try {
                    loginUser.nickName = jsonObject.getString("nickname");
                    loginUser.headPicUrl = jsonObject.getString("figureurl_qq_2");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                updateViewInf.loaded("登录成功");
                updateViewInf.loginEnd(true, loginUser);
            }

            @Override
            public void onError(UiError uiError) {
                updateViewInf.loaded("ERR:" + uiError);
                updateViewInf.loginEnd(false, null);
            }

            @Override
            public void onCancel() {
                updateViewInf.loaded("ERR:取消授权");
                updateViewInf.loginEnd(false, null);
            }
        });
    }

    @Override
    public IUiListener getListener() {
        return uiListener;
    }

    private void addDevice(String deviceName) {
        SelectRequest selectRequest = new SelectRequest(loginUser, handler, OperationDeviceType.ADD);
        selectRequest.setDeviceName(deviceName);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(selectRequest);
    }

    private void updateDevice(int deviceId, String deviceName) {
        SelectRequest selectRequest = new SelectRequest(loginUser, handler, OperationDeviceType.UPDATE);
        selectRequest.setDeviceId(deviceId);
        selectRequest.setDeviceName(deviceName);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(selectRequest);
    }

    private void delDevice(int deviceId) {
        SelectRequest selectRequest = new SelectRequest(loginUser, handler, OperationDeviceType.DEL);
        selectRequest.setDeviceId(deviceId);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(selectRequest);
    }


    @Override
    public void sessionCreated(IoSession ioSession) {
    }

    @Override
    public void sessionOpened(IoSession ioSession) {

    }

    @Override
    public void messageReceived(IoSession ioSession, byte[] bytes) {
        ResponseMsg responseMsg = new ResponseMsg(bytes);
        int resultCode = Integer.parseInt(responseMsg.getResponseType());
        String response = responseMsg.getResponseContent();
        notifyHandler(response, resultCode, 0);
    }


    @Override
    public void errorCatch(IoSession ioSession, Throwable throwable) {

    }

    @Override
    public void messageSent(IoSession ioSession, Object o) {

    }

    @Override
    public void sessionClosed(IoSession ioSession) {
        status.onlineType = OnlineType.offline;
        notifyHandler("", ResultCode.SUCCESS_SESSION_CLOSE, 0);

    }

    @Override
    public void sessionCreateStart(String s, int i) {

    }

    @Override
    public void sessionCreateEnd(IoSession ioSession, boolean b) {
        if (b) {
            notifyHandler("", ResultCode.SUCCESS_SESSION_OPEN, 0);
            checkToken();
        } else {
            notifyHandler("", ResultCode.FAIL_SESSION_OPEN, 0);
        }
    }

    @Override
    public void check(int time) {
        if (timeCounter.isStop()) {
            return;
        }
        if (time <= 0) {
            notifyHandler("", ResultCode.FAIL_WAIT_TIME_OUT, 0);
            if(status.onlineType==OnlineType.offline) {
                status.internetSocket.CloseSession();
            }
        } else {
            notifyHandler("", ResultCode.TIME_CHECKING, time);
        }
    }
    //request validate the identity
    private void checkToken() {
        RequestMsg requestMsg = new RequestMsg();
        requestMsg.sendJson(status.internetSocket,ResultCode.JSON_TYPE_CHECK_PSW+"", loginUser.openId + "|" + loginUser.token + "|" + status.selectDeviceID + "|" + status.selectDeviceName + "|" + "0");
        timeCounter = new TimeCounter(this);
        timeCounter.setStop(false);
        timeCounter.startCounter(countNum);
    }

    @Override
    public List execute(int i) {
        long nowTime = Calendar.getInstance().getTimeInMillis();
        List devices = new ArrayList();
        List<DeviceInfo> temp = new ArrayList();
        RequestEntry requestEntry = new RequestEntry();
        RequestResult requestResult = requestEntry.getDevices(loginUser.openId, loginUser.token);
        if (requestResult.resultCode <= -2) {
            //request fail
            sendMsg(requestResult.resultCode, requestResult.resultContent);
        } else {
            SelectDevice selectDevice = new SelectDevice(requestResult.resultContent);
            switch (status.onlineType) {
                case offline:
                    temp = selectDevice.getFree();
                    break;
                default:
                    temp = selectDevice.getOnline();
                    break;
            }
        }

        //wait to improvement
        for (DeviceInfo deviceInfo : temp) {
            HashMap hs = new HashMap();
            hs.put("deviceName", deviceInfo.deviceName);
            hs.put("deviceID", deviceInfo.deviceID + "");
            hs.put("deviceStatus", deviceInfo.deviceStatus);
            devices.add(hs);
        }
        nowTime = (Calendar.getInstance().getTimeInMillis() - nowTime);
        MyUtil.sleep(1000, (int) nowTime);
        return devices;
    }

    @Override
    public void onItemClick(View view, int i, Object o) {
        updateViewInf.clickList(i);
    }


    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            if (o == null) {
                updateViewInf.loginEnd(false, null);
                return;
            }
            JSONObject jsonObject = (JSONObject) o;
            int ret = -1;
            try {
                ret = jsonObject.getInt("ret");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (ret == 0) {
                setUserInfo(jsonObject);
                return;
            }
            updateViewInf.loginEnd(false, null);
        }

        @Override
        public void onError(UiError uiError) {
            updateViewInf.loaded("ERR:" + uiError);
            updateViewInf.loginEnd(false, null);
        }

        @Override
        public void onCancel() {
            updateViewInf.loaded("ERR:取消授权");
            updateViewInf.loginEnd(false, null);
        }
    }
    private void rDeviceTurnOn(String response) {
        JSONObject json = null;
        DeviceInfo deviceInfo = new DeviceInfo();
        try {
            json = new JSONObject(response);
            deviceInfo.deviceID = Integer.parseInt(json.getString("deviceID"));
            deviceInfo.deviceName = json.getString("deviceName");
            deviceInfo.deviceStatus="OnLine";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateViewInf.turnOnDev(deviceInfo);
    }

    private void rDeviceTurnOff(String response) {
        updateViewInf.turnOffDev(response);
    }

    private void rDeviceLinkUp(String response) {
        updateViewInf.linkUpDev(response);
    }

    private void rDeviceLinkDown(String response) {
        updateViewInf.linkDownDev(response);
    }

    private class SocketHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.arg1) {
                case ResultCode.SUCCESS_SESSION_OPEN:
                    updateViewInf.loadIng("创建连接成功!开始与服务器校验秘钥");
                    break;
                case ResultCode.FAIL_SESSION_OPEN:
                    timeCounter.setStop(true);
                    updateViewInf.loaded("与远端服务器创建连接失败");
                    break;
                case ResultCode.SUCCESS_SESSION_CLOSE:
                    timeCounter.setStop(true);
                    updateViewInf.loaded("与服务器断开连接");
                    break;
                case ResultCode.FAIL_WAIT_TIME_OUT:
                    timeCounter.setStop(true);
                    updateViewInf.loaded("执行任务在超时!");
                    break;
                case ResultCode.TIME_CHECKING:
                    updateViewInf.loadIng(msg.arg2 + "s");
                    break;
                case ResultCode.SOCKET_RESPONSE_VALIDATE_SUCCESS:
                    timeCounter.setStop(true);
                    status.onlineType = OnlineType.online;
                    updateViewInf.loaded("秘钥校验完成，成功上线");
                    updateViewInf.updateDevList();
                    break;
                case ResultCode.NOTIFY_DEVICE_TURN_ON:

                    rDeviceTurnOn(msg.obj.toString());
                    break;
                case ResultCode.NOTIFY_DEVICE_TURN_OFF:

                    rDeviceTurnOff(msg.obj.toString());
                    break;
                case ResultCode.NOTIFY_DEVICE_LINK_UP:
                    String sss=msg.obj.toString();
                    if(sss.equals(status.selectDeviceID+""))
                    {
                        status.onlineType=OnlineType.link;
                    }
                    rDeviceLinkUp(msg.obj.toString());
                    break;
                case ResultCode.NOTIFY_DEVICE_LINK_DOWN:
                    String yyy=msg.obj.toString();
                    if(yyy.equals(status.selectDeviceID+""))
                    {
                        status.onlineType=OnlineType.online;
                    }
                    rDeviceLinkDown(msg.obj.toString());
                    break;
                default:
                    updateViewInf.loaded("发生未知的错误,CODE:"+msg.arg1);
                    break;
            }
        }
    }


    private class ResHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -40:
                    updateViewInf.loaded("ERR:虚拟设备添加失败!");
                    break;
                case -41:
                    updateViewInf.loaded("SUCCESS:虚拟设备添加成功!");
                    updateViewInf.addDeviceSuccess(((List<DeviceInfo>) msg.obj).get(0));
                    break;
                case -50:
                    updateViewInf.loaded("ERR:虚拟设备名称更新失败!");
                    break;
                case -51:
                    updateViewInf.loaded("SUCCESS:虚拟设备名称更新成功!");
                    updateViewInf.updateDevice(((List<DeviceInfo>) msg.obj).get(0));
                    break;
                case -60:
                    updateViewInf.loaded("ERR:虚拟设备删除失败!");
                    break;
                case -61:
                    updateViewInf.loaded("SUCCESS:虚拟设备删除成功!");
                    updateViewInf.delDeviceSuccess();
                    break;
                case -3:
                    //response返回为null
                    updateViewInf.loaded("查询设备ID时,服务器返回空");
                    break;
                case -2:
                    //qq的openId和accessToken未通过校验
                    updateViewInf.loaded("秘钥不正确或服务器故障");
                    break;
                case -1:
                    //第一次登录，自动创建了账户
                    updateViewInf.loaded("自动注册完成");
                    break;
            }
        }
    }

    /**
     * to notify draw the UI
     **/

    private void notifyHandler(String response, int resultCode, int ars2) {
        Message msg = new Message();
        msg.obj = response;
        msg.arg1 = resultCode;
        msg.arg2 = ars2;
        sHandler.sendMessage(msg);
    }

    private void sendMsg(int code, Object obj) {
        Message message = new Message();
        message.what = code;
        message.obj = obj;
        handler.sendMessage(message);
    }

}
