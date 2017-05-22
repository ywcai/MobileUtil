package ywcai.ls.module.remote.login.model;

/**
 * Created by zmy_11 on 2017/1/15.
 */

public enum DeviceType {
    MOBILE("MOBILE"),PC("PC");
    private String str;
    DeviceType(String mobile) {
        str=mobile;
    }
    public String getStr()
    {
        return str;
    }

}
