package ywcai.ls.module.components.wifi.presenter.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ywcai.ls.module.components.wifi.model.BsrLineObj;
import ywcai.ls.module.components.wifi.model.WifiInfo;
import ywcai.ls.module.components.wifi.model.RefreshWifi2d4G;
import ywcai.ls.module.components.wifi.model.RefreshWifi5G;
import ywcai.ls.module.components.wifi.model.RefreshWifiDbm;
import ywcai.ls.module.components.wifi.model.RefreshWifiList;
import ywcai.ls.module.components.wifi.presenter.inf.WifiRefreshInf;
import ywcai.ls.module.components.wifi.view.WifiInfoListFragment;
import ywcai.ls.module.components.wifi.view.WifiChanelAnalysis5GFragment;
import ywcai.ls.module.components.wifi.view.WifiChanelAnalysisFragment;
import ywcai.ls.module.components.wifi.view.WifiDbmRecordFragment;


public class WifiPageAdapter extends FragmentStatePagerAdapter  implements WifiRefreshInf  {
    public List<Fragment> pageList=new ArrayList<>();;
    public WifiPageAdapter(FragmentManager fm) {
        super(fm);
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
    @Override
    public void ClearListInfoUI() {

        RefreshWifiList refreshWifiList = ((WifiInfoListFragment) pageList.get(0)).refreshWifiList;
        if (refreshWifiList != null) {
            refreshWifiList.ClearList();
        }
    }

    @Override
    public void SetListInfoTip(String tip) {
        RefreshWifiList refreshWifiList = ((WifiInfoListFragment) pageList.get(0)).refreshWifiList;
        if (refreshWifiList != null) {
            refreshWifiList.SetTip(tip);
        }
    }

    @Override
    public void UpdateListInfoList(List<WifiInfo> wifiInfoList) {
        RefreshWifiList refreshWifiList = ((WifiInfoListFragment) pageList.get(0)).refreshWifiList;
        if (refreshWifiList != null) {
            refreshWifiList.UpdateList(wifiInfoList);
        }
    }

    @Override
    public void UpdateChanelCount(int[] chanelCount) {
        RefreshWifi2d4G refreshWifi2d4G = ((WifiChanelAnalysisFragment) pageList.get(1)).refreshWifi2d4G;
        RefreshWifi5G refreshWifi5G = ((WifiChanelAnalysis5GFragment) pageList.get(2)).refreshWifi5G;
        if (refreshWifi2d4G != null) {
            refreshWifi2d4G.updateNum(chanelCount);
        }
        if (refreshWifi5G != null) {
            refreshWifi5G.updateNum(chanelCount);
        }
    }

    @Override
    public Boolean UpdateGraphic2d4G(HashMap<String, BsrLineObj> hashMap2d4G) {
        RefreshWifi2d4G refreshWifi2d4G = ((WifiChanelAnalysisFragment) pageList.get(1)).refreshWifi2d4G;
        if (refreshWifi2d4G != null) {
            refreshWifi2d4G.updateGraphic(hashMap2d4G);
            return true;
        }
        return false;
    }

    @Override
    public Boolean UpdateGraphic5G(HashMap<String, BsrLineObj> hashMap5G) {
        RefreshWifi5G refreshWifi5G = ((WifiChanelAnalysis5GFragment) pageList.get(2)).refreshWifi5G;
        if (refreshWifi5G != null) {
            refreshWifi5G.updateGraphic(hashMap5G);
            return true;
        }
        return false;
    }

    @Override
    public void UpdateGraphicDbm(List<WifiInfo> wifiInfoList) {
        RefreshWifiDbm refreshWifiDbm = ((WifiDbmRecordFragment) pageList.get(3)).refreshWifiDbm;
        if (refreshWifiDbm != null) {
            refreshWifiDbm.updateDbmLine(wifiInfoList);
        }
    }

}
