package ywcai.ls.module.mouse.lsenum;

/**
 * Created by zmy_11 on 2016/12/19.
 */

public enum OnlineType {
    offline(0),loginIng(1) ,online(2),link(3),linkIng(4);
    private int id;
    OnlineType(int _id) {
        id=_id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
