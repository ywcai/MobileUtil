package ywcai.ls.module.mouse.presenter;

import android.content.Intent;

import ywcai.ls.common.ComponentStatus;
import ywcai.ls.common.em.MouseViewType;
import ywcai.ls.common.em.MouseViewUpdateType;
import ywcai.ls.common.net.DisAssemblyMsg;
import ywcai.ls.common.ApplicationProtocol;
import ywcai.ls.common.service.PushShadowService;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.util.statics.MesUtil;
import ywcai.ls.util.statics.ResultCode;

/**
 * Created by zmy_11 on 2017/6/30.
 */
public class MouseProcess {
    private ComponentStatus status=ComponentStatus.getInstance();
    public MouseProcess(byte[] bytes) {
        DisAssemblyMsg disAssemblyMsg = new DisAssemblyMsg(bytes);
        switch (disAssemblyMsg.dataType) {
            case ResultCode.byte_head_byte:
                //doNothing;
                doBytes(disAssemblyMsg.payLoad);
                break;
            case ResultCode.byte_head_json:
                ApplicationProtocol applicationProtocol = MesUtil.getObj(disAssemblyMsg.payLoad);
                selectWorkType(applicationProtocol);
                break;
            default:
//                MesUtil.sendEventMsgForMouse(MouseViewUpdateType.SHOW_TOAST,"收到数据:"+disAssemblyMsg.dataType,null);
                break;
        }
    }
    private void doBytes(byte[] payLoad) {

    }
    private void selectWorkType(ApplicationProtocol applicationProtocol) {
        switch (applicationProtocol.type) {
            case ResultCode.json_type_notify_back_check_success:
                status.mouseViewType= MouseViewType.CONN;
                MesUtil.sendEventMsgForMouse(MouseViewUpdateType.LOAD_CONN_VIEW,"连接服务端成功!",null);
                break;
            case ResultCode.json_type_notify_back_check_fail:
                status.socket.CloseSession();
                status.mouseViewType=MouseViewType.NONE;
                MesUtil.sendEventMsgForMouse(MouseViewUpdateType.LOAD_NONE_VIEW,"密码校验失败!",null);
                break;
            case ResultCode.json_type_notify_back_shadow_open_ok:
                startShadow();
                break;
            case ResultCode.json_type_notify_back_shadow_open_fail:
                status.mouseViewType= MouseViewType.CONN;
                MesUtil.sendEventMsgForMouse(MouseViewUpdateType.LOAD_CONN_VIEW,"远端被占用",null);
                break;
            case ResultCode.json_type_notify_back_shadow_close:
                status.mouseViewType= MouseViewType.CONN;
                MesUtil.sendEventMsgForMouse(MouseViewUpdateType.LOAD_CONN_VIEW,"远端终止了你的桌面投影",null);
                break;
            case ResultCode.json_type_notify_back_mouse_open_ok:
                status.mouseViewType= MouseViewType.MOUSE;
                MesUtil.sendEventMsgForMouse(MouseViewUpdateType.LOAD_MOUSE_VIEW,"进入空中鼠标模式",null);
                break;
            case ResultCode.json_type_notify_back_mouse_open_fail:
                status.mouseViewType= MouseViewType.CONN;
                MesUtil.sendEventMsgForMouse(MouseViewUpdateType.LOAD_CONN_VIEW,"远端被占用",null);
                break;
        }
    }
    private void startShadow()
    {
        status.mouseViewType=MouseViewType.SHADOW;
        MesUtil.sendEventMsgForMouse(MouseViewUpdateType.LOAD_SHADOW_VIEW,"进入投影模式",null);
        MyApplication.getInstance().getApplicationContext().
                startService(new Intent(MyApplication.getInstance().getApplicationContext(), PushShadowService.class));
    }
}
