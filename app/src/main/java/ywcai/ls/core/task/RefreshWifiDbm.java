package ywcai.ls.core.task;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.MotionEvent;
import android.view.View;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import ywcai.ls.adapter.LocalImgAdapter;
import ywcai.ls.bean.WifiInfo;
import ywcai.ls.core.draw.DrawImgDbm;
import ywcai.ls.inf.WifiDbmBgClearInf;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.main.activity.ImageActivity;
import ywcai.ls.mobileutil.main.fragment.sub.WifiDbmRecordFragment;
import ywcai.ls.util.MyConfig;
import ywcai.ls.util.MyUtil;

public class RefreshWifiDbm implements WifiDbmBgClearInf {
    private View tabView;
    private ImageView img, img_2, img_bg;
    public DrawImgDbm draw;
    public Bitmap temp = null;
    private HashMap<String, Integer> hashTemp = new HashMap<>();
    private int selectChanel = 1, lastChanel = 1;
    private ListViewCompat listView;
    private RelativeLayout rl_log, rl_chanel_box;
    private WifiDbmRecordFragment wifiFragment;
    private TextView tv_save, tv_set, tv_show, noData;
    private TextView lastSelect;


    public RefreshWifiDbm(View pView, WifiDbmRecordFragment pF) {
        tabView = pView;
        wifiFragment = pF;
        InitView();
    }

    private void InitView() {
        TextView t1 = (TextView) tabView.findViewById(R.id.radioButton);
        TextView t2 = (TextView) tabView.findViewById(R.id.radioButton2);
        TextView t3 = (TextView) tabView.findViewById(R.id.radioButton3);
        TextView t4 = (TextView) tabView.findViewById(R.id.radioButton4);
        TextView t5 = (TextView) tabView.findViewById(R.id.radioButton5);
        TextView t6 = (TextView) tabView.findViewById(R.id.radioButton6);
        TextView t7 = (TextView) tabView.findViewById(R.id.radioButton7);
        TextView t8 = (TextView) tabView.findViewById(R.id.radioButton8);
        TextView t9 = (TextView) tabView.findViewById(R.id.radioButton9);
        TextView t10 = (TextView) tabView.findViewById(R.id.radioButton10);
        TextView t11 = (TextView) tabView.findViewById(R.id.radioButton11);
        TextView t12 = (TextView) tabView.findViewById(R.id.radioButton12);
        TextView t13 = (TextView) tabView.findViewById(R.id.radioButton13);
        TextView t149 = (TextView) tabView.findViewById(R.id.radioButton149);
        TextView t153 = (TextView) tabView.findViewById(R.id.radioButton153);
        TextView t157 = (TextView) tabView.findViewById(R.id.radioButton157);
        TextView t161 = (TextView) tabView.findViewById(R.id.radioButton161);
        TextView t165 = (TextView) tabView.findViewById(R.id.radioButton165);
        final SelectChanel selectChanelEvent = new SelectChanel();
        t1.setOnClickListener(selectChanelEvent);
        t2.setOnClickListener(selectChanelEvent);
        t3.setOnClickListener(selectChanelEvent);
        t4.setOnClickListener(selectChanelEvent);
        t5.setOnClickListener(selectChanelEvent);
        t6.setOnClickListener(selectChanelEvent);
        t7.setOnClickListener(selectChanelEvent);
        t8.setOnClickListener(selectChanelEvent);
        t9.setOnClickListener(selectChanelEvent);
        t10.setOnClickListener(selectChanelEvent);
        t11.setOnClickListener(selectChanelEvent);
        t12.setOnClickListener(selectChanelEvent);
        t13.setOnClickListener(selectChanelEvent);
        t149.setOnClickListener(selectChanelEvent);
        t153.setOnClickListener(selectChanelEvent);
        t157.setOnClickListener(selectChanelEvent);
        t161.setOnClickListener(selectChanelEvent);
        t165.setOnClickListener(selectChanelEvent);
        lastSelect = t1;
        lastSelect.setTextColor(0xFF6DD900);
        rl_log = (RelativeLayout) tabView.findViewById(R.id.dbm_log_rl);
        rl_chanel_box = (RelativeLayout) tabView.findViewById(R.id.rl_chanel_box);

        MoveViewEvent mvEvent = new MoveViewEvent();
        rl_log.setOnTouchListener(mvEvent);
        rl_chanel_box.setOnTouchListener(mvEvent);

        TextView tv_ack = (TextView) tabView.findViewById(R.id.textView_ack);
        TextView tv_cancal = (TextView) tabView.findViewById(R.id.textView_cancal);
        tv_ack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectChanel == lastChanel) {
                    CloseMenuBox();
                } else {
                    ChangeChanel();
                    lastChanel = selectChanel;
                }
            }
        });
        tv_cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseMenuBox();
            }
        });
        noData = (TextView) tabView.findViewById(R.id.tv_has_no_log);
        noData.setVisibility(View.GONE);
        TextView out = (TextView) tabView.findViewById(R.id.tv_out);
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                CloseFileList();
            }
        });
        listView = (ListViewCompat) tabView.findViewById(R.id.dbm_record_list);
        rl_log.setVisibility(View.GONE);
        rl_chanel_box.setVisibility(View.GONE);
        img_bg = (ImageView) tabView.findViewById(R.id.record_bg);
        draw = new DrawImgDbm(800, 1000, this, lastChanel);
        img_bg.setImageBitmap(draw.bg);
        img = (ImageView) tabView.findViewById(R.id.record_img);
        img_2 = (ImageView) tabView.findViewById(R.id.record_img_2);
        tv_set = (TextView) tabView.findViewById(R.id.tv_setChanel);
        tv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowChangeBox();
            }
        });
        tv_save = (TextView) tabView.findViewById(R.id.tv_save_record);
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                draw.saveLog();
            }
        });
        tv_show = (TextView) tabView.findViewById(R.id.tv_show_record);
        tv_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDbmRecord();
            }
        });
    }

    private void CloseMenuBox() {
        TextView tv_ack = (TextView) tabView.findViewById(R.id.textView_ack);
        TextView tv_cancal = (TextView) tabView.findViewById(R.id.textView_cancal);
        tv_ack.setEnabled(false);
        tv_cancal.setEnabled(false);
        int closeH = rl_chanel_box.getMeasuredHeight();
        Animation animation1 = new TranslateAnimation(0, 0, 0, -closeH);
        animation1.setDuration(500);
        animation1.setInterpolator(new AccelerateDecelerateInterpolator());
        rl_chanel_box.startAnimation(animation1);
        setBtnVisible();
        rl_chanel_box.setVisibility(View.GONE);
    }

    private void ShowChangeBox() {
        setBtnGone();
        rl_chanel_box.setVisibility(View.VISIBLE);
        TextView tv_ack = (TextView) tabView.findViewById(R.id.textView_ack);
        TextView tv_cancal = (TextView) tabView.findViewById(R.id.textView_cancal);
        tv_ack.setEnabled(true);
        tv_cancal.setEnabled(true);
        int h = (rl_chanel_box.getMeasuredHeight() > 0) ? rl_chanel_box.getMeasuredHeight() : 750;
        Animation animation1 = new TranslateAnimation(0, 0, -h, 0);
        animation1.setDuration(500);
        animation1.setInterpolator(new AccelerateDecelerateInterpolator());
        rl_chanel_box.startAnimation(animation1);
    }

    private void ChangeChanel() {
        CloseMenuBox();
        draw.reset(selectChanel);
    }

    private void CloseFileList() {
        int closeH = rl_log.getMeasuredHeight();
        Animation animation1 = new TranslateAnimation(0, 0, 0, -closeH);
        animation1.setDuration(500);
        animation1.setInterpolator(new AccelerateDecelerateInterpolator());
        rl_log.startAnimation(animation1);
        setBtnVisible();
        rl_log.setVisibility(View.GONE);
    }

    private void setBtnGone() {
        TextView out = (TextView) tabView.findViewById(R.id.tv_out);
        out.setEnabled(true);
        tv_set.setVisibility(View.GONE);
        tv_show.setVisibility(View.GONE);
        tv_save.setVisibility(View.GONE);
    }

    private void setBtnVisible() {
        tv_set.setVisibility(View.VISIBLE);
        tv_show.setVisibility(View.VISIBLE);
        tv_save.setVisibility(View.VISIBLE);
    }

    public void updateDbmLine(List<WifiInfo> infoList) {

        HashMap<String, Integer> hashDrawInfo = new HashMap<>();
        for (int i = 0; i < infoList.size(); i++) {
            if (infoList.get(i).channel == selectChanel) {
                hashDrawInfo.put(infoList.get(i).sid + "(" + infoList.get(i).mac + ")", infoList.get(i).dbm);
            }
        }
        temp = DrawLine(hashDrawInfo);
    }

    private Bitmap DrawLine(HashMap hashDrawInfo) {
        int step = img_bg.getMeasuredWidth() / MyConfig.INT_WIFI_RECORD_X_MAX;

        img_2.setImageBitmap(temp);
        Animation animation1 = new TranslateAnimation(0, step, 0, 0);
        animation1.setDuration(500);
        animation1.setInterpolator(new AccelerateDecelerateInterpolator());
        animation1.setFillAfter(true);
        img_2.startAnimation(animation1);

        Bitmap record = draw.getRecordImg(hashTemp, hashDrawInfo);
        img.setImageBitmap(record);
        Animation animation2 = new TranslateAnimation(-step, 0, 0, 0);
        animation2.setDuration(500);
        animation2.setInterpolator(new AccelerateDecelerateInterpolator());
        img.startAnimation(animation2);
        return record;
    }

    @Override
    public void clearImg() {
        int step = img_bg.getMeasuredWidth() / MyConfig.INT_WIFI_RECORD_X_MAX;
        Animation animation1 = new TranslateAnimation(step, img_bg.getMeasuredWidth() + step, 0, 0);
        animation1.setDuration(1000);
        animation1.setFillAfter(true);
        img_2.startAnimation(animation1);

        img.setImageBitmap(null);
        hashTemp.clear();
        temp = null;
    }

    private void showDbmRecord() {
        rl_log.setVisibility(View.VISIBLE);
        setBtnGone();
        List<File> imgList = MyUtil.getImgList(MyConfig.STR_INTENT_LOG_PATH_DBM);
        int h;
        if (imgList == null) {
            h = 240;
            noData.setVisibility(View.VISIBLE);
        } else {
            h = 150 + imgList.size() * 90 + (imgList.size() - 1) * 3;
            noData.setVisibility(View.GONE);
            LocalImgAdapter localImgAdapter = new LocalImgAdapter(imgList);
            listView.setAdapter(localImgAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showImgActivity(MyConfig.STR_INTENT_LOG_PATH_DBM, position);
                }
            });
        }
        h = (rl_log.getMeasuredHeight() > 0) ? rl_log.getMeasuredHeight() : h;
        Animation animation1 = new TranslateAnimation(0, 0, -h, 0);
        animation1.setDuration(500);
        animation1.setInterpolator(new AccelerateDecelerateInterpolator());
        rl_log.startAnimation(animation1);
    }

    private void showImgActivity(String path, int pos) {
        Intent intent = new Intent();
        intent.setClass(wifiFragment.getActivity(), ImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(MyConfig.STR_INTENT_IMAGES_INDEX, pos);
        bundle.putString(MyConfig.STR_INTENT_IMAGES_PATH, path);
        intent.putExtras(bundle);
        wifiFragment.startActivity(intent);
    }

    class MoveViewEvent implements View.OnTouchListener {
        int downY = 0;
        int moveLastY = 0;
        int moveNowY = 0;
        int upY = 0;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    moveNowY = downY - (int) event.getRawY();
                    if (moveNowY > 0) {
                        Animation animation1 = new TranslateAnimation(0, 0, -moveLastY, -moveNowY);
                        animation1.setDuration(5);
                        animation1.setFillAfter(true);
                        v.startAnimation(animation1);
                        moveLastY = moveNowY;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    upY = (int) event.getRawY();
                    if (upY - downY < -300) {
                        Animation animation1 = new TranslateAnimation(0, 0, -moveLastY, -v.getMeasuredHeight());
                        animation1.setDuration(500);
                        animation1.setInterpolator(new AccelerateDecelerateInterpolator());
                        v.startAnimation(animation1);
                        v.setVisibility(View.GONE);
                        setBtnVisible();
                    } else {
                        Animation animation1 = new TranslateAnimation(0, 0, -moveLastY, 0);
                        animation1.setDuration(200);
                        animation1.setInterpolator(new AccelerateDecelerateInterpolator());
                        v.startAnimation(animation1);
                    }
                    downY = 0;
                    moveLastY = 0;
                    moveNowY = 0;
                    upY = 0;
                    break;
            }
            return true;
        }
    }

    class SelectChanel implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() != lastSelect.getId()) {
                ClearLastBtn();
                SetBtnState(v.getId());
            }
        }
    }

    private void ClearLastBtn() {
        lastSelect.setTextColor(0xFF777474);
    }

    private void SetBtnState(int mid) {
        lastSelect = (TextView) tabView.findViewById(mid);
        lastSelect.setTextColor(0xFF6DD900);
        switch (mid) {
            case R.id.radioButton:
                selectChanel = 1;
                break;
            case R.id.radioButton2:
                selectChanel = 2;
                break;
            case R.id.radioButton3:
                selectChanel = 3;
                break;
            case R.id.radioButton4:
                selectChanel = 4;
                break;
            case R.id.radioButton5:
                selectChanel = 5;
                break;
            case R.id.radioButton6:
                selectChanel = 6;
                break;
            case R.id.radioButton7:
                selectChanel = 7;
                break;
            case R.id.radioButton8:
                selectChanel = 8;
                break;
            case R.id.radioButton9:
                selectChanel = 9;
                break;
            case R.id.radioButton10:
                selectChanel = 10;
                break;
            case R.id.radioButton11:
                selectChanel = 11;
                break;
            case R.id.radioButton12:
                selectChanel = 12;
                break;
            case R.id.radioButton13:
                selectChanel = 13;
                break;
            case R.id.radioButton149:
                selectChanel = 149;
                break;
            case R.id.radioButton153:
                selectChanel = 153;
                break;
            case R.id.radioButton157:
                selectChanel = 157;
                break;
            case R.id.radioButton161:
                selectChanel = 161;
                break;
            case R.id.radioButton165:
                selectChanel = 165;
                break;
        }
    }


}
