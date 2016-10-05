package ywcai.ls.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.inf.CallBackMainTitle;
import ywcai.ls.inf.WifiRefreshInf;
import ywcai.ls.mobileutil.main.fragment.sub.WifiInfoListFragment;
import ywcai.ls.mobileutil.main.fragment.sub.WifiChanelAnalysis5GFragment;
import ywcai.ls.mobileutil.main.fragment.sub.WifiChanelAnalysisFragment;
import ywcai.ls.mobileutil.main.fragment.sub.WifiDbmRecordFragment;


/**
 * Created by zmy_11 on 2016/8/15.
 */
public class WifiPageAdapter extends FragmentStatePagerAdapter {
    public List<Fragment> pageList;
//    private List<WifiRefreshInf>  infList;
    public WifiPageAdapter(FragmentManager fm) {
        super(fm);
//        list=new ArrayList<>();
//        Wifi wifi=new Wifi(fragmentCallBack,list);
//        WifiInfoListFragment wifiAllInfoFragment=new WifiInfoListFragment(wifi);
//        WifiChanelAnalysisFragment wifiChanelAnalysisFragment=new WifiChanelAnalysisFragment(wifi);
//        WifiDbmRecordFragment wifiDbmRecordFragment=new WifiDbmRecordFragment(wifi);
//        WifiChanelAnalysis5GFragment wifiChanelAnalysis5GFragment=new WifiChanelAnalysis5GFragment(wifi);
//        list.add(wifiAllInfoFragment);
//        list.add(wifiChanelAnalysisFragment);
//        list.add(wifiChanelAnalysis5GFragment);
//        list.add(wifiDbmRecordFragment);
        pageList=new ArrayList<>();
        WifiInfoListFragment wifiInfoListFragment=new WifiInfoListFragment();
        WifiChanelAnalysisFragment wifiChanelAnalysisFragment=new WifiChanelAnalysisFragment();
        WifiChanelAnalysis5GFragment wifiChanelAnalysis5GFragment=new WifiChanelAnalysis5GFragment();
        WifiDbmRecordFragment wifiDbmRecordFragment=new WifiDbmRecordFragment();
        pageList.add(wifiInfoListFragment);
        pageList.add(wifiChanelAnalysisFragment);
        pageList.add(wifiChanelAnalysis5GFragment);
        pageList.add(wifiDbmRecordFragment);
    }
    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return pageList.get(position);
    }
}
