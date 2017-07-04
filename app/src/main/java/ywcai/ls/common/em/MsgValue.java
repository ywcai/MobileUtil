package ywcai.ls.common.em;

public enum MsgValue {
    SUCCESS("SUCCESS"),
    FAIL("FAIL"),
    SESSION_OPEN_START("SESSION_OPEN_START"),
    SESSION_OPEN_SUCCESS("SESSION_OPEN_SUCCESS"),
    SESSION_OPEN_FAIL("SESSION_OPEN_FAIL"),
    SESSION_CLOSE("SESSION_CLOSE"),
    SESSION_CHECKING("SESSION_CHECKING"),
    WAIT_TIME_OUT("WAIT_TIME_OUT"),
    True("True"),
    False("False"),
    CLOSE("CLOSE"),
    REPEAT_START("REPEAT_START"),
    REPEAT_FAIL("REPEAT_FAIL"),
    REPEAT_SUCCESS("REPEAT_SUCCESS"),
    REPEAT_STOP("REPEAT_STOP"),
    UNKNOW("UNKNOW");
    private String str;
    MsgValue(String _str) {
        str=_str;
    }

    public String getStr() {
        return str;
    }
}
