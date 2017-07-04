package ywcai.ls.module.remote.model;

import java.util.Comparator;

import ywcai.ls.common.ComponentStatus;
import ywcai.ls.common.DeviceInfo;
import ywcai.ls.util.statics.ResultCode;

/**
 * Created by zmy_11 on 2017/6/29.
 */

public class SortByStatus implements Comparator<DeviceInfo> {
    @Override
    public int compare(DeviceInfo lhs, DeviceInfo rhs) {
        int sort,lIndex=0,rIndex=0;
        if(lhs.deviceId.equals(ComponentStatus.getInstance().deviceId))
        {
            return -1;
        }
        if(rhs.deviceId.equals(ComponentStatus.getInstance().deviceId))
        {
            return 1;
        }
        if(lhs.status.equals(ResultCode.device_status_busy))
        {
            lIndex=1;
        }
        if(lhs.status.equals(ResultCode.device_status_offline))
        {
            lIndex=2;
        }
        if(rhs.status.equals(ResultCode.device_status_busy))
        {
            rIndex=1;
        }
        if(rhs.status.equals(ResultCode.device_status_offline))
        {
            rIndex=2;
        }
        sort=rIndex-lIndex;
        return sort;
    }
}
