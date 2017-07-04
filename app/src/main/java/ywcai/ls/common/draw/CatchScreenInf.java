package ywcai.ls.common.draw;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by zmy_11 on 2016/12/25.
 */

public interface CatchScreenInf {
    void requestPermission(Activity activity);
    void initScreen(Intent mResultData);
    String getScreenSize();
    byte[] getDeskByte();

}
