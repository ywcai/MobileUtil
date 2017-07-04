package ywcai.ls.module.remote.model;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ywcai.ls.common.ComponentStatus;
import ywcai.ls.common.DeviceInfo;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.util.statics.ResultCode;

public class DeviceListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mInflate;
    private List<DeviceInfo> list;

    public DeviceListAdapter(List<DeviceInfo> list) {
        this.list = list;
        this.context= MyApplication.getInstance().getApplicationContext();
        this.mInflate=LayoutInflater.from(context);
    }
    public void sortList()
    {
        SortByStatus sortByStatus=new SortByStatus();
        Collections.sort(list,sortByStatus);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeviceListViewTemp deviceListViewTemp;
        if(convertView==null)
        {
            convertView=mInflate.inflate(R.layout.listview_remote_device,null);
            deviceListViewTemp=new DeviceListViewTemp();
            deviceListViewTemp.deviceName_TV=(TextView)convertView.findViewById(R.id.device_name);
            deviceListViewTemp.deviceStatus_TV=(TextView)convertView.findViewById(R.id.device_status);
            deviceListViewTemp.deviceCode_TV=(TextView)convertView.findViewById(R.id.device_code);
            deviceListViewTemp.deviceId_TV=(TextView)convertView.findViewById(R.id.device_id);
            deviceListViewTemp.deviceOwner_TV=(TextView)convertView.findViewById(R.id.device_owner);
            deviceListViewTemp.deviceMode_img=(CircleImageView)convertView.findViewById(R.id.device_mode);
            convertView.setTag(deviceListViewTemp);
        }
        else
        {
            deviceListViewTemp= (DeviceListViewTemp) convertView.getTag();
        }
        DeviceInfo deviceInfo=list.get(position);
        deviceListViewTemp.deviceName_TV.setText(deviceInfo.deviceName);
        deviceListViewTemp.deviceStatus_TV.setText(deviceInfo.status);
        deviceListViewTemp.deviceCode_TV.setText(deviceInfo.accessCode);
        deviceListViewTemp.deviceId_TV.setText(deviceInfo.deviceId);
        if(position==0) {
            deviceListViewTemp.deviceOwner_TV.setText("本地设备");
        }
        else {
            if(deviceInfo.userId.equals(ComponentStatus.getInstance().qqUserId))
            {
                deviceListViewTemp.deviceOwner_TV.setText("个人设备");
            }
            else
            {
                deviceListViewTemp.deviceOwner_TV.setText("临时设备");
            }
        }
        if(deviceInfo.deviceMode.equals(ResultCode.device_mode_pc))
        {
        deviceListViewTemp.deviceMode_img.setImageDrawable(ContextCompat.getDrawable(MyApplication.getInstance().getApplicationContext(),R.drawable.log)  );
        }
        if(deviceInfo.deviceMode.equals(ResultCode.device_mode_mobile))
        {
            deviceListViewTemp.deviceMode_img.setImageDrawable(ContextCompat.getDrawable(MyApplication.getInstance().getApplicationContext(),R.drawable.qq_login) );
        }
        if(deviceInfo.status.equals(ResultCode.device_status_online))
        {
            deviceListViewTemp.deviceStatus_TV.setTextColor(Color.GREEN);
        }
        if(deviceInfo.status.equals(ResultCode.device_status_busy))
        {
            deviceListViewTemp.deviceStatus_TV.setTextColor(Color.RED);
        }
        if(deviceInfo.status.equals(ResultCode.device_status_offline))
        {
            deviceListViewTemp.deviceStatus_TV.setTextColor(Color.DKGRAY);
        }
        return convertView;
    }
}
