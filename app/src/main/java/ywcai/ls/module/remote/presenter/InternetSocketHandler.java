package ywcai.ls.module.remote.presenter;

import org.apache.mina.core.session.IoSession;

import ywcai.ls.mina.socket.SocketEventListener;
import ywcai.ls.common.ComponentStatus;
import ywcai.ls.common.em.RemoteViewUpdateType;
import ywcai.ls.util.statics.MesUtil;


/**
 * Created by zmy_11 on 2017/6/26.
 */

public class InternetSocketHandler implements SocketEventListener {
    @Override
    public void sessionCreated(IoSession ioSession) {

    }

    @Override
    public void sessionOpened(IoSession ioSession) {

    }

    @Override
    public void messageReceived(IoSession ioSession, byte[] bytes) {
        RemoteProcess remoteProcess =new RemoteProcess(bytes);
        remoteProcess.execute();
    }

    @Override
    public void errorCatch(IoSession ioSession, Throwable throwable) {

    }

    @Override
    public void messageSent(IoSession ioSession, Object o) {

    }

    @Override
    public void sessionClosed(IoSession ioSession) {
        ComponentStatus.getInstance().internetSocket=null;
        MesUtil.sendEventMsg(RemoteViewUpdateType.INTERNET_SESSION_CLOSED,"网络连接中断",null);
    }

    @Override
    public void sessionCreateStart(String s, int i) {

    }

    @Override
    public void sessionCreateEnd(IoSession ioSession, boolean b) {
        if(b)
        {
            MesUtil.sendEventMsg(RemoteViewUpdateType.CONNECT_OK,"创建连接成功，正在发起注册请求...",null);
        }
        else
        {
            MesUtil.sendEventMsg(RemoteViewUpdateType.CONNECT_FAIL,"创建连接失败，退出QQ登录!",null);
        }
    }
}
