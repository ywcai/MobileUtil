package ywcai.ls.module.tools.sensor.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ywcai.ls.module.tools.sensor.model.SensorInfo;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;

/**
 * Created by zmy_11 on 2016/8/15.
 */
public class SensorAdapter  extends BaseAdapter{
    private List<SensorInfo> list;
    private Context context;
    private  LayoutInflater mInflate;
    public SensorAdapter(List<SensorInfo> list)
    {
        this.list = list;
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
        ViewTempSensor vh;
        if (convertView == null) {
            convertView = mInflate.inflate(R.layout.listview_sensor,null);
            vh = new ViewTempSensor();
            /**得到各个控件的对象*/
            vh.title = (TextView) convertView.findViewById(R.id.title_sensor);
            vh.info = (TextView) convertView.findViewById(R.id.info_sensor);
            vh.content=(LinearLayout)convertView.findViewById(R.id.context_sensor);
            vh.detail_data = (TextView) convertView.findViewById(R.id.data_sensor);
            vh.item=(LinearLayout)convertView.findViewById(R.id.list_item_sensor);
            convertView.setTag(vh);//绑定ViewHolder对象
        }
        else
        {
            vh = (ViewTempSensor)convertView.getTag();//取出ViewHolder对象
        }
        vh.title.setText(list.get(position).name_cn);
        vh.info.setText(list.get(position).name_native);
        if(list.get(position).isDetail)
        {
            if(vh.content.getVisibility()!=View.VISIBLE)
            {
                vh.content.setVisibility(View.VISIBLE);
            }
            vh.detail_data.setText(list.get(position).collect_data);
        }
        else
        {
            vh.content.setVisibility(View.GONE);
        }
        return convertView;
    }
}
