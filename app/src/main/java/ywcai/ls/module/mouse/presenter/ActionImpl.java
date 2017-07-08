package ywcai.ls.module.mouse.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import com.google.gson.Gson;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ywcai.ls.common.DeviceInfo;
import ywcai.ls.common.draw.CatchScreen;
import ywcai.ls.common.em.MouseViewType;
import ywcai.ls.common.em.MouseViewUpdateType;
import ywcai.ls.mina.socket.ClientSocket;
import ywcai.ls.common.ComponentStatus;;
import ywcai.ls.module.mouse.presenter.inf.ActionInf;
import ywcai.ls.util.InputRule;
import ywcai.ls.util.LookDeviceUtil;
import ywcai.ls.util.statics.MesUtil;
import ywcai.ls.util.statics.MyConfig;
import ywcai.ls.util.statics.ResultCode;

public class ActionImpl implements ActionInf {
    private ComponentStatus instance = ComponentStatus.getInstance();
    private ClientSocket tempSocket;
    private String remoteIp;
    private DeviceInfo localDeviceInfo;

    @Override
    public void requestConn(String ip) {
        remoteIp=ip;
        InputRule rule = new InputRule();
        if (!rule.checkIpRule(ip)) {
            MesUtil.sendEventMsgForMouse(MouseViewUpdateType.SHOW_TOAST, "ip格式验证错误!", null);
            return;
        }
        ;
        MesUtil.sendEventMsgForMouse(MouseViewUpdateType.LOADING, "正在向" + remoteIp + "请求建立连接...", null);
        tempSocket = new ClientSocket();
        LocalSocketHandler localSocketHandler = new LocalSocketHandler();
        tempSocket.addListener(localSocketHandler);
        instance.socket = tempSocket;
        new Thread(new Runnable() {
            @Override
            public void run() {
                tempSocket.CreateSession(remoteIp, MyConfig.INT_SOCKET_PORT);
            }
        }).start();
    }
    @Override
    public void checkPsw(final String psw) {
        InputRule rule = new InputRule();
        localDeviceInfo = new DeviceInfo();
        localDeviceInfo.accessCode = psw;
        LookDeviceUtil look = new LookDeviceUtil();
        localDeviceInfo.deviceId = look.getDeviceId();
        localDeviceInfo.deviceName = look.getDeviceName();
        Gson gson = new Gson();
        final String json = gson.toJson(localDeviceInfo);
        if (!rule.checkPswRule(psw)) {
            MesUtil.sendEventMsgForMouse(MouseViewUpdateType.SHOW_TOAST, "密码格式验证错误!", null);
            return;
        }
        MesUtil.sendEventMsgForMouse(MouseViewUpdateType.LOADING, "等待服务器返回校验结果...", null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MesUtil.sendJson(tempSocket, ResultCode.json_type_req_local_check, json);
            }
        }).start();
    }

    @Override
    public void cancalConn() {
        instance.mouseViewType = MouseViewType.NONE;
        tempSocket.CloseSession();
        MesUtil.sendEventMsgForMouse(MouseViewUpdateType.LOAD_NONE_VIEW, "", null);
    }

    @Override
    public void clickShadowBtn(Activity _activity) {
        if (MouseViewType.SHADOW == instance.mouseViewType) {
            reqCloseShadow();
            return;
        }
        if (MouseViewType.CONN == instance.mouseViewType) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                MesUtil.sendEventMsgForMouse(MouseViewUpdateType.SHOW_TOAST, "投影模式不支持5.0以下系统!", null);
                return;
            }
            if (!tempSocket.getSessionStatus()) {
                MesUtil.sendEventMsgForMouse(MouseViewUpdateType.SHOW_TOAST, "连接已中断，正在自动重新连接!", null);
            } else {
                CatchScreen catchScreen = new CatchScreen();
                catchScreen.requestPermission(_activity);
            }
        }
    }

    private void reqCloseShadow() {
        instance.mouseViewType = MouseViewType.CONN;
        instance.catchScreen.stopCatch();
        MesUtil.sendEventMsgForMouse(MouseViewUpdateType.LOAD_CONN_VIEW, "退出投影模式", null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(tempSocket.getSessionStatus()) {
                    MesUtil.sendJson(tempSocket, ResultCode.json_type_req_local_close_shadow, "");
                }
            }
        }).start();
    }

    @Override
    public void clickMouseBtn() {
        if (!instance.socket.getSessionStatus()) {
            MesUtil.sendEventMsgForMouse(MouseViewUpdateType.SHOW_TOAST, "连接已中断!", null);
            return;
        }
        if (instance.mouseViewType == MouseViewType.CONN) {
            MesUtil.sendEventMsgForMouse(MouseViewUpdateType.LOADING, "请求控制，正在等待服务端响应...", null);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MesUtil.sendJson(tempSocket, ResultCode.json_type_req_local_open_mouse, "");
                }
            }).start();
        }
        if (instance.mouseViewType == MouseViewType.SHADOW) {
            MesUtil.sendEventMsgForMouse(MouseViewUpdateType.SHOW_TOAST, "当前已进入投影模式!", null);
        }
        if (instance.mouseViewType == MouseViewType.NONE) {
            MesUtil.sendEventMsgForMouse(MouseViewUpdateType.SHOW_TOAST, "当前无可用连接", null);
        }
        if (instance.mouseViewType == MouseViewType.MOUSE) {
            MesUtil.sendEventMsgForMouse(MouseViewUpdateType.SHOW_TOAST, "已当前进入鼠标模式", null);
        }
    }

    @Override
    public void startShadow(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            CatchScreen catchScreen = new CatchScreen();
            catchScreen.initScreen(data);
            instance.catchScreen = catchScreen;
            MesUtil.sendEventMsgForMouse(MouseViewUpdateType.LOADING, "初始化屏幕数据，等待服务端响应...", null);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MesUtil.sendJson(tempSocket, ResultCode.json_type_req_local_open_shadow, instance.catchScreen.getScreenSize());
                }
            }).start();
        } else {
            MesUtil.sendEventMsgForMouse(MouseViewUpdateType.SHOW_TOAST, "请求截屏权限失败!", null);
        }
    }

    @Override
    public void repeatConn() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tempSocket.CreateSession(remoteIp, MyConfig.INT_SOCKET_PORT);
            }
        }).start();
    }
    @Override
    public void repeatConnSuccess() {
        localDeviceInfo.status=instance.mouseViewType.getId()+"";
        localDeviceInfo.userId=instance.catchScreen.getScreenSize()+"|"+"v";//userId在该插件未使用，用于携带设备屏幕尺寸和方向,V代表竖，H代表横屏
        MesUtil.sendEventMsgForMouse(MouseViewUpdateType.SHOW_TOAST, "TCP自动重连成功", null);
        Gson gson = new Gson();
        final String json = gson.toJson(localDeviceInfo);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MesUtil.sendJson(tempSocket, ResultCode.json_type_req_local_repeat_conn, json);
            }
        }).start();
    }

}
