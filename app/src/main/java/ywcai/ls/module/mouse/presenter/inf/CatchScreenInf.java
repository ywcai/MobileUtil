package ywcai.ls.module.mouse.presenter.inf;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by zmy_11 on 2016/12/25.
 */

public interface CatchScreenInf {
    void requestPermission(Activity activity);
    void initScreen(int mResultCode, Intent mResultData);
    String getScreenSize();
    byte[] getDeskByte();

}
