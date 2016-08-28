package ywcai.ls.ui.child;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ywcai.ls.core.Wifi;
import ywcai.ls.mobileutil.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WifiFragment extends Fragment {

    public WifiFragment() {
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
        View tab_view = inflater.inflate(R.layout.fragment_tab_wifi, container, false);
        Wifi wifi=new Wifi(tab_view);
        return tab_view;
    }
}
