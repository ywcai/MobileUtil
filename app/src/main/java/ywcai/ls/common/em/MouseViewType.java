package ywcai.ls.common.em;

/**
 * Created by zmy_11 on 2016/12/19.
 */

public enum MouseViewType {
    NONE(0),CONN(1),SHADOW(2),MOUSE(3);
    private int id;
    MouseViewType(int _id) {
        id=_id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId()
    {
        return id;
    }

}
