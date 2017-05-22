package ywcai.ls.module.remote.login.model;

/**
 * Created by zmy_11 on 2017/1/15.
 */

public enum OperationDeviceType {
    LOGIN(0),ADD(0),UPDATE(1),DEL(2),OUT(4);
    int id;
    OperationDeviceType(int i) {
        id=i;
    }
    public int getId()
    {
        return id;
    }
}
