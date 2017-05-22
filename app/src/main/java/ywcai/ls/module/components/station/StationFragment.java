package ywcai.ls.module.components.station;


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
public class StationFragment extends Fragment {

    public StationFragment() {
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
        View tab_view = inflater.inflate(R.layout.fragment_tab_station, container, false);
        Station station=new Station(tab_view,this.getContext());
        return tab_view;
    }
    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(),"Station");
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(this.getContext(),"Station");
    }
}
