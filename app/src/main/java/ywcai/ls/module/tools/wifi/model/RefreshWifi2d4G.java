package ywcai.ls.module.tools.wifi.model;

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
import ywcai.ls.module.tools.wifi.model.draw.CurveView;
import ywcai.ls.module.tools.wifi.model.draw.DrawImgChanel;

/**
 * Created by zmy_11 on 2016/9/25.
 */
public class RefreshWifi2d4G {
    private View tabView;
    private TextView chanel_1, chanel_2, chanel_3, chanel_4, chanel_5, chanel_6, chanel_7, chanel_8, chanel_9, chanel_10, chanel_11, chanel_12, chanel_13;
    private TextView tv_2d4G;
    private RelativeLayout relativeLayout;
    private DrawImgChanel draw2d4G;
    private ImageView img2d4G;
    private int[] lineColor={0xFFFD0303,0xff7b9dd4,0xff28c2ab,0xff2663cc,0xff895739,0xff10412c,0xff2ed62b,0xff2aa59f,0xffeb9bbd,
                                0xffb6b850,0xff8f7381,0xffb84575,0xff24dc21,0xffd024c2};
    public RefreshWifi2d4G(View pView)
    {
        tabView=pView;
        InitView();
    }
    private void InitView()
    {
        img2d4G = (ImageView) tabView.findViewById(R.id.img_2d4G);
        draw2d4G = new DrawImgChanel(850, 660);
        img2d4G.setImageBitmap(draw2d4G.bitmap);
        relativeLayout = (RelativeLayout) tabView.findViewById(R.id.wifi_analysis_bg);
        chanel_1 = (TextView) tabView.findViewById(R.id.chanel_num_1);
        chanel_2 = (TextView) tabView.findViewById(R.id.chanel_num_2);
        chanel_3 = (TextView) tabView.findViewById(R.id.chanel_num_3);
        chanel_4 = (TextView) tabView.findViewById(R.id.chanel_num_4);
        chanel_5 = (TextView) tabView.findViewById(R.id.chanel_num_5);
        chanel_6 = (TextView) tabView.findViewById(R.id.chanel_num_6);
        chanel_7 = (TextView) tabView.findViewById(R.id.chanel_num_7);
        chanel_8 = (TextView) tabView.findViewById(R.id.chanel_num_8);
        chanel_9 = (TextView) tabView.findViewById(R.id.chanel_num_9);
        chanel_10 = (TextView) tabView.findViewById(R.id.chanel_num_10);
        chanel_11 = (TextView) tabView.findViewById(R.id.chanel_num_11);
        chanel_12 = (TextView) tabView.findViewById(R.id.chanel_num_12);
        chanel_13 = (TextView) tabView.findViewById(R.id.chanel_num_13);
        tv_2d4G=(TextView) tabView.findViewById(R.id.tv_2d4g);
    }
    public void  updateGraphic(HashMap<String,BsrLineObj> hashMap)
    {
        Iterator iterator = hashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            BsrLineObj bsrLineObj = (BsrLineObj) entry.getValue();
            if (bsrLineObj.isExist) {
                draw2d4GLine(bsrLineObj);
            }
            else {
                iterator.remove();
                removeLine(bsrLineObj);
            }
        }
        tv_2d4G.setText("2.4G 扫描到信号"+hashMap.size()+"个");
    }

    private void removeLine(BsrLineObj bsrLineObj) {
        Animation animation = new ScaleAnimation(1,1,1,0,Animation.RELATIVE_TO_SELF,1,Animation.RELATIVE_TO_SELF,1);
        animation.setDuration(1000);
        bsrLineObj.curveView.startAnimation(animation);
        relativeLayout.removeView(bsrLineObj.curveView);
    }

    public void updateNum(int[] channelSum)
    {
        chanel_1.setText(channelSum[1] + "");
        chanel_2.setText(channelSum[2] + "");
        chanel_3.setText(channelSum[3] + "");
        chanel_4.setText(channelSum[4] + "");
        chanel_5.setText(channelSum[5] + "");
        chanel_6.setText(channelSum[6] + "");
        chanel_7.setText(channelSum[7] + "");
        chanel_8.setText(channelSum[8] + "");
        chanel_9.setText(channelSum[9] + "");
        chanel_10.setText(channelSum[10] + "");
        chanel_11.setText(channelSum[11] + "");
        chanel_12.setText(channelSum[12] + "");
        chanel_13.setText(channelSum[13] + "");
    }
    private void draw2d4GLine(BsrLineObj bsrLineObj)
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
            cv.setBoldColor(lineColor[bsrLineObj.wifiInfo.channel]);
            cv.setFontColor(lineColor[bsrLineObj.wifiInfo.channel]);
            cv.setBoldWidth(3);
            cv.setViewName(bsrLineObj.wifiInfo.sid);
        }
        RelativeLayout.LayoutParams cvLayout;
        bsrLineObj.wifiInfo.dbm=bsrLineObj.wifiInfo.dbm>-50?-50:bsrLineObj.wifiInfo.dbm;
        bsrLineObj.wifiInfo.dbm=bsrLineObj.wifiInfo.dbm<-110?-110:bsrLineObj.wifiInfo.dbm;
        int step_2d4G = tabView.findViewById(R.id.img_2d4G).getMeasuredWidth() / 17;
        int h_all=tabView.findViewById(R.id.img_2d4G).getMeasuredHeight();
        int h_2d4G = (bsrLineObj.wifiInfo.dbm + 110) * h_all / 60;
        int w_2D4G = step_2d4G * 4;
        cvLayout = new RelativeLayout.LayoutParams(w_2D4G, h_2d4G);
        cvLayout.addRule(RelativeLayout.ABOVE, R.id.img_line_2d4G);
        cvLayout.addRule(RelativeLayout.RIGHT_OF, R.id.left_line);
        int leftMargins=(2*bsrLineObj.wifiInfo.channel-1)*(tabView.findViewById(R.id.img_2d4G).getMeasuredWidth())/34;
        cvLayout.setMargins(leftMargins,0, 0, 0);
        if (bsrLineObj.isNew) {
            relativeLayout.addView(cv, cvLayout);
            int fromY=0;
            float toY=1;
            Animation animation = new ScaleAnimation(1,1,fromY,toY,Animation.RELATIVE_TO_SELF,1,Animation.RELATIVE_TO_SELF,1);
            animation.setDuration(1500);
            cv.startAnimation(animation);
        } else {
            float fromY=(float)cv.getMeasuredHeight()/(float) h_2d4G;
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
