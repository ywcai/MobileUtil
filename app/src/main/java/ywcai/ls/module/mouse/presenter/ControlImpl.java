package ywcai.ls.module.mouse.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import ywcai.ls.mina.socket.ClientSocket;
import ywcai.ls.mina.socket.SocketEventListener;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.module.mouse.model.service.ShadowService;
import ywcai.ls.module.mouse.lsenum.MsgType;
import ywcai.ls.module.mouse.lsenum.MsgValue;
import ywcai.ls.module.mouse.lsenum.ViewType;
import ywcai.ls.module.mouse.model.ComponentStatus;
import ywcai.ls.module.mouse.model.MouseEvent;
import ywcai.ls.module.mouse.model.net.RequestMsg;
import ywcai.ls.module.mouse.model.net.ResponseMsg;
import ywcai.ls.module.mouse.model.handler.ShadowHandler;
import ywcai.ls.module.mouse.presenter.inf.ControlInf;
import ywcai.ls.module.mouse.presenter.inf.MouseEventInf;
import ywcai.ls.module.mouse.presenter.inf.UpdateActivityInf;

public class ControlImpl implements ControlInf, SocketEventListener {
    private ComponentStatus status = ComponentStatus.getInstance();
    private UpdateActivityInf updateActivityInf;
    private MouseEventInf mouseEvent;
    private Handler myHandler;

    public ControlImpl(UpdateActivityInf _updateActivityInf) {
        updateActivityInf = _updateActivityInf;
        myHandler = new ShadowHandler(updateActivityInf);
    }

    @Override
    public void init() {
        if (status.socket != null) {
            status.socket.addListener(this);
            updateActivityInf.changeViewType(status.viewType);
        } else {
            updateActivityInf.disconnectSession();
        }
    }

    @Override
    public void clickMenuMouse() {
        switch (status.viewType) {
            case SHADOW:
                updateActivityInf.showTip("当前已进入投影模式！");
                break;
            case CONN:
                mouseEvent = new MouseEvent(updateActivityInf.getContext());
                status.viewType = ViewType.MOUSE;
                updateActivityInf.changeViewType(ViewType.MOUSE);
                break;
        }
    }

    @Override
    public void clickMenuShadow() {
        updateActivityInf.loadShadowMode();
        switch (status.viewType) {
            case SHADOW:
                sendCloseShadow();
                break;
            case CONN:
                startShadow();
                break;
        }
    }

    private void sendCloseShadow() {
        RequestMsg req = new RequestMsg();
        req.sendJson(MsgType.close_shadow.getStr(), MsgValue.CLOSE.getStr());
    }

    private void startShadow() {
        if (!status.CreateCatchScreen(updateActivityInf.getActivity())) {
            updateActivityInf.changeViewType(ViewType.CONN);
            updateActivityInf.showTip("投影模式不支持5.0以下系统!");
        }
    }

    @Override
    public void clickMenuDisConnect() {
        updateActivityInf.loadShadowMode();
        if(status.viewType==ViewType.SHADOW) {
            RequestMsg req = new RequestMsg();
            WriteFuture writeFuture=req.sendJson(MsgType.close_shadow.getStr(), MsgValue.CLOSE.getStr());
            writeFuture.awaitUninterruptibly();
        }
        ClientSocket temp=status.socket;
        status.QuitSession();
        if(temp!=null) {
            temp.CloseSession();
        }
    }

    @Override
    public void requestShadowConn(int requestCode, int resultCode, Intent data) {
        if (requestCode == 7 && resultCode == Activity.RESULT_OK) {
            String screenXY = status.InitScreen(resultCode, data);
            RequestMsg requestMsg = new RequestMsg();
            requestMsg.sendJson(MsgType.request_shadow.getStr(), screenXY);
        }
        if (requestCode == 7 && resultCode == Activity.RESULT_CANCELED) {
            status.ExitShadow();
            updateActivityInf.changeViewType(ViewType.CONN);
            updateActivityInf.showTip("未获取到求录屏权限！");
        }
    }

    @Override
    public void clickEsc() {
        mouseEvent.sendEsc();
    }


    @Override
    public void moveDown() {
        mouseEvent.sendMoveDown();
    }

    @Override
    public void moveUp() {
        mouseEvent.sendMoveUp();
    }

    @Override
    public void leftDown() {
        mouseEvent.sendLeftDown();
    }

    @Override
    public void leftUp() {
        mouseEvent.sendLeftUp();
    }

    @Override
    public void rightDown() {
        mouseEvent.sendRightDown();
    }

    @Override
    public void rightUp() {
        mouseEvent.sendRightUp();
    }

    @Override
    public void onTouchUp() {
        mouseEvent.sendPagePrev();
    }

    @Override
    public void onTouchDown() {
        mouseEvent.sendPageNext();
    }

    @Override
    public void clickExitMouse() {
        status.viewType = ViewType.CONN;
        updateActivityInf.changeViewType(ViewType.CONN);
    }

    @Override
    public void closeActivity() {
        //如果只是关闭了UI，则需要卸载socket的事件监听，不然重新打开UI会再次注册，造成重复输出
        if (status.socket != null) {
            status.socket.removeListener(this);
        }
    }

    private void startBackGroundService() {
                MyApplication.getInstance().getApplicationContext().
                        startService
                        (
                                new Intent(updateActivityInf.getActivity(), ShadowService.class)
                        );
    }
    private void notifyHandler(MsgValue value) {
        Message msg = new Message();
        msg.obj = value;
        myHandler.sendMessage(msg);
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
        MsgType type = MsgType.valueOf(responseMsg.getResponseType());
        MsgValue response = MsgValue.valueOf(responseMsg.getResponseContent());
        switch (type) {
            case response_shadow:
                switch (response) {
                    case True:
                        status.SaveShadowMode();
                        startBackGroundService();
                        break;
                    case FAIL:
                        status.ExitShadow();
                        break;
                    case CLOSE:
                        status.ExitShadow();
                        break;
                }
                notifyHandler(response);
                break;
        }
    }

    @Override
    public void errorCatch(IoSession ioSession, Throwable throwable) {

    }

    @Override
    public void messageSent(IoSession ioSession, Object o) {

    }

    @Override
    public void sessionClosed(IoSession ioSession) {
        notifyHandler(MsgValue.SESSION_CLOSE);
    }

    @Override
    public void sessionCreateStart(String s, int i) {

    }

    @Override
    public void sessionCreateEnd(IoSession ioSession, boolean b) {

    }
}
