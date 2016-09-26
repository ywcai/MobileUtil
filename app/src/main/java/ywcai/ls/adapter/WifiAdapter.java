package ywcai.ls.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import java.util.List;


import ywcai.ls.control.MyProgress;
import ywcai.ls.assist.ViewTempWifi;
import ywcai.ls.bean.WifiInfo;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;

/**
 * Created by zmy_11 on 2016/8/15.
 */
public class WifiAdapter extends BaseAdapter{
    private List<WifiInfo> list;
    private Context context;
    private  LayoutInflater mInflate;
    public WifiAdapter(List<WifiInfo> pList)
    {
        this.list = pList;
        this.context= MyApplication.getInstance().getApplicationContext();
        this.mInflate=LayoutInflater.from(context);
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
        ViewTempWifi vh;
        if (convertView == null) {
            convertView = mInflate.inflate(R.layout.listview_wifi,null);
            vh = new ViewTempWifi();
            vh.tv_sid = (TextView) convertView.findViewById(R.id.tv_sid);
            vh.tv_channel = (TextView) convertView.findViewById(R.id.tv_channel);
            vh.tv_device=(TextView)convertView.findViewById(R.id.tv_device);
            vh.tv_frequency = (TextView) convertView.findViewById(R.id.tv_frequency);
            vh.tv_keyType=(TextView)convertView.findViewById(R.id.tv_keyType);
            vh.tv_mac=(TextView)convertView.findViewById(R.id.tv_mac);
            vh.pb_Dbm=(MyProgress)convertView.findViewById(R.id.pb_Dbm);
            convertView.setTag(vh);
        }
        else
        {
            vh = (ViewTempWifi)convertView.getTag();
        }

        vh.tv_sid.setText(list.get(position).sid+"");
        vh.tv_channel.setText("信道: " + list.get(position).channel+"");
        vh.tv_device.setText(list.get(position).device+"");
        vh.tv_frequency.setText("频率: "+list.get(position).frequency+"MHZ");
        vh.tv_keyType.setText(list.get(position).keyType);
        vh.tv_mac.setText("["+list.get(position).mac+"]");
        vh.pb_Dbm.setDbm(list.get(position).dbm);
        int percent=0;
        if(list.get(position).dbm>-40)
        {
            percent=60;
        }
        if(list.get(position).dbm<=-40&&list.get(position).dbm>=-100)
        {
            percent=list.get(position).dbm+100;
        }
        vh.pb_Dbm.setFillPercent(percent);

        if(list.get(position).isConnWifi)
        {
            vh.tv_sid.setText("已连接 " + list.get(position).sid + "");
            vh.tv_frequency.setText("频率: " + list.get(position).frequency + "MHZ    速率: " + list.get(position).speed + "M");
            vh.tv_sid.setTextColor(0xFF30BF30);
            vh.tv_channel.setTextColor(0xFF30BF30);
            vh.tv_frequency.setTextColor(0xFF30BF30);
            vh.tv_keyType.setTextColor(0xFF30BF30);
            vh.tv_mac.setTextColor(0xFF30BF30);
            vh.tv_device.setTextColor(0xFF30BF30);
        }
        else
        {
            vh.tv_sid.setTextColor(0xFF6B6969);
            vh.tv_channel.setTextColor(0xFF6B6969);
            vh.tv_frequency.setTextColor(0xFF6B6969);
            vh.tv_keyType.setTextColor(0xFF6B6969);
            vh.tv_mac.setTextColor(0xFF6B6969);
            vh.tv_device.setTextColor(0xFF6B6969);

        }

        return convertView;
    }

}
