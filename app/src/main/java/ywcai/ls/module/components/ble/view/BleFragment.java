package ywcai.ls.module.components.ble.view;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.baidu.mobstat.StatService;

import ywcai.ls.module.components.ble.presenter.Ble;
import ywcai.ls.mobileutil.R;

public class BleFragment extends Fragment {



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
    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(),"BLE");
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(this.getContext(),"BLE");
    }
}
