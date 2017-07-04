package ywcai.ls.common.draw;

import android.os.Build;

/**
 * Created by zmy_11 on 2016/12/25.
 */

public class ScreenFactory {
    public CatchScreenInf getScreen() {
        CatchScreenInf catchScreenInf=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            catchScreenInf = new CatchScreen();
        }
        return catchScreenInf;
    }
}
