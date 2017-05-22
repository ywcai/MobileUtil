package ywcai.ls.module.components.gps;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;

import ywcai.ls.mobileutil.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GpsFragment extends Fragment {

    public GpsFragment() {
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
        View tab_view = inflater.inflate(R.layout.fragment_tab_gps, container, false);

        Gps gps=new Gps(tab_view,this.getContext());
        return tab_view;
    }
    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(),"GPS");
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(this.getContext(),"GPS");
    }
}
