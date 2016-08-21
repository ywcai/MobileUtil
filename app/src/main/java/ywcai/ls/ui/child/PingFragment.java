package ywcai.ls.ui.child;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ywcai.ls.core.Ping;
import ywcai.ls.core.Sensor;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PingFragment extends Fragment {

    private Ping ping;
    public PingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View tab_view = inflater.inflate(R.layout.fragment_tab_ping, container, false);
        ping =new Ping(tab_view);
        return tab_view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        super.onDestroy();
        //ping.pingParameter.isWorking=false;
        ping=null;
    }
}
