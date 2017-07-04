package ywcai.ls.common.em;

public enum MsgType {
    CheckPsw("CheckPsw"),
    BackResult("BackResult"),
    request_shadow("request_shadow"),
    response_shadow("response_shadow"),
    close_shadow("close_shadow"),
    UNKNOW("UNKNOW");
    private String str;
    MsgType(String _str)
    {
        str=_str;
    }
    public String getStr() {
        return str;
    }
}
