package ywcai.ls.module.mouse.presenter.inf;

import android.content.Intent;

/**
 * Created by zmy_11 on 2016/12/24.
 */

public interface ControlInf {
    void init();
    void clickMenuMouse();
    void clickMenuShadow();
    void clickMenuDisConnect();
    void requestShadowConn(int requestCode, int resultCode, Intent data);
    void moveDown() ;
    void moveUp() ;
    void leftDown()  ;
    void leftUp()  ;
    void rightDown()  ;
    void rightUp()  ;
    void onTouchUp()  ;
    void onTouchDown()  ;
    void clickExitMouse();
    void clickEsc();
    void closeActivity();
}
