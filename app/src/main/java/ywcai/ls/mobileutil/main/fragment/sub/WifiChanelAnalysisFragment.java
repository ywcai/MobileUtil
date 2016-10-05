package ywcai.ls.mobileutil.main.fragment.sub;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ywcai.ls.core.Wifi;
import ywcai.ls.core.task.RefreshWifi2d4G;
import ywcai.ls.mobileutil.R;

/**
 * Created by zmy_11 on 2016/9/22.
 */

public class WifiChanelAnalysisFragment extends Fragment {
    public RefreshWifi2d4G refreshWifi2d4G=null;
    public WifiChanelAnalysisFragment() {
    }
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View tab_view = inflater.inflate(R.layout.fragment_tab_wifi_analysis_2d4g, container, false);
        refreshWifi2d4G=new RefreshWifi2d4G(tab_view);
        return tab_view;
    }
}
