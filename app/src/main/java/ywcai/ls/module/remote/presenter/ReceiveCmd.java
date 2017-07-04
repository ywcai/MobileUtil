package ywcai.ls.module.remote.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.UnsupportedEncodingException;
import java.util.List;
import ywcai.ls.common.em.RemoteViewUpdateType;
import ywcai.ls.common.DeviceInfo;
import ywcai.ls.common.ApplicationProtocol;
import ywcai.ls.util.statics.MesUtil;
import ywcai.ls.util.statics.ResultCode;

/**
 * Created by zmy_11 on 2017/6/27.
 */
public class ReceiveCmd {
    private int type;
    private String content;
    public void execute(byte[] _payLoad) {
        setJson(_payLoad);
        coreSelect();
    }
    private void setJson(byte[] _payLoad)
    {
        String str="";
        try {
            str=new String (_payLoad,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Gson gson=new Gson();
        ApplicationProtocol applicationProtocol=gson.fromJson(str,ApplicationProtocol.class);
        type=applicationProtocol.type;
        content=applicationProtocol.content;
    }
    private void coreSelect() {
        switch (type)
        {
            case ResultCode.json_type_notify_back_reg_fail:
                //注册失败;
                regFail();
                break;
            case ResultCode.json_type_notify_back_reg_ok_with_list:
                //注册成功，返回设备列表信息;
                regOk();
                break;
            case ResultCode.json_type_notify_back_add_temp_fail:
                //添加失败
                break;
            case ResultCode.json_type_notify_back_change_abs_fail:

                break;
            case ResultCode.json_type_notify_back_change_code_fail:

                break;
            case ResultCode.json_type_notify_back_change_detail_fail:

                break;
            case ResultCode.json_type_notify_loop_add_temp_ok:

                break;
            case ResultCode.json_type_notify_loop_change_abs_ok:

                break;
            case ResultCode.json_type_notify_loop_change_code_ok:

                break;
            case ResultCode.json_type_notify_loop_change_detail_ok:

                break;
            case ResultCode.json_type_notify_loop_process_down:

                break;
            case ResultCode.json_type_notify_loop_process_up:

                break;
            case ResultCode.json_type_notify_loop_turn_busy:
                deviceTurnBusy(content);
                break;
            case ResultCode.json_type_notify_loop_turn_free:
                deviceTurnOn(content);
                break;
            case ResultCode.json_type_notify_loop_turn_off:
                deviceTurnOff(content);
                break;
            case ResultCode.json_type_notify_loop_turn_on:
                deviceTurnOn(content);
                break;
            case ResultCode.json_type_switch_control_key:

                break;
            case ResultCode.json_type_switch_control_mouse:

                break;
            case ResultCode.json_type_switch_desk_config:

                break;
        }
    }



    private void regFail() {
        MesUtil.sendEventMsg(RemoteViewUpdateType.REG_FAIL,content,null);
    }
    private void regOk() {
        Gson gson=new Gson();
        List<DeviceInfo> deviceInfoList=gson.fromJson(content,new TypeToken<List<DeviceInfo>>() {
        }.getType());
        MesUtil.sendEventMsg(RemoteViewUpdateType.REG_OK,"登录成功!",deviceInfoList);
    }
    private void deviceTurnOn(String content) {
        MesUtil.sendEventMsg(RemoteViewUpdateType.TURN_ON,content,null);
    }
    private void deviceTurnBusy(String content) {
        MesUtil.sendEventMsg(RemoteViewUpdateType.TURN_BUSY, content, null);
    }
    private void deviceTurnOff(String content) {
        MesUtil.sendEventMsg(RemoteViewUpdateType.TURN_OFF,content,null);
    }



}
