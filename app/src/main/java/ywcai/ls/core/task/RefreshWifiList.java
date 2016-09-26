package ywcai.ls.core.task;

import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import ywcai.ls.adapter.WifiAdapter;
import ywcai.ls.bean.WifiInfo;
import ywcai.ls.mobileutil.R;

/**
 * Created by zmy_11 on 2016/9/25.
 */
public class RefreshWifiList {
    private View tabView;
    private List<WifiInfo> wifiList;
    private WifiAdapter wifiAdapter;
    private TextView tv_wifiTip;

    public RefreshWifiList(View pView, List<WifiInfo> pList)
    {
        tabView=pView;
        wifiList=pList;
        wifiAdapter=new WifiAdapter(pList);
        InitView();
    }
    private void InitView()
    {
        tv_wifiTip = (TextView) tabView.findViewById(R.id.tv_wifiTip);
        ListViewCompat listView = (ListViewCompat) tabView.findViewById(R.id.now_wifiInfo);
        listView.setAdapter(wifiAdapter);
    }
    public void updateList()
    {
        wifiAdapter.notifyDataSetChanged();
    }
    public void setTextTip(String text)
    {
        tv_wifiTip.setText(text);
    }
}
