package ywcai.ls.module.mouse.presenter;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import org.apache.mina.core.session.IoSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ywcai.ls.controls.pull.inf.WorkTask;
import ywcai.ls.mina.socket.ClientSocket;
import ywcai.ls.mina.socket.SocketEventListener;
import ywcai.ls.module.mouse.lsenum.MsgType;
import ywcai.ls.module.mouse.lsenum.MsgValue;
import ywcai.ls.module.mouse.lsenum.ViewType;
import ywcai.ls.module.mouse.model.handler.ConnHandler;
import ywcai.ls.module.mouse.model.net.RequestMsg;
import ywcai.ls.module.mouse.model.net.ResponseMsg;
import ywcai.ls.module.mouse.model.net.ScanCoreProcess;
import ywcai.ls.module.mouse.model.ComponentStatus;;
import ywcai.ls.module.mouse.model.net.SocketOperation;
import ywcai.ls.module.mouse.model.util.TimeCounter;
import ywcai.ls.module.mouse.presenter.inf.ActionInf;
import ywcai.ls.module.mouse.presenter.inf.TimerCountInf;
import ywcai.ls.module.mouse.presenter.inf.UpdateViewInf;
import ywcai.ls.common.net.LocalInfo;


import ywcai.ls.util.MyUtil;

public class ActionImpl implements ActionInf, WorkTask, SocketEventListener, TimerCountInf {
    private UpdateViewInf updateView = null;
    private ComponentStatus status = null;
    private String selectIp;
    private Handler handler;
    private ClientSocket tempSocket;
    private TimeCounter timeCounter;
    private int countNum = 10;
    private int repeatTime=0;
    private int repeatStep=10000;
    private int repeatMaxNum=61000;

    public ActionImpl(UpdateViewInf inf) {
        updateView = inf;
        handler = new ConnHandler(updateView);
    }

    @Override
    public void InitConnStatus() {
        status = ComponentStatus.getInstance();
        switch (status.viewType) {
            case NONE:
                updateView.setPullViewStatus(false, new ArrayList());
                break;
            default:
                updateView.setPullViewStatus(true, status.cacheList);
                break;
        }
    }

    @Override
    public void requestConn() {
        tempSocket = new ClientSocket();
        tempSocket.addListener(this);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        SocketOperation socketOperation = new SocketOperation(tempSocket, selectIp);
        executorService.execute(socketOperation);
    }
    public void repeatConn()
    {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        SocketOperation socketOperation = new SocketOperation(status.socket, selectIp);
        executorService.execute(socketOperation);
    }



    @Override
    public List execute(int i) {
        List tempList = new ArrayList();
        if (i == 1) {
            status.cacheList.clear();
            LocalInfo localInfo = new LocalInfo(updateView.getContext());
            status.localIp = localInfo.getLocalIp();
            tempList.addAll(localInfo.getLocalList(status.localIp));
        }
        ScanCoreProcess scanCoreProcess = new ScanCoreProcess(status.localIp);
        tempList.addAll(scanCoreProcess.scan(i));
        status.cacheList.addAll(tempList);
        return tempList;
    }

    @Override
    public void onItemClick(View view, int i, Object o) {
        if(status.viewType!=ViewType.NONE&&!status.socket.getSessionStatus())
        {
            return;
        }
        HashMap hashMap = (HashMap) o;
        selectIp = hashMap.get("lanIp").toString();
        if (selectIp.equals(status.localIp)) {
            return;
        }
        if (status.viewType == ViewType.NONE) {
            updateView.onClickListItem();
        } else {
            updateView.sessionChecked(true, "已存在连接，重定向到下一步");
        }
    }

    private void notifyHandler(MsgValue response, int args) {
        Message msg = new Message();
        msg.obj = response;
        msg.arg1 = args;
        handler.sendMessage(msg);
    }

    private void checkPsw() {
        RequestMsg requestMsg = new RequestMsg();
        requestMsg.sendJson(tempSocket, MsgType.CheckPsw.getStr(), updateView.getPsw());
        timeCounter = new TimeCounter(this);
        timeCounter.setStop(false);
        timeCounter.startCounter(countNum);
    }

    @Override
    public void check(int count) {
        if (timeCounter.isStop()) {
            return;
        }
        if (count <= 0) {
            notifyHandler(MsgValue.WAIT_TIME_OUT, count);
            tempSocket.CloseSession();
        } else {
            notifyHandler(MsgValue.SESSION_CHECKING, count);
        }
    }

    @Override
    public void messageReceived(IoSession ioSession, byte[] bytes) {
        ResponseMsg responseMsg = new ResponseMsg(bytes);
        MsgType type = MsgType.valueOf(responseMsg.getResponseType());
        MsgValue response = MsgValue.valueOf(responseMsg.getResponseContent());
        switch (type) {
            case BackResult:
                timeCounter.setStop(true);
                switch (response) {
                    case SUCCESS:
                        status.SaveSession(tempSocket);
                        break;
                    case FAIL:
                        clearTempSocket();
                        break;
                }
                notifyHandler(response, 0);
                break;
            case response_shadow:
                switch (response) {
                    case CLOSE:
                        status.ExitShadow();
                        notifyHandler(response, 0);
                        break;
                }
                break;
        }
    }
    private void clearTempSocket()
    {
        tempSocket.removeListener(this);
        tempSocket.CloseSession();
        tempSocket = null;
    }

    @Override
    public void errorCatch(IoSession ioSession, Throwable throwable) {
        MyUtil.saveLogText
                ("ERR: " + MyUtil.getDetailTime() + "\n"
                        + throwable.getCause().toString() + "\n"
                        + throwable.getCause().getMessage() + "\n\n");
    }

    @Override
    public void messageSent(IoSession ioSession, Object o) {

    }

    @Override
    public void sessionClosed(IoSession ioSession) {
        if (status.viewType!=ViewType.NONE) {
            notifyHandler(MsgValue.REPEAT_START, 0);
            repeatTime=0;
            repeatConn();
        }
        else
        {
            notifyHandler(MsgValue.SESSION_CLOSE,0);
        }
    }

    @Override
    public void sessionCreateStart(String s, int i) {
        if (status.viewType == ViewType.NONE) {
            notifyHandler(MsgValue.SESSION_OPEN_START, 0);
        }
    }
    @Override
    public void sessionCreated(IoSession ioSession) {

    }

    @Override
    public void sessionOpened(IoSession ioSession) {

    }

    @Override
    public void sessionCreateEnd(IoSession ioSession, boolean b) {
        if (status.viewType != ViewType.NONE) {
            if (!b) {
                repeatFail();
            }
            else {
                repeatSuccess();
            }
            return;
        }
        if (b) {
            notifyHandler(MsgValue.SESSION_OPEN_SUCCESS, 0);
            checkPsw();
        } else {
            notifyHandler(MsgValue.SESSION_OPEN_FAIL, 0);
        }
    }
    private void repeatFail()
    {
        repeatTime+=repeatStep;
        if(repeatTime>=repeatMaxNum)
        {
            stopRepeatConn();
        }
        else
        {
            stepRepeatConn();
        }
    }
    private void repeatSuccess()
    {
        notifyHandler(MsgValue.REPEAT_SUCCESS, 0);
    }
    private void stopRepeatConn()
    {
        status.QuitSession();
        notifyHandler(MsgValue.REPEAT_STOP,0);
    }
    private void stepRepeatConn()
    {
        notifyHandler(MsgValue.REPEAT_FAIL,repeatStep);
        try {
            Thread.sleep(repeatStep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repeatConn();
    }

    @Override
    public void showIpConfigBox() {
        if (status.viewType == ViewType.NONE) {
            updateView.onClickSetIpBtn();
        }
    }

    @Override
    public void setIp() {
        selectIp = updateView.getIp();
        updateView.onClickListItem();
    }

    @Override
    public void breakScanManual() {
        if (status.viewType == ViewType.NONE) {
            updateView.onClickBreakBtn();
        }
    }
}
