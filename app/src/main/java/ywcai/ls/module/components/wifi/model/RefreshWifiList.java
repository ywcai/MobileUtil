package ywcai.ls.module.components.wifi.model;

import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.module.components.wifi.presenter.adapter.WifiAdapter;
import ywcai.ls.mobileutil.R;

/**
 * Created by zmy_11 on 2016/9/25.
 */
public class RefreshWifiList {
    private View tabView;
    private List<WifiInfo> wifiList;
    private WifiAdapter wifiAdapter;
    private TextView tv_wifiTip;

    public RefreshWifiList(View pView)
    {
        tabView=pView;
        wifiList=new ArrayList<>();
        wifiAdapter=new WifiAdapter(wifiList);
        InitView();
    }
    private void InitView()
    {
        tv_wifiTip = (TextView) tabView.findViewById(R.id.tv_wifiTip);
        ListViewCompat listView = (ListViewCompat) tabView.findViewById(R.id.now_wifiInfo);
        listView.setAdapter(wifiAdapter);
    }
    public void ClearList()
    {
        wifiList.clear();
        wifiAdapter.notifyDataSetChanged();
    }
    public void SetTip(String tip)
    {
        tv_wifiTip.setText(tip);
    }
    public void UpdateList(List<WifiInfo> pList)
    {
        wifiList.addAll(pList);
        wifiAdapter.notifyDataSetChanged();
    }
}
