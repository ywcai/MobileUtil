package ywcai.ls.module.components.wifi.model;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ywcai.ls.mobileutil.R;
import ywcai.ls.module.components.wifi.model.draw.CurveView;
import ywcai.ls.module.components.wifi.model.draw.DrawImgChanel;

/**
 * Created by zmy_11 on 2016/9/25.
 */
public class RefreshWifi5G {
    private View tabView;
    private TextView chanel_149, chanel_153, chanel_157, chanel_161, chanel_165;
    private TextView tv_5G;
    private RelativeLayout relativeLayout_5g;
    private DrawImgChanel draw5G;
    private ImageView img5G;
    private int[] lineColor={0xFFFD0303,0xff1a48be,0xff3693b7,0xff356244,0xffc959dc,0xff31beb2};
    public RefreshWifi5G(View pView)
    {
        tabView=pView;
        InitView();
    }
    private void InitView()
    {
        img5G = (ImageView) tabView.findViewById(R.id.img_5G);
        draw5G = new DrawImgChanel(850, 660);
        img5G.setImageBitmap(draw5G.bitmap);
        relativeLayout_5g = (RelativeLayout) tabView.findViewById(R.id.wifi_analysis_bg_5g);
        chanel_149 = (TextView) tabView.findViewById(R.id.chanel_num_149);
        chanel_153 = (TextView) tabView.findViewById(R.id.chanel_num_153);
        chanel_157 = (TextView) tabView.findViewById(R.id.chanel_num_157);
        chanel_161 = (TextView) tabView.findViewById(R.id.chanel_num_161);
        chanel_165 = (TextView) tabView.findViewById(R.id.chanel_num_165);
        tv_5G=(TextView)tabView.findViewById(R.id.tv_5G);
    }
    public  void updateGraphic(HashMap<String,BsrLineObj> hashMap)
    {
        Iterator iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            BsrLineObj bsrLineObj = (BsrLineObj) entry.getValue();
            if (bsrLineObj.isExist) {
                draw5GLine(bsrLineObj);
            }
            else {
                iterator.remove();
                removeLine(bsrLineObj);
            }
        }
        tv_5G.setText("5G 扫描到信号"+hashMap.size()+"个");
    }

    private void removeLine(BsrLineObj bsrLineObj) {
        Animation animation = new ScaleAnimation(1,1,1,0,Animation.RELATIVE_TO_SELF,1,Animation.RELATIVE_TO_SELF,1);
        animation.setDuration(1000);
        bsrLineObj.curveView.startAnimation(animation);
        relativeLayout_5g.removeView(bsrLineObj.curveView);
    }

    public void updateNum(int[] channelSum)
    {
        chanel_149.setText(channelSum[149] + "");
        chanel_153.setText(channelSum[153] + "");
        chanel_157.setText(channelSum[157] + "");
        chanel_161.setText(channelSum[161] + "");
        chanel_165.setText(channelSum[165] + "");
    }
    private void draw5GLine(BsrLineObj bsrLineObj)
    {
        CurveView cv = bsrLineObj.curveView;
        if(bsrLineObj.wifiInfo.isConnWifi)
        {
            cv.setBoldColor(lineColor[0]);
            cv.setFontColor(lineColor[0]);
            cv.setBoldWidth(5);
            cv.setViewName("已连接"+bsrLineObj.wifiInfo.sid);
        }
        else
        {
            try {
                cv.setBoldColor(lineColor[(bsrLineObj.wifiInfo.channel - 144) / 4]);
                cv.setFontColor(lineColor[bsrLineObj.wifiInfo.channel]);
            }
            catch (Exception e)
            {

            }
            cv.setBoldWidth(3);
            cv.setViewName(bsrLineObj.wifiInfo.sid);
        }
        RelativeLayout.LayoutParams cvLayout;
        if (bsrLineObj.wifiInfo.dbm > -50) {
            bsrLineObj.wifiInfo.dbm = -50;
        }
        if (bsrLineObj.wifiInfo.dbm < -110) {
            bsrLineObj.wifiInfo.dbm = -110;
        }
        int step_5G = tabView.findViewById(R.id.img_5G).getMeasuredWidth() / 5;
        int h_5G = (bsrLineObj.wifiInfo.dbm + 110) * (tabView.findViewById(R.id.img_5G).getMeasuredHeight() / 60);
        int w_5G = step_5G;
        cvLayout = new RelativeLayout.LayoutParams(w_5G, h_5G);
        cvLayout.addRule(RelativeLayout.ABOVE, R.id.img_line_5G);
        cvLayout.addRule(RelativeLayout.RIGHT_OF, R.id.left_line);
        cvLayout.setMargins((bsrLineObj.wifiInfo.channel-149)*step_5G/4 , 0, 0, 0);
        if (bsrLineObj.isNew) {
            relativeLayout_5g.addView(cv, cvLayout);
            int fromY=0;
            float toY=1;
            Animation animation = new ScaleAnimation(1,1,fromY,toY,Animation.RELATIVE_TO_SELF,1,Animation.RELATIVE_TO_SELF,1);
            animation.setDuration(1500);
            cv.startAnimation(animation);
        } else {
            float fromY=(float)cv.getMeasuredHeight()/(float) h_5G;
            float toY = 1;
            cv.setLayoutParams(cvLayout);
            Animation animation = new ScaleAnimation(1,1,fromY,toY,Animation.RELATIVE_TO_SELF,1,Animation.RELATIVE_TO_SELF,1);
            animation.setDuration(1500);
            cv.startAnimation(animation);
        }
        bsrLineObj.isExist = false;
        bsrLineObj.isNew=false;
    }
}
