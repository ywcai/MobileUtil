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

import ywcai.ls.adapter.WifiPageAdapter;
import ywcai.ls.inf.FragmentCallBack;
import ywcai.ls.mobileutil.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class WifiFragment extends Fragment {
    private FragmentCallBack fragmentCallBack;
    private FragmentManager fm;
    @SuppressLint("ValidFragment")
    public WifiFragment(FragmentManager pFm,FragmentCallBack pFragmentCallBack) {
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
        WifiPageAdapter wifiPageAdapter=new WifiPageAdapter(fm,fragmentCallBack);
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
        return tab_view;
    }
}
