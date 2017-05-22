package ywcai.ls.module.mouse.lsenum;

/**
 * Created by zmy_11 on 2016/12/21.
 */

public enum JsonHead {
    JSON_TYPE("type"),
    JSON_CONTENT("content");
    private String str;
    JsonHead(String _str)
    {
        str=_str;
    }
    public String getStr() {
        return str;
    }
}
