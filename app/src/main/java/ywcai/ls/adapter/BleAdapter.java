package ywcai.ls.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ywcai.ls.control.MyProgress;

import ywcai.ls.bean.BleInfo;
import ywcai.ls.assist.ViewTempBle;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;

/**
 * Created by zmy_11 on 2016/8/15.
 */
public class BleAdapter extends BaseAdapter{
    private List<BleInfo> list;
    private Context context;
    private  LayoutInflater mInflate;
    public BleAdapter(List<BleInfo> pList)
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
        ViewTempBle vh;
        if (convertView == null) {
            convertView = mInflate.inflate(R.layout.listview_ble,null);
            vh = new ViewTempBle();
            vh.tv_device=(TextView)convertView.findViewById(R.id.tv_ble_device);
            vh.tv_mac = (TextView) convertView.findViewById(R.id.tv_ble_mac);
            vh.pb_Dbm=(MyProgress)convertView.findViewById(R.id.pb_ble_Dbm);
            convertView.setTag(vh);
        }
        else
        {
            vh = (ViewTempBle)convertView.getTag();
        }

        vh.tv_device.setText(list.get(position).device+"");
        vh.tv_mac.setText(list.get(position).mac+"");
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

        return convertView;
    }

}
