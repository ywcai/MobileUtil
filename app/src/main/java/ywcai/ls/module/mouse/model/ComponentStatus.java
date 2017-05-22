package ywcai.ls.module.mouse.model;

import android.app.Activity;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.mina.socket.ClientSocket;
import ywcai.ls.module.mouse.lsenum.OnlineType;
import ywcai.ls.module.mouse.lsenum.ViewType;
import ywcai.ls.module.mouse.presenter.inf.CatchScreenInf;


public class ComponentStatus {
    private static Object lock = new Object();
    public ClientSocket socket = null;
    public ClientSocket internetSocket = null;//remote use
    public ViewType viewType = ViewType.NONE;
    public OnlineType onlineType=OnlineType.offline;//remote use
    public CatchScreenInf catchScreen = null;
    public String localIp = "0.0.0.0";
    public List cacheList = new ArrayList();
    public int selectDeviceID;
    public String selectDeviceName;
    private static ComponentStatus componentStatus = null;

    private ComponentStatus() {

    }

    public static ComponentStatus getInstance() {
        synchronized (lock) {
            if (componentStatus == null) {
                componentStatus = new ComponentStatus();
            }
        }
        return componentStatus;
    }


    public void QuitSession() {
        ExitShadow();
        viewType = ViewType.NONE;
        this.socket = null;
    }

    public void ExitShadow() {
        viewType = ViewType.CONN;
        catchScreen = null;
    }

    public void SaveSession(ClientSocket tempSocket) {
        socket = tempSocket;
        viewType = ViewType.CONN;
    }




    public boolean CreateCatchScreen(Activity activity) {
        ScreenFactory screenFactory = new ScreenFactory();
        catchScreen = screenFactory.getScreen();
        if (catchScreen != null) {
            catchScreen.requestPermission(activity);
            return true;
        }
        return false;
    }

    public String InitScreen(int resultCode, Intent data) {
        catchScreen.initScreen(resultCode, data);
        return catchScreen.getScreenSize();
    }

    public void SaveShadowMode() {
        viewType = ViewType.SHADOW;
    }
}
