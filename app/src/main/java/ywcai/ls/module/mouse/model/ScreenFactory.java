package ywcai.ls.module.mouse.model;

import android.os.Build;

import ywcai.ls.module.mouse.model.draw.CatchScreen;
import ywcai.ls.module.mouse.presenter.inf.CatchScreenInf;

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
