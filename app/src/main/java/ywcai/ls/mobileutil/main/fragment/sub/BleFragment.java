package ywcai.ls.mobileutil.main.fragment.sub;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import ywcai.ls.core.Ble;
import ywcai.ls.mobileutil.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BleFragment extends Fragment {

    public BleFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View tab_view = inflater.inflate(R.layout.fragment_tab_ble, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            new Ble(tab_view,this.getContext());
        }
        else
        {
            TextView textView=(TextView)tab_view.findViewById(R.id.tv_ble_title);
            textView.setText("该系统版本(4.3及以下)不支持蓝牙4.0协议!");
        }
        return tab_view;
    }
}
