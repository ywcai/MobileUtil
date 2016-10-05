package ywcai.ls.inf;

import java.util.HashMap;
import java.util.List;

import ywcai.ls.bean.BsrLineObj;
import ywcai.ls.bean.WifiInfo;

/**
 * Created by zmy_11 on 2016/10/5.
 */
public interface WifiRefreshInf {
    public void ClearListInfoUI();
    public void SetListInfoTip(String tip);
    public void UpdateListInfoList(List<WifiInfo> wifiInfoList);
    public void UpdateChanelCount(int[] chanelCount);
    public void UpdateGraphic2d4G(HashMap<String, BsrLineObj> hashMap2d4G);
    public void UpdateGraphic5G(HashMap<String, BsrLineObj> hashMap5G);
    public void UpdateGraphicDbm(List<WifiInfo> wifiInfoList);

}
