package ywcai.ls.mobileutil.main.fragment.sub;


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
import android.widget.Toast;

import com.baidu.mobstat.StatService;

import ywcai.ls.adapter.WifiPageAdapter;
import ywcai.ls.core.Wifi;
import ywcai.ls.inf.CallBackMainTitle;
import ywcai.ls.mobileutil.R;

public class WifiFragment extends Fragment{
    private  Wifi wifi;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View tab_view = inflater.inflate(R.layout.fragment_tab_wifi, container, false);
        final CallBackMainTitle fragmentCallBack = (CallBackMainTitle) this.getActivity();
        FragmentManager fm = this.getChildFragmentManager();
        WifiPageAdapter wifiPageAdapter=new WifiPageAdapter(fm);
        ViewPager wifiViewPager = (ViewPager) tab_view.findViewById(R.id.wifiViewPager);
        wifiViewPager.setAdapter(wifiPageAdapter);
        RelativeLayout relativeLayout = (RelativeLayout) tab_view.findViewById(R.id.tab_wifi);
        LinearLayout navImg = (LinearLayout) tab_view.findViewById(R.id.wifi_top_nav);
        relativeLayout.removeView(navImg);
        relativeLayout.addView(navImg, 1);
        final ImageView nav1 = (ImageView) tab_view.findViewById(R.id.wifiNav_1);
        final ImageView nav2 = (ImageView) tab_view.findViewById(R.id.wifiNav_2);
        final ImageView nav3 = (ImageView) tab_view.findViewById(R.id.wifiNav_3);
        final ImageView nav4 = (ImageView) tab_view.findViewById(R.id.wifiNav_4);
        wifiViewPager.setOffscreenPageLimit(3);
        wifiViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                switch (position) {
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
        wifi=new Wifi(fragmentCallBack,wifiPageAdapter,this.getContext());
        return tab_view;
    }

    @Override
    public void onDestroy() {
        wifi.BreakBackgroundTask();
        super.onDestroy();
    }
    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(),"Wifi");
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(this.getContext(),"Wifi");
    }
}
