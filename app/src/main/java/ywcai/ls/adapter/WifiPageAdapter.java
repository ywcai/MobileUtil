package ywcai.ls.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.core.Wifi;
import ywcai.ls.inf.FragmentCallBack;
import ywcai.ls.mobileutil.main.fragment.sub.WifiAllInfoFragment;
import ywcai.ls.mobileutil.main.fragment.sub.WifiChanelAnalysis5GFragment;
import ywcai.ls.mobileutil.main.fragment.sub.WifiChanelAnalysisFragment;
import ywcai.ls.mobileutil.main.fragment.sub.WifiDbmRecordFragment;


/**
 * Created by zmy_11 on 2016/8/15.
 */
public class WifiPageAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;
    public WifiPageAdapter(FragmentManager fm,FragmentCallBack fragmentCallBack) {
        super(fm);
        list=new ArrayList<>();
        Wifi wifi=new Wifi(fragmentCallBack);
        WifiAllInfoFragment wifiAllInfoFragment=new WifiAllInfoFragment(wifi);
        WifiChanelAnalysisFragment wifiChanelAnalysisFragment=new WifiChanelAnalysisFragment(wifi);
        WifiDbmRecordFragment wifiDbmRecordFragment=new WifiDbmRecordFragment(wifi);
        WifiChanelAnalysis5GFragment wifiChanelAnalysis5GFragment=new WifiChanelAnalysis5GFragment(wifi);
        list.add(wifiAllInfoFragment);
        list.add(wifiChanelAnalysisFragment);
        list.add(wifiChanelAnalysis5GFragment);
        list.add(wifiDbmRecordFragment);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }
}
