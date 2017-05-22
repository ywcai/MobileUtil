package ywcai.ls.module.mouse.model.handler;

import android.os.Handler;
import android.os.Message;

import ywcai.ls.module.mouse.lsenum.MsgValue;
import ywcai.ls.module.mouse.presenter.inf.UpdateViewInf;


public class ConnHandler extends Handler {
    private UpdateViewInf updateView;
    public ConnHandler(UpdateViewInf inf)
    {
        updateView=inf;
    }
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        MsgValue response=(MsgValue)msg.obj;
        switch (response)
        {
            case SUCCESS:
                updateView.sessionChecked(true,response.getStr());
                break;
            case FAIL:
                updateView.sessionChecked(false,response.getStr());
                break;
            case SESSION_OPEN_START:
                updateView.sessionCreating();
                break;
            case SESSION_OPEN_SUCCESS:
                updateView.sessionCreated(true);
                break;
            case SESSION_OPEN_FAIL:
                updateView.sessionCreated(false);
                break;
            case SESSION_CLOSE:
                updateView.sessionClosed();
                break;
            case WAIT_TIME_OUT:
                updateView.sessionChecked(false,response.getStr());
                break;
            case SESSION_CHECKING:
                updateView.sessionChecking(msg.arg1);
                break;
            case CLOSE:
                updateView.showInfo("远端PC断开投影!");
                break;
            case REPEAT_START:
                updateView.showInfo("网络异常，系统正在尝试自动重接!");
                break;
            case REPEAT_SUCCESS:
                updateView.showInfo("自动重连成功!");
                break;
            case REPEAT_FAIL:
                updateView.showInfo("重连失败，"+msg.arg1/1000+"秒后系统再次重试连接!");
                break;
            case REPEAT_STOP:
                updateView.showInfo("重连失败，退出会话，建议检查网络或软件后重新开启!");
                break;
        }
    }
}
