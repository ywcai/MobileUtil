package ywcai.ls.common;

import ywcai.ls.common.draw.CatchScreenInf;
import ywcai.ls.mina.socket.ClientSocket;
import ywcai.ls.common.em.MouseViewType;


public class ComponentStatus {
    private static Object lock = new Object();
    public ClientSocket socket=null ,internetSocket=null ;
    public String qqUserId,deviceId;
    public MouseViewType mouseViewType = MouseViewType.NONE;
    public CatchScreenInf catchScreen = null;
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
}
