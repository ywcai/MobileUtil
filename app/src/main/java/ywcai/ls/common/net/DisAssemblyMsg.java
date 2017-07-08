package ywcai.ls.common.net;



public class DisAssemblyMsg {
    public int dataType;
    public byte[] payLoad;
    public DisAssemblyMsg(byte[] msg)
    {
        dataType=msg[0];
        payLoad = new byte[msg.length - 1];
        System.arraycopy(msg, 1, payLoad, 0, payLoad.length);
    }
}
