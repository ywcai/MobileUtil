package ywcai.ls.mobileutil.main.fragment.sub;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ywcai.ls.core.Wifi;
import ywcai.ls.core.task.RefreshWifiList;
import ywcai.ls.mobileutil.R;

/**
 * Created by zmy_11 on 2016/9/22.
 */
@SuppressLint("ValidFragment")
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




}
