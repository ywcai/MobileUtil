package ywcai.ls.util.statics;
import com.google.gson.Gson;
import org.apache.mina.core.future.WriteFuture;
import org.greenrobot.eventbus.EventBus;
import java.io.UnsupportedEncodingException;
import ywcai.ls.common.em.MouseViewUpdateType;
import ywcai.ls.common.eventbus.MouseViewUpdate;
import ywcai.ls.mina.socket.ClientSocket;
import ywcai.ls.common.em.RemoteViewUpdateType;
import ywcai.ls.common.eventbus.RemoteViewUpdate;
import ywcai.ls.common.ApplicationProtocol;

public class MesUtil {
    public static WriteFuture sendJson(ClientSocket session, int type, String content) {
        String str = getJsonObj(type, content);
        byte[] temp = null;
        try {
            temp = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] data = new byte[temp.length + 1];
        data[0] = ResultCode.byte_head_json;
        System.arraycopy(temp, 0, data, 1, temp.length);
        return session.sentMes(data);
    }
    public static WriteFuture sendByte(ClientSocket session, byte[] payload) {
        byte[] data = new byte[payload.length + 1];
        data[0] = ResultCode.byte_head_byte;
        System.arraycopy(payload, 0, data, 1, payload.length);
        return session.sentMes(data);
    }
    public static ApplicationProtocol getObj(byte[] payload) {
        String s = "";
        try {
            s = new String(payload, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Gson g = new Gson();
        return g.fromJson(s, ApplicationProtocol.class);
    }
    private static String getJsonObj(int type, String content) {
        Gson gson = new Gson();
        ApplicationProtocol applicationProtocol = new ApplicationProtocol();
        applicationProtocol.type = type;
        applicationProtocol.content = content;
        String string = gson.toJson(applicationProtocol);
        return string;
    }
    public static void sendEventMsg(RemoteViewUpdateType remoteViewUpdateType, String content, Object obj) {
        RemoteViewUpdate remoteViewUpdate = new RemoteViewUpdate();
        remoteViewUpdate.remoteViewUpdateType = remoteViewUpdateType;
        remoteViewUpdate.tip = content;
        remoteViewUpdate.obj = obj;
        EventBus.getDefault().post(remoteViewUpdate);
    }
    public static void sendEventMsgForMouse(MouseViewUpdateType mouseViewUpdateType, String content, Object obj) {
        MouseViewUpdate mouseViewUpdate = new MouseViewUpdate();
        mouseViewUpdate.mouseViewUpdateType = mouseViewUpdateType;
        mouseViewUpdate.tip = content;
        mouseViewUpdate.obj = obj;
        EventBus.getDefault().post(mouseViewUpdate);
    }
}
