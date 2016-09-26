package ywcai.ls.mobileutil.main.fragment.sub;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ywcai.ls.core.Wifi;
import ywcai.ls.inf.FragmentCallBack;
import ywcai.ls.mobileutil.R;

/**
 * Created by zmy_11 on 2016/9/22.
 */
@SuppressLint("ValidFragment")
public class WifiDbmRecordFragment extends Fragment {
    private Wifi wifi;
    public WifiDbmRecordFragment(Wifi pWifi) {
        wifi=pWifi;
    }
    public WifiDbmRecordFragment() {

    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View tab_view = inflater.inflate(R.layout.fragment_tab_wifi_record, container, false);
        wifi.setRecordView(tab_view);
        return tab_view ;
    }
}
