package ywcai.ls.module.mouse.presenter.inf;

import android.app.Activity;
import android.content.Context;

import ywcai.ls.module.mouse.lsenum.ViewType;

/**
 * Created by zmy_11 on 2016/12/24.
 */

public interface UpdateActivityInf {
    void disconnectSession();
    void changeViewType(ViewType viewType);
    Context getContext();
    void loadShadowMode();
    Activity getActivity();
    void showTip(String tip);


}
