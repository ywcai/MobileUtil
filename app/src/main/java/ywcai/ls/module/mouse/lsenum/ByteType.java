package ywcai.ls.module.mouse.lsenum;

/**
 * Created by zmy_11 on 2016/12/21.
 */

public enum ByteType {
    BYTE_JSON((byte)0xcf),
    BYTE_BYTE((byte)0xdf);

    private byte head;
    ByteType(byte _head)
    {
        head=_head;
    }
    public byte getHead()
    {
        return head;
    }
}
