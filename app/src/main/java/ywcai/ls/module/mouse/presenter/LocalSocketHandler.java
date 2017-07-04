package ywcai.ls.module.mouse.presenter;

import org.apache.mina.core.session.IoSession;

import ywcai.ls.common.ComponentStatus;
import ywcai.ls.common.em.MouseViewType;
import ywcai.ls.common.em.MouseViewUpdateType;
import ywcai.ls.mina.socket.SocketEventListener;
import ywcai.ls.util.statics.MesUtil;


/**
 * Created by zmy_11 on 2017/6/30.
 */

public class LocalSocketHandler implements SocketEventListener {
    ComponentStatus status=ComponentStatus.getInstance();
    @Override
    public void sessionCreated(IoSession ioSession) {

    }

    @Override
    public void sessionOpened(IoSession ioSession) {

    }

    @Override
    public void messageReceived(IoSession ioSession, byte[] bytes) {
         new MouseProcess(bytes);
    }

    @Override
    public void errorCatch(IoSession ioSession, Throwable throwable) {
    }

    @Override
    public void messageSent(IoSession ioSession, Object o) {

    }

    @Override
    public void sessionClosed(IoSession ioSession) {
        MesUtil.sendEventMsgForMouse(MouseViewUpdateType.SESSION_CLOSED, "TCP连接中断,10秒后再次自动尝试重连!", null);
    }

    @Override
    public void sessionCreateStart(String s, int i) {
//        if (status.mouseViewType == MouseViewType.NONE) {
//            notifyHandler(MsgValue.SESSION_OPEN_START, 0);
//        }
    }

    @Override
    public void sessionCreateEnd(IoSession ioSession, boolean b) {

        if (b) {
            switch (status.mouseViewType) {
                case NONE:
                    MesUtil.sendEventMsgForMouse(MouseViewUpdateType.SHOW_PSW_BOX, "创建连接成功,输入连接验证!", null);
                    break;
                default:
                    MesUtil.sendEventMsgForMouse(MouseViewUpdateType.REPEAT_SUCCESS, "重连成功!", null);
                    break;
            }
        } else {
            switch (status.mouseViewType) {
                case NONE:
                    MesUtil.sendEventMsgForMouse(MouseViewUpdateType.LOAD_END, "创建连接失败", null);
                    break;
                default:
                    MesUtil.sendEventMsgForMouse(MouseViewUpdateType.SESSION_CLOSED, "TCP连接中断,10秒后再次自动尝试重连!", null);
                    break;
            }

        }
    }
}
