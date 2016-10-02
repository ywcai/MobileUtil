package ywcai.ls.core.task;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ywcai.ls.bean.BsrLineObj;
import ywcai.ls.bean.WifiInfo;
import ywcai.ls.control.CurveView;
import ywcai.ls.inf.CallBackClearBg;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.util.MyConfig;

/**
 * Created by zmy_11 on 2016/9/25.
 */
public class RefreshWifiDbm implements CallBackClearBg {
    private View tabView;
    private ImageView img,img_2,img_bg;
    private DrawImgDbm draw;
    private Bitmap temp=null;
    private HashMap <String ,Integer> hashTemp;
    private int selectChanel=11;

    public RefreshWifiDbm(View pView)
    {
        tabView=pView;
        InitView();
    }
    private void InitView()
    {
        hashTemp=new HashMap<>();

        img_bg=(ImageView)tabView.findViewById(R.id.record_bg);
        draw=new DrawImgDbm(800,1000,this);
        img_bg.setImageBitmap(draw.bg);
        img=(ImageView)tabView.findViewById(R.id.record_img);
        img_2=(ImageView)tabView.findViewById(R.id.record_img_2);
        TextView tv_set=(TextView)tabView.findViewById(R.id.tv_setChanel);
        tv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeChanel(6);
            }
        });
    }
    public void updateDbmLine(List<WifiInfo> infoList)
    {
        HashMap <String ,Integer> hashDrawInfo=new HashMap<>();
        for (int i=0;i<infoList.size();i++)
        {
            if(infoList.get(i).channel==selectChanel)
            {
                    hashDrawInfo.put(infoList.get(i).sid+"("+infoList.get(i).mac+")", infoList.get(i).dbm);
            }
        }
        DrawLine(hashDrawInfo);
    }
    private void DrawLine(HashMap hashDrawInfo)
    {
        int step = img_bg.getMeasuredWidth() / MyConfig.INT_WIFI_RECORD_X_MAX;
        if(temp!=null)
        {
            img_2.setImageBitmap(temp);
            Animation animation = new TranslateAnimation(0,step,0,0);
            animation.setDuration(1000);
            animation.setFillAfter(true);
            img_2.startAnimation(animation);
        }
        Bitmap record=draw.getRecordImg(hashTemp,hashDrawInfo);
        img.setImageBitmap(record);
        Animation animation = new TranslateAnimation(-step,0,0,0);
        animation.setDuration(1000);
        img.startAnimation(animation);
        hashTemp=hashDrawInfo;
        temp=record;
    }

    @Override
    public void clearImg() {
        img_2.setImageBitmap(temp);
        Animation animation1 = new TranslateAnimation(0,img_bg.getMeasuredWidth(),0,0);
        animation1.setDuration(1000);
        animation1.setFillAfter(true);
        img_2.startAnimation(animation1);
        temp=null;
        img.setImageBitmap(null);
    }
    private void changeChanel(int chanel)
    {
        selectChanel=chanel;
        draw.reset(chanel);
    }

}
