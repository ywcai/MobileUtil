package ywcai.ls.module.remote.login.model;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.module.mouse.model.ComponentStatus;

/**
 * Created by zmy_11 on 2017/4/16.
 */
public class SelectDevice {
    private List<DeviceInfo> temp;
    private List list=new ArrayList();
    public SelectDevice(List<DeviceInfo> _devices) {
        temp = _devices;
    }

    public List getFree() {
        for (DeviceInfo device : temp) {
            if (device.deviceStatus.equals("Free")) {
                list.add(device);
            }
        }
        return list;
    }

    public List getOnline() {
        for (DeviceInfo device : temp) {
            if (!device.deviceStatus.equals("Free")) {
                list.add(device);
            }
            if(device.deviceID==ComponentStatus.getInstance().selectDeviceID)
            {
                list.remove(device);
                device.deviceStatus="[本机-OnLine]";
                list.add(0,device);
            }
        }
        return list;
    }
}
