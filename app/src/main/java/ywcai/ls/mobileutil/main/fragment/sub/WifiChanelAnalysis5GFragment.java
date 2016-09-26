package ywcai.ls.mobileutil.main.fragment.sub;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ywcai.ls.core.Wifi;
import ywcai.ls.mobileutil.R;

/**
 * Created by zmy_11 on 2016/9/22.
 */
@SuppressLint("ValidFragment")
public class WifiChanelAnalysis5GFragment extends Fragment {
    private Wifi wifi;
    public WifiChanelAnalysis5GFragment(Wifi pWifi) {
        wifi=pWifi;
    }
    public WifiChanelAnalysis5GFragment() {;
    }
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View tab_view = inflater.inflate(R.layout.fragment_tab_wifi_analysis_5g, container, false);
        wifi.setAnalysis5GView(tab_view);
        return tab_view;
    }
}
