package ywcai.ls.module.tools.wifi.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ywcai.ls.module.tools.wifi.model.RefreshWifi5G;
import ywcai.ls.mobileutil.R;


public class WifiChanelAnalysis5GFragment extends Fragment {
    public RefreshWifi5G refreshWifi5G=null;

    public WifiChanelAnalysis5GFragment() {

    }
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View tab_view = inflater.inflate(R.layout.fragment_tab_wifi_analysis_5g, container, false);
        refreshWifi5G=new RefreshWifi5G(tab_view);
        return tab_view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refreshWifi5G=null;
    }
}
