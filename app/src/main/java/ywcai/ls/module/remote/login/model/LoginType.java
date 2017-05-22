package ywcai.ls.module.remote.login.model;

/**
 * Created by zmy_11 on 2017/1/15.
 */
public enum LoginType {
    QQ(0),MM(1),OTHER(2);
    private int type;
    LoginType(int i) {
        type=i;
    }
    public int getType() {
        return type;
    }
}
