package ywcai.ls.mobileutil.main.fragment.sub;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import ywcai.ls.core.Station;
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
}
