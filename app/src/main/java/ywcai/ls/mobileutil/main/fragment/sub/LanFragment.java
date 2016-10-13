package ywcai.ls.mobileutil.main.fragment.sub;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ywcai.ls.bean.LanInfo;
import ywcai.ls.control.MyPullListView;
import ywcai.ls.control.MyPullViewCallBack;
import ywcai.ls.core.task.ScanLan;
import ywcai.ls.core.thread.MyThreadFactory;
import ywcai.ls.inf.CallBackLanScanResultInf;
import ywcai.ls.mobileutil.R;
import ywcai.ls.util.MyConfig;
import ywcai.ls.util.MyUtil;


public class LanFragment extends Fragment implements CallBackLanScanResultInf {

    private View tabView;
    private MyHandler myHandler;
    private int scanNum = 0;
    private List<HashMap<String, String>> ipList;
    private ProgressBar lanLoader;
    private SimpleAdapter simpleAdapter;
    private RelativeLayout rlHead;
    private TextView tv_tip;
    private MyPullListView lanListView;
    private int loaderHeight = 150;
    private int maxPullHeight = 3 * 150;
    private final Object lock = new Object();
    private String ipRecord, userInPut = "0.0.0";
    private String localIp, localMac;
    private ExecutorService executorService;
    private boolean isAuto = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tabView = inflater.inflate(R.layout.fragment_tab_lan, container, false);
        InitView();
        InitAnimation();
        ScanStart();
        return tabView;
    }

    private void LoadNetSub() {
        ipRecord = "0.0.0";
        localMac = "null";
        InitLocalIp();
        InitLocalMac();
        InitLocalInfo();
    }

    private void InitLocalIp() {
        try {
            WifiManager wifiMg = (WifiManager) this.getContext().getSystemService(Context.WIFI_SERVICE);
            android.net.wifi.WifiInfo wifiInfo = wifiMg.getConnectionInfo();
            String ip = MyUtil.ConvertIpToStr(wifiInfo.getIpAddress());
            localIp = ip;
            if (localIp.equals("0.0.0.0")) {
                ReadRecord();
            } else {
                ipRecord = localIp.substring(0, localIp.lastIndexOf("."));
            }
        } catch (Exception e) {
            ReadRecord();
        }
    }

    private void InitLocalMac() {
        File arpFile = new File(MyConfig.STR_LOCAL_MAC_FILE);
        InputStream inStream;
        try {
            inStream = new FileInputStream(arpFile);
            InputStreamReader inStreamReader = new InputStreamReader(inStream);
            BufferedReader buffReader = new BufferedReader(inStreamReader);
            localMac = buffReader.readLine();
            inStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitLocalInfo() {
        HashMap hs = new HashMap();
        hs.put("lanIp", localIp);
        hs.put("lanMac", localMac);
        hs.put("lanDevice", "[本机]");
        ipList.add(hs);
        simpleAdapter.notifyDataSetChanged();
    }

    private void ReadRecord() {
        File arpFile = new File(MyConfig.STR_ARP_FILE_PATH);
        InputStream inStream;
        try {
            inStream = new FileInputStream(arpFile);
            InputStreamReader inStreamReader = new InputStreamReader(inStream);
            BufferedReader buffReader = new BufferedReader(inStreamReader);
            String temp;
            buffReader.readLine();
            if ((temp = buffReader.readLine()) != null) {
                temp = temp.replaceAll("\\s+", " ");
                String[] result = temp.split(" ");
                ipRecord = result[0].substring(0, result[0].lastIndexOf("."));
                localIp = ipRecord + ".1";
            }
            inStream.close();
        } catch (Exception e) {
        }
    }

    private void InitView() {
        myHandler = new MyHandler();
        ipList = new ArrayList<>();
        rlHead = (RelativeLayout) tabView.findViewById(R.id.rl_head);
        lanLoader = (ProgressBar) tabView.findViewById(R.id.pb_lan);
        tv_tip = (TextView) tabView.findViewById(R.id.tv_load_tip);
        simpleAdapter = new SimpleAdapter(this.getContext(),
                ipList,
                R.layout.listview_lan, new String[]{"lanIp", "lanMac", "lanDevice"},
                new int[]{R.id.lan_ip, R.id.lan_mac, R.id.lan_device});
        lanListView = (MyPullListView) tabView.findViewById(R.id.list_lan);
        ListViewCallBack listViewCallBack = new ListViewCallBack();
        lanListView.SetCallBack(listViewCallBack);
        lanListView.setAdapter(simpleAdapter);
        lanListView.h = loaderHeight;

        TextView tv_config = (TextView) tabView.findViewById(R.id.tv_cf);
        TextView tv_stop = (TextView) tabView.findViewById(R.id.tv_stop);
        TextView tv_set = (TextView) tabView.findViewById(R.id.tv_set);
        TextView tv_cancal = (TextView) tabView.findViewById(R.id.tv_cancal);

        tv_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowConfig();
            }
        });
        tv_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopNow();
            }
        });
        tv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetConfig();
            }
        });
        tv_cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAuto();
            }
        });
    }


    public void InitAnimation() {
        lanListView.isLoading = true;
        Animation animation = new TranslateAnimation(0, 0, -loaderHeight, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        lanListView.startAnimation(animation);

        Animation animation2 = new TranslateAnimation(0, 0, -loaderHeight, 0);
        Animation animation3 = new AlphaAnimation(0f, 1.0f);
        AnimationSet as = new AnimationSet(true);
        as.addAnimation(animation2);
        as.addAnimation(animation3);
        as.setDuration(1000);
        as.setInterpolator(new AccelerateDecelerateInterpolator());
        rlHead.startAnimation(as);
    }

    private void BreakBackgroundThread() {
        try {
            executorService.shutdownNow();
        } catch (Exception e) {
        }
        scanNum=254;
    }

    private void ScanStart() {
        TextView tv_config = (TextView) tabView.findViewById(R.id.tv_cf);
        TextView tv_stop = (TextView) tabView.findViewById(R.id.tv_stop);
        tv_config.setVisibility(View.GONE);
        tv_stop.setVisibility(View.VISIBLE);
        HideConfig();
        ipList.clear();
        simpleAdapter.notifyDataSetChanged();
        LoadNetSub();
        scanNum = 0;
        MyThreadFactory myThreadFactory = new MyThreadFactory();
        executorService = Executors.newFixedThreadPool(MyConfig.INT_SCAN_LAN_THREAD_COUNT, myThreadFactory);
        String scanIp = ipRecord;
        if (!isAuto) {
            scanIp = userInPut;
        }
        for (int i = 1; i <= 254; i++) {
            ScanLan scanLan = new ScanLan(scanIp + "." + i, this);
            executorService.execute(scanLan);
        }
    }

    private void ScanEnd() {
        TextView tv_config = (TextView) tabView.findViewById(R.id.tv_cf);
        TextView tv_stop = (TextView) tabView.findViewById(R.id.tv_stop);
        tv_config.setVisibility(View.VISIBLE);
        tv_stop.setVisibility(View.GONE);
        lanListView.isLoading = false;
        lanLoader.setVisibility(View.INVISIBLE);
        tv_tip.setText("↓ 下拉刷新");
        rlHead.setAlpha(1);
        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                loaderHeight);
        lay.setMargins(0, -loaderHeight, 0, 0);
        rlHead.setLayoutParams(lay);
    }

    private void ScanEndAnimation() {
        Animation animation1 = new AlphaAnimation(1.0f, 0f);
        Animation animation2 = new TranslateAnimation(0, 0, loaderHeight, 0);
        AnimationSet as = new AnimationSet(true);
        as.addAnimation(animation1);
        as.addAnimation(animation2);
        as.setDuration(1000);
        as.setInterpolator(new AccelerateDecelerateInterpolator());
        rlHead.startAnimation(as);
        Animation animation = new TranslateAnimation(0, 0, loaderHeight, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        lanListView.startAnimation(animation);
    }


    @Override
    public void UpdateIpList(LanInfo pLanInfo, Boolean isExist) {
        Message mes = new Message();
        mes.what = isExist ? 1 : 0;
        mes.obj = pLanInfo;
        myHandler.sendMessage(mes);
    }

    private void StopNow() {
        BreakBackgroundThread();
    }

    private void SetAuto() {
        isAuto = true;
        HideConfig();
    }

    private void SetConfig() {
        TextView tv_ip1 = (TextView) tabView.findViewById(R.id.tv_ip1);
        TextView tv_ip2 = (TextView) tabView.findViewById(R.id.tv_ip2);
        TextView tv_ip3 = (TextView) tabView.findViewById(R.id.tv_ip3);
        //判断TEXT值
        if(Integer.parseInt(tv_ip1.getText().toString())>254||Integer.parseInt(tv_ip2.getText().toString())>254||Integer.parseInt(tv_ip3.getText().toString())>254)
        {
            Toast.makeText(this.getContext(), "IP输入有误，设置失败!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(tv_ip1.equals("")||tv_ip2.equals("")||tv_ip2.equals(""))
        {
            Toast.makeText(this.getContext(), "IP输入有误，设置失败!", Toast.LENGTH_SHORT).show();
            return;
        }
        isAuto = false;
        userInPut = tv_ip1.getText().toString() + "." + tv_ip2.getText().toString() + "." + tv_ip3.getText().toString();
        HideConfig();
    }

    private void ShowConfig() {
        RelativeLayout rl_config = (RelativeLayout) tabView.findViewById(R.id.rl_show_config);
        FrameLayout fl = (FrameLayout) tabView.findViewById(R.id.frame_mask);
        rl_config.setVisibility(View.VISIBLE);
        fl.setVisibility(View.VISIBLE);
    }

    private void HideConfig() {
        RelativeLayout rl_config = (RelativeLayout) tabView.findViewById(R.id.rl_show_config);
        FrameLayout fl = (FrameLayout) tabView.findViewById(R.id.frame_mask);
        rl_config.setVisibility(View.GONE);
        fl.setVisibility(View.GONE);
    }

    class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LanInfo lanInfo = (LanInfo) msg.obj;
            switch (msg.what) {
                case 1:
                    HashMap<String, String> hs = new HashMap<>();
                    if (!lanInfo.lanIp.equals(localIp)) {
                        hs.put("lanIp", lanInfo.lanIp);
                        hs.put("lanMac", lanInfo.lanMac);
                        hs.put("lanDevice", "");
                        ipList.add(hs);
                    }
                    simpleAdapter.notifyDataSetChanged();
                    break;
            }
            synchronized (lock) {
                if(scanNum<254)
                scanNum++;
            }
            int per = scanNum * 100 / 254;
            lanLoader.setVisibility(View.VISIBLE);
            lanLoader.setProgress(per);
            tv_tip.setText(per + "%");
            if (per == 100) {
                ScanEnd();
                ScanEndAnimation();
            }
        }
    }

    class ListViewCallBack implements MyPullViewCallBack {

        @Override
        public void PullView(int lastY, int nowY) {
            if (nowY < 0) {
                nowY = 0;
            }
            lastY = lastY < 0 ? 0 : lastY;
            nowY = nowY < 0 ? 0 : nowY;
            lastY = lastY > maxPullHeight ? maxPullHeight : lastY;
            nowY = nowY > maxPullHeight ? maxPullHeight : nowY;

            if (nowY >= 2 * loaderHeight) {
                tv_tip.setText("↑ 释放立即刷新");
            } else {
                tv_tip.setText("↓ 下拉刷新");
            }
            float lastAlpha = (float) lastY / (float) loaderHeight;
            float nowAlpha = (float) nowY / (float) loaderHeight;
            Animation animation1 = new AlphaAnimation(lastAlpha, nowAlpha);
            Animation animation2 = new TranslateAnimation(0, 0, lastY, nowY);
            AnimationSet as = new AnimationSet(true);
            as.addAnimation(animation1);
            as.addAnimation(animation2);
            as.setDuration(10);
            as.setFillAfter(true);
            rlHead.startAnimation(as);

            Animation animation3 = new TranslateAnimation(0, 0, lastY, nowY);
            animation3.setDuration(10);
            animation3.setFillAfter(true);
            lanListView.startAnimation(animation3);
        }

        @Override
        public void UpView(int nowY) {
            nowY = nowY > maxPullHeight ? maxPullHeight : nowY;
            nowY = nowY < 0 ? 0 : nowY;
            if (nowY >= 2 * loaderHeight) {
                LoadData(nowY);
            } else {
                BackBasePoint(nowY);
            }
        }


        private void LoadData(int nowY) {
            lanListView.isLoading = true;
            tv_tip.setText("开始刷新...");
            rlHead.setAlpha(1);
            RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    loaderHeight);
            lay.setMargins(0, 0, 0, 0);
            rlHead.setLayoutParams(lay);
            Animation animation1 = new AlphaAnimation(1, 1);
            Animation animation2 = new TranslateAnimation(0, 0, nowY - loaderHeight, 0);
            AnimationSet as = new AnimationSet(true);
            as.addAnimation(animation1);
            as.addAnimation(animation2);
            as.setInterpolator(new AccelerateDecelerateInterpolator());
            as.setDuration(500);
            rlHead.startAnimation(as);

            Animation animation3 = new TranslateAnimation(0, 0, nowY - loaderHeight, 0);
            as.setInterpolator(new AccelerateDecelerateInterpolator());
            animation3.setDuration(500);
            lanListView.startAnimation(animation3);
            ScanStart();
        }

        private void BackBasePoint(int nowY) {
            tv_tip.setText("↓ 下拉刷新");
            float nowAlpha = (float) nowY / (float) loaderHeight;
            Animation animation1 = new AlphaAnimation(nowAlpha, 0);
            Animation animation2 = new TranslateAnimation(0, 0, nowY, 0);
            AnimationSet as = new AnimationSet(true);
            as.addAnimation(animation1);
            as.addAnimation(animation2);
            as.setDuration(500);
            as.setInterpolator(new AccelerateDecelerateInterpolator());
            rlHead.startAnimation(as);
            Animation animation3 = new TranslateAnimation(0, 0, nowY, 0);
            animation3.setDuration(500);
            as.setInterpolator(new AccelerateDecelerateInterpolator());
            lanListView.startAnimation(animation3);
        }
    }

    @Override
    public void onDestroy() {
        BreakBackgroundThread();
        super.onDestroy();
    }
}
