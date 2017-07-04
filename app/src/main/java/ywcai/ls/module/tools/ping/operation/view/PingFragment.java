package ywcai.ls.module.tools.ping.operation.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;

import ywcai.ls.mobileutil.R;
import ywcai.ls.module.tools.ping.operation.presenter.Ping;

public class PingFragment extends Fragment {
    private Ping ping;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View tab_view = inflater.inflate(R.layout.fragment_tab_ping, container, false);
        ping =new Ping(tab_view,PingFragment.this);
        return tab_view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ping.pingParameter.isWorking=false;
        ping.breakBackgroundThread();
    }
    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(),"PingTest");
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(this.getContext(),"PingTest");
    }
}
