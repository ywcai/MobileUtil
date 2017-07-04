package ywcai.ls.common.net;



public class ResponseMsg {
    public int dataType;
    public byte[] payLoad;
    public ResponseMsg(byte[] msg)
    {
        dataType=msg[0];
        payLoad = new byte[msg.length - 1];
        System.arraycopy(msg, 1, payLoad, 0, payLoad.length);
    }
}
