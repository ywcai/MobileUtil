package ywcai.ls.module.tools.wifi.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ywcai.ls.module.tools.wifi.model.RefreshWifiList;
import ywcai.ls.mobileutil.R;


public class WifiInfoListFragment extends Fragment {
    public RefreshWifiList refreshWifiList=null;
    public WifiInfoListFragment() {

    }
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View tab_view = inflater.inflate(R.layout.fragment_tab_wifi_all_info, container, false);
        refreshWifiList=new RefreshWifiList(tab_view);
        return tab_view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refreshWifiList=null;
    }
}
