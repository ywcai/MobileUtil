package ywcai.ls.inf;

import ywcai.ls.bean.LanInfo;

public interface CallBackLanScanResultInf {
     void UpdateIpList(LanInfo lanInfo);
     void CallBackCheckResult(Boolean isSuccess ,String identity);
}
