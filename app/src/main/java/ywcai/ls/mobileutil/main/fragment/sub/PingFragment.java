package ywcai.ls.mobileutil.main.fragment.sub;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;

import ywcai.ls.core.Ping;
import ywcai.ls.mobileutil.R;

/**
 * A simple {@link Fragment} subclass.
 */
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
        StatService.onPageEnd(this.getContext(),"Ping");
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(this.getContext(),"Ping");
    }
}
