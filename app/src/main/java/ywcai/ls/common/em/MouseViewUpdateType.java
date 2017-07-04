package ywcai.ls.common.em;

/**
 * Created by zmy_11 on 2017/6/26.
 */

public enum MouseViewUpdateType {
    LOADING(0),
    LOAD_END(1),
    LOAD_NONE_VIEW(3),
    LOAD_CONN_VIEW(4),
    LOAD_SHADOW_VIEW(5),
    LOAD_MOUSE_VIEW(6),
    CLICK_LIST_VIEW(7),
    SHOW_TOAST(8),
    SHOW_PSW_BOX(9),
    SESSION_CLOSED(10),
    REPEAT_SUCCESS(11),
    ;
    MouseViewUpdateType(int i ) {
    }
}
