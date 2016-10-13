package ywcai.ls.mobileutil.main.fragment.sub;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ywcai.ls.core.task.RefreshWifiDbm;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;



public class WifiDbmRecordFragment extends Fragment {

    public RefreshWifiDbm refreshWifiDbm=null;
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
        refreshWifiDbm=new RefreshWifiDbm(tab_view,this);
        return tab_view ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refreshWifiDbm=null;
    }
}
