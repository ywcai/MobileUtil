package ywcai.ls.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import ywcai.ls.assist.ViewTempPingImg;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;

/**
 * Created by zmy_11 on 2016/8/15.
 */
public class LocalImgAdapter extends BaseAdapter{
    private List<File> list;
    private Context context;
    private  LayoutInflater mInflate;
    public LocalImgAdapter(List<File> imgList)
    {
        this.list = imgList;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewTempPingImg vh;
        if (convertView == null) {
            convertView = mInflate.inflate(R.layout.listview_local_img,null);
            vh = new ViewTempPingImg();
            vh.imgName = (TextView) convertView.findViewById(R.id.tv_local_img_path);
            vh.btnDelete= (TextView) convertView.findViewById(R.id.btn_del);
            convertView.setTag(vh);
        }
        else
        {
            vh = (ViewTempPingImg)convertView.getTag();
        }
        vh.imgName.setText(list.get(position).getName());
        vh.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).delete();
                list.remove(position);
                LocalImgAdapter.this.notifyDataSetChanged();
            }
        });
        return convertView;
    }

}
