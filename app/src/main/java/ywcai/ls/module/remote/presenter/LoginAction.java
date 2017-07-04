package ywcai.ls.module.remote.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ywcai.ls.common.net.CreateSocket;
import ywcai.ls.mina.socket.ClientSocket;
import ywcai.ls.mina.socket.SocketEventListener;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.common.ComponentStatus;
import ywcai.ls.common.DeviceInfo;
import ywcai.ls.module.remote.model.LoginUser;
import ywcai.ls.common.em.RemoteViewUpdateType;
import ywcai.ls.module.remote.presenter.inf.LoginActionInf;
import ywcai.ls.util.LookDeviceUtil;
import ywcai.ls.util.statics.MyConfig;
import ywcai.ls.util.statics.MesUtil;
import ywcai.ls.util.statics.ResultCode;

public class LoginAction implements LoginActionInf {
    private Tencent mTencent;
    private IUiListener baseListener;
    private LoginUser loginUser = new LoginUser();
    private ComponentStatus status = ComponentStatus.getInstance();
    private Activity activity;
    private Context context;

    public LoginAction(Activity _activity, Context _context) {
        activity = _activity;
        context = _context;
        baseListener = new BaseUiListener();
    }



    @Override
    public void LoginIn() {
        MesUtil.sendEventMsg(RemoteViewUpdateType.LOADING,"正在获取QQ授权...",null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                loginForQQ();
            }
        }).start();

    }

    @Override
    public void LoginOut() {
        outForQQ();
    }

    @Override
    public void ConnServer() {
        MesUtil.sendEventMsg(RemoteViewUpdateType.LOADING,"登录QQ成功，正在创建连接...",null);
        ClientSocket temp = new ClientSocket();
        status.internetSocket = temp;
        SocketEventListener socketEventListener = new InternetSocketHandler();
        temp.addListener(socketEventListener);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CreateSocket createSocket = new CreateSocket(temp, MyConfig.STR_SOCKET_IP);
        executorService.execute(createSocket);

    }
    @Override
    public void RegDevice() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MesUtil.sendEventMsg(RemoteViewUpdateType.LOADING,"等待服务器返回注册信息...",null);
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.userId = loginUser.openId;
                LookDeviceUtil lookDeviceUtil=new LookDeviceUtil();
                deviceInfo.deviceId = lookDeviceUtil.getDeviceId();
                deviceInfo.deviceName = lookDeviceUtil.getDeviceName();
                deviceInfo.deviceMode =ResultCode.device_mode_mobile;
                deviceInfo.status = ResultCode.device_status_offline;
                deviceInfo.accessCode = "000000";
                ComponentStatus.getInstance().deviceId=deviceInfo.deviceId;
                Gson gson = new Gson();
                String content = gson.toJson(deviceInfo);
                MesUtil.sendJson(status.internetSocket, ResultCode.json_type_req_reg, content);
            }
        }).start();
    }

    @Override
    public IUiListener getListener() {
        return baseListener;
    }

    private void loginForQQ() {
        if (mTencent == null) {
            mTencent = Tencent.createInstance("1105582955", MyApplication.getInstance().getApplicationContext());
        }
        if (!mTencent.isSessionValid()) {
            String SCOPE = "all";
            mTencent.login(activity, SCOPE, baseListener);
        } else {
            getQQDetailInfo();
        }
    }

    private void outStatus() {
//        status.onlineType = OnlineType.offline;
        if (status.internetSocket != null) {
            status.internetSocket.CloseSession();
        }
    }

    private void outForQQ() {
        outStatus();
        if (mTencent != null) {
            mTencent.logout(context);
            mTencent=null;
        }
        MesUtil.sendEventMsg(RemoteViewUpdateType.QQ_LOGIN_OUT,"",null);
    }

    private void getTokenAndDetailInfo(JSONObject object) {
        try {
            loginUser.openId = object.getString(Constants.PARAM_OPEN_ID);
            loginUser.expires = object.getString(Constants.PARAM_EXPIRES_IN);
            loginUser.token = object.getString(Constants.PARAM_ACCESS_TOKEN);
            mTencent.setOpenId(loginUser.openId);
            mTencent.setAccessToken(loginUser.token, loginUser.expires);
            getQQDetailInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getQQDetailInfo() {
        new UserInfo(activity, mTencent.getQQToken()).getUserInfo(new IUiListener() {
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
                MesUtil.sendEventMsg(RemoteViewUpdateType.QQ_LOGIN_OK,"",loginUser);
            }

            @Override
            public void onError(UiError uiError) {
                MesUtil.sendEventMsg(RemoteViewUpdateType.QQ_LOGIN_OUT,"",null);
                MesUtil.sendEventMsg(RemoteViewUpdateType.LOAD_END,uiError+"",null);
            }
            @Override
            public void onCancel() {
                MesUtil.sendEventMsg(RemoteViewUpdateType.QQ_LOGIN_OUT,"",null);
                MesUtil.sendEventMsg(RemoteViewUpdateType.LOAD_END,"ERR:取消授权",null);
            }
        });
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            if (o == null) {
                MesUtil.sendEventMsg(RemoteViewUpdateType.QQ_LOGIN_OUT,"",null);
                MesUtil.sendEventMsg(RemoteViewUpdateType.LOAD_END,"获取QQ应用对象为空",null);
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
                getTokenAndDetailInfo(jsonObject);//获取授权信息并拉去详细信息
                return;
            }
            MesUtil.sendEventMsg(RemoteViewUpdateType.QQ_LOGIN_OUT,"",null);
            MesUtil.sendEventMsg(RemoteViewUpdateType.LOAD_END,"获取QQ用户信息失败",null);
        }

        @Override
        public void onError(UiError uiError) {
            MesUtil.sendEventMsg(RemoteViewUpdateType.QQ_LOGIN_OUT,"",null);
            MesUtil.sendEventMsg(RemoteViewUpdateType.LOAD_END,uiError+"",null);
        }

        @Override
        public void onCancel() {
            MesUtil.sendEventMsg(RemoteViewUpdateType.QQ_LOGIN_OUT,"",null);
            MesUtil.sendEventMsg(RemoteViewUpdateType.LOAD_END,"ERR:取消授权",null);
        }
    }

}
