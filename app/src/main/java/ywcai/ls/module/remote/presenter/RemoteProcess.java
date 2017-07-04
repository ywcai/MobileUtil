package ywcai.ls.module.remote.presenter;

import ywcai.ls.util.statics.ResultCode;

/**
 * Created by zmy_11 on 2017/6/27.
 */

public class RemoteProcess {
    public int byteType;
    public byte[] payLoad;
    public RemoteProcess(byte[] _bytes)
    {
            byteType=_bytes[0];
        setPayLoad(_bytes);
    }
    private void setPayLoad(byte[] _bytes)
    {
            payLoad = new byte[_bytes.length - 1];
            System.arraycopy(_bytes, 1, payLoad, 0, payLoad.length);
    }
    public void execute()
    {
        switch (byteType)
        {
            case ResultCode.byte_head_byte:
                ReceiveImage receiveImage=new ReceiveImage();
                receiveImage.doImage(payLoad);
                break;
            case ResultCode.byte_head_json:
                ReceiveCmd receiveCmd=new ReceiveCmd();
                receiveCmd.execute(payLoad);
                break;
        }
    }
}
