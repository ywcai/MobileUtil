package ywcai.ls.module.mouse.model.handler;

import android.os.Handler;
import android.os.Message;

import ywcai.ls.module.mouse.lsenum.MsgValue;
import ywcai.ls.module.mouse.lsenum.ViewType;
import ywcai.ls.module.mouse.presenter.inf.UpdateActivityInf;


public class ShadowHandler extends Handler {
    private UpdateActivityInf updateView;
    public ShadowHandler(UpdateActivityInf inf)
    {
        updateView=inf;
    }
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        MsgValue response=(MsgValue)msg.obj;
        switch (response)
        {
            case True:
                updateView.changeViewType(ViewType.SHADOW);
                updateView.showTip("成功进入投影模式！");
                break;
            case False:
                updateView.changeViewType(ViewType.CONN);
                updateView.showTip("远端PC不允许建立投影！");
                break;
            case CLOSE:
                updateView.changeViewType(ViewType.CONN);
                break;
            case SESSION_CLOSE:
                updateView.disconnectSession();
                break;
        }
    }
}
