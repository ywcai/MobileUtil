package ywcai.ls.mobileutil.main.fragment.sub;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.List;

import ywcai.ls.adapter.WifiPageAdapter;
import ywcai.ls.bean.BsrLineObj;
import ywcai.ls.bean.WifiInfo;
import ywcai.ls.core.Wifi;
import ywcai.ls.core.task.RefreshWifi2d4G;
import ywcai.ls.core.task.RefreshWifi5G;
import ywcai.ls.core.task.RefreshWifiDbm;
import ywcai.ls.core.task.RefreshWifiList;
import ywcai.ls.inf.CallBackMainTitle;
import ywcai.ls.inf.WifiRefreshInf;
import ywcai.ls.mobileutil.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class WifiFragment extends Fragment implements WifiRefreshInf {
    private CallBackMainTitle fragmentCallBack;
    private FragmentManager fm;
    private WifiPageAdapter wifiPageAdapter;
    @SuppressLint("ValidFragment")
    public WifiFragment(FragmentManager pFm,CallBackMainTitle pFragmentCallBack) {
        fragmentCallBack=pFragmentCallBack;
        fm=pFm;
    }
    public WifiFragment()
    {

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
        wifiPageAdapter=new WifiPageAdapter(fm);
        ViewPager wifiViewPager=(ViewPager)tab_view.findViewById(R.id.wifiViewPager);
        wifiViewPager.setAdapter(wifiPageAdapter);
        RelativeLayout relativeLayout=(RelativeLayout)tab_view.findViewById(R.id.tab_wifi);
        LinearLayout navImg=(LinearLayout)tab_view.findViewById(R.id.wifi_top_nav);
        relativeLayout.removeView(navImg);
        relativeLayout.addView(navImg,1);
        final ImageView nav1=(ImageView)tab_view.findViewById(R.id.wifiNav_1);
        final ImageView nav2=(ImageView)tab_view.findViewById(R.id.wifiNav_2);
        final ImageView nav3=(ImageView)tab_view.findViewById(R.id.wifiNav_3);
        final ImageView nav4=(ImageView)tab_view.findViewById(R.id.wifiNav_4);
        wifiViewPager.setCurrentItem(0);
        wifiViewPager.setOffscreenPageLimit(3);
        wifiViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
                    case 0:
                        nav1.setImageResource(R.drawable.wifi_nav_1_2);
                        nav2.setImageResource(R.drawable.wifi_nav_2_1);
                        nav3.setImageResource(R.drawable.wifi_nav_2_1);
                        nav4.setImageResource(R.drawable.wifi_nav_3_1);
                        break;
                    case 1:
                        nav1.setImageResource(R.drawable.wifi_nav_1_1);
                        nav2.setImageResource(R.drawable.wifi_nav_2_2);
                        nav3.setImageResource(R.drawable.wifi_nav_2_1);
                        nav4.setImageResource(R.drawable.wifi_nav_3_1);
                        break;
                    case 2:
                        nav1.setImageResource(R.drawable.wifi_nav_1_1);
                        nav2.setImageResource(R.drawable.wifi_nav_2_1);
                        nav3.setImageResource(R.drawable.wifi_nav_2_2);
                        nav4.setImageResource(R.drawable.wifi_nav_3_1);
                        break;
                    case 3:
                        nav1.setImageResource(R.drawable.wifi_nav_1_1);
                        nav2.setImageResource(R.drawable.wifi_nav_2_1);
                        nav3.setImageResource(R.drawable.wifi_nav_2_1);
                        nav4.setImageResource(R.drawable.wifi_nav_3_2);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        new Wifi(fragmentCallBack,this);
        return tab_view;
    }
    @Override
    public void ClearListInfoUI() {
        RefreshWifiList refreshWifiList=((WifiInfoListFragment)wifiPageAdapter.pageList.get(0)).refreshWifiList;
        if(refreshWifiList!=null)
        {
            refreshWifiList.ClearList();
        }
    }

    @Override
    public void SetListInfoTip(String tip) {
        RefreshWifiList refreshWifiList=((WifiInfoListFragment)wifiPageAdapter.pageList.get(0)).refreshWifiList;
        if(refreshWifiList!=null)
        {
            refreshWifiList.SetTip(tip);
        }
    }

    @Override
    public void UpdateListInfoList(List<WifiInfo> wifiInfoList) {
        RefreshWifiList refreshWifiList=((WifiInfoListFragment)wifiPageAdapter.pageList.get(0)).refreshWifiList;
        if(refreshWifiList!=null)
        {
            refreshWifiList.UpdateList(wifiInfoList);
        }
    }

    @Override
    public void UpdateChanelCount(int[] chanelCount) {
        RefreshWifi2d4G refreshWifi2d4G=((WifiChanelAnalysisFragment)wifiPageAdapter.pageList.get(1)).refreshWifi2d4G;
        RefreshWifi5G refreshWifi5G=((WifiChanelAnalysis5GFragment)wifiPageAdapter.pageList.get(2)).refreshWifi5G;
        if(refreshWifi2d4G!=null)
        {
            refreshWifi2d4G.updateNum(chanelCount);
        }
        if(refreshWifi5G!=null)
        {
            refreshWifi5G.updateNum(chanelCount);
        }
    }

    @Override
    public void UpdateGraphic2d4G(HashMap<String, BsrLineObj> hashMap2d4G) {

        RefreshWifi2d4G refreshWifi2d4G=((WifiChanelAnalysisFragment)wifiPageAdapter.pageList.get(1)).refreshWifi2d4G;
        if(refreshWifi2d4G!=null)
        {
            refreshWifi2d4G.updateGraphic(hashMap2d4G);
        }
    }

    @Override
    public void UpdateGraphic5G(HashMap<String, BsrLineObj> hashMap5G) {
        RefreshWifi5G refreshWifi5G=((WifiChanelAnalysis5GFragment)wifiPageAdapter.pageList.get(2)).refreshWifi5G;
        if(refreshWifi5G!=null)
        {
            refreshWifi5G.updateGraphic(hashMap5G);
        }
    }

    @Override
    public void UpdateGraphicDbm(List<WifiInfo> wifiInfoList) {
        RefreshWifiDbm refreshWifiDbm=((WifiDbmRecordFragment)wifiPageAdapter.pageList.get(3)).refreshWifiDbm;
        if(refreshWifiDbm!=null)
        {
            refreshWifiDbm.updateDbmLine(wifiInfoList);
        }
    }
}
