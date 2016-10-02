package ywcai.ls.mobileutil.main.fragment.sub;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ywcai.ls.core.Gps;
import ywcai.ls.core.GpsTest;
import ywcai.ls.mobileutil.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GpsTestFragment extends Fragment {

    public GpsTestFragment() {
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
        View tab_view = inflater.inflate(R.layout.fragment_tab_gps_test, container, false);

        final GpsTest gpsTest=new GpsTest(tab_view);
        Button btn_start=(Button)tab_view.findViewById(R.id.bt_mock_start);
        final TextView tv_show=(TextView)tab_view.findViewById(R.id.tv_gps_test_info);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTest.setGps();
                tv_show.setText("开启");
            }
        });
        return tab_view;
    }
}
