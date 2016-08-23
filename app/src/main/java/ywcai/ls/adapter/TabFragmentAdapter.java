package ywcai.ls.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.ui.child.BleFragment;
import ywcai.ls.ui.child.GpsFragment;
import ywcai.ls.ui.child.PingFragment;
import ywcai.ls.ui.child.SensorFragment;
import ywcai.ls.ui.child.StationFragment;
import ywcai.ls.ui.child.WifiFragment;

/**
 * Created by zmy_11 on 2016/8/12.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {

    private String[] meTitle;
    private Context context;
    private List<Fragment> fragmentList=null;
    public TabFragmentAdapter(FragmentManager fm,String[] titles,Context pContext) {
        super(fm);
        meTitle=titles;
        this.context=pContext;
        fragmentList = new ArrayList<Fragment>();
        PingFragment pingFragment=new PingFragment();
        StationFragment stationFragment=new StationFragment();
        WifiFragment wifiFragment=new WifiFragment();
        SensorFragment sensorFragment=new SensorFragment();
        GpsFragment gpsFragment=new GpsFragment();
        //BleFragment bleFragment=new BleFragment();
        fragmentList.add(pingFragment);
        fragmentList.add(stationFragment);
        fragmentList.add(wifiFragment);
        fragmentList.add(sensorFragment);
        fragmentList.add(gpsFragment);
        //fragmentList.add(bleFragment);

    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
    @Override
    public int getCount() {
        return meTitle.length;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return meTitle[position];
    }
}
