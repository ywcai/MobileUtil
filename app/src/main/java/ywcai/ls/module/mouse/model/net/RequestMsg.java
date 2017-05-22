package ywcai.ls.module.mouse.model.net;

import org.apache.mina.core.future.WriteFuture;

import ywcai.ls.mina.socket.ClientSocket;
import ywcai.ls.module.mouse.lsenum.ByteType;
import ywcai.ls.module.mouse.model.ComponentStatus;


public class RequestMsg {
    private MakeJson mj = new MakeJson();
    private ClientSocket session= ComponentStatus.getInstance().socket;
    public WriteFuture sendJson(String type, String content) {
        byte[] jsonData = mj.getByte(type, content);
        byte[] data = new byte[jsonData.length + 1];
        data[0]=ByteType.BYTE_JSON.getHead();
        System.arraycopy(jsonData,0,data,1,jsonData.length);
        return  session.sentMes(data);
    }
    public WriteFuture sendJson(ClientSocket session,String type,String content) {
        byte[] jsonData = mj.getByte(type, content);
        byte[] data = new byte[jsonData.length + 1];
        data[0]=ByteType.BYTE_JSON.getHead();
        System.arraycopy(jsonData,0,data,1,jsonData.length);
        return session.sentMes(data);
    }
    public WriteFuture sendByte(byte[] payload) {
        byte[] data = new byte[payload.length + 1];
        data[0]=ByteType.BYTE_BYTE.getHead();
        System.arraycopy(payload,0,data,1,payload.length);
        return session.sentMes(data);
    }

}
