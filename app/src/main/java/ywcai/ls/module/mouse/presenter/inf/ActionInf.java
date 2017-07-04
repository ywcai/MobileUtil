package ywcai.ls.module.mouse.presenter.inf;


import android.app.Activity;
import android.content.Intent;

public interface ActionInf {
     void requestConn(String ip);
     void checkPsw(String psw);
     void cancalConn();
     void clickShadowBtn(Activity activity);
     void clickMouseBtn();
     void startShadow(int requestCode, int resultCode, Intent data);
     void repeatConn();
     void repeatConnSuccess();
}
