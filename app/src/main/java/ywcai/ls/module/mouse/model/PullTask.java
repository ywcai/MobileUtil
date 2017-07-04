package ywcai.ls.module.mouse.model;

import android.view.View;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ywcai.ls.common.ComponentStatus;
import ywcai.ls.common.em.MouseViewType;
import ywcai.ls.common.em.MouseViewUpdateType;
import ywcai.ls.common.net.LocalInfo;
import ywcai.ls.common.net.ScanCoreProcess;
import ywcai.ls.controls.pull.inf.WorkTask;
import ywcai.ls.module.mouse.presenter.ActionImpl;
import ywcai.ls.module.mouse.presenter.inf.ActionInf;
import ywcai.ls.util.statics.MesUtil;

/**
 * Created by zmy_11 on 2017/6/30.
 */

public class PullTask implements WorkTask {
    //    ComponentStatus status=ComponentStatus.getInstance();
    String localIp="192.168.1.1";

    @Override
    public List execute(int i) {
        List tempList = new ArrayList();
        if (i == 1) {
            LocalInfo localInfo = new LocalInfo();
            localIp = localInfo.getLocalIp();
            tempList.addAll(localInfo.getLocalList(localIp));
        }
        ScanCoreProcess scanCoreProcess = new ScanCoreProcess(localIp);
        tempList.addAll(scanCoreProcess.scan(i));
        return tempList;
    }

    @Override
    public void onItemClick(View view, int i, Object o) {
        if (i == 0) {
            return;
        }
        HashMap hashMap = (HashMap) o;
        String selectIp = hashMap.get("lanIp").toString();
        MesUtil.sendEventMsgForMouse(MouseViewUpdateType.CLICK_LIST_VIEW,selectIp,null);

    }
}
