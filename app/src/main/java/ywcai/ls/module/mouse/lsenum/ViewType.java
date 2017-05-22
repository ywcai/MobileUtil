package ywcai.ls.module.mouse.lsenum;

/**
 * Created by zmy_11 on 2016/12/19.
 */

public enum ViewType {
    NONE(0),CONN(1),SHADOW(2),MOUSE(3);
    private int id;
    ViewType(int _id) {
        id=_id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
