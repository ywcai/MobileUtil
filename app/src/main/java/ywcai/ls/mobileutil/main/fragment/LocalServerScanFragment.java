package ywcai.ls.mobileutil.main.fragment;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

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

import ywcai.ls.bean.CheckResult;
import ywcai.ls.bean.LanInfo;
import ywcai.ls.control.MyPullListView;
import ywcai.ls.control.MyPullViewCallBack;
import ywcai.ls.core.task.CheckToken;
import ywcai.ls.core.task.ScanRemotePc;
import ywcai.ls.core.thread.MyThreadFactory;
import ywcai.ls.inf.CallBackLanScanResultInf;
import ywcai.ls.mobileutil.R;
import ywcai.ls.util.MyConfig;
import ywcai.ls.util.MyUtil;


public class LocalServerScanFragment extends Fragment implements CallBackLanScanResultInf {

    private View tabView;
    private MyHandler myHandler;
    private int scanNum = 0;
    private List<HashMap<String, String>> ipList;
    private ContentLoadingProgressBar lanLoader;
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
    private int nowSelectPos;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tabView = inflater.inflate(R.layout.fragment_tab_mouse, container, false);
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
        lanLoader = (ContentLoadingProgressBar) tabView.findViewById(R.id.pb_lan);
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

        ImageView img_close  = (ImageView) tabView.findViewById(R.id.btn_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClosePopWindow();
            }
        });
        lanListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position>0) {
                    ShowConnUi(position);
                }
            }
        });
        TextView btn_conn=(TextView)tabView.findViewById(R.id.tv_conn);
        btn_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestConn();
            }
        });
    }

    private void RequestConn() {
        String ipaddr=ipList.get(nowSelectPos).get("lanIp");
        TextView text=(TextView)tabView.findViewById(R.id.tv_psw);
        String psw=text.getText().toString();
        SendPSW(ipaddr,psw);
        HideFormOnly();
        ShowLoader();
    }
    private void SendPSW(String ipAddr,String psw) {

        MyThreadFactory myThreadFactory = new MyThreadFactory();
        executorService = Executors.newSingleThreadExecutor(myThreadFactory);
        CheckToken checkToken=new CheckToken(psw,ipAddr,this);
        executorService.execute(checkToken);
    }

    private void ShowConnUi(int position) {
        nowSelectPos=position;
        RelativeLayout rl_config = (RelativeLayout) tabView.findViewById(R.id.rl_show_config);
        FrameLayout fl = (FrameLayout) tabView.findViewById(R.id.frame_mask);
        fl.setVisibility(View.VISIBLE);
        rl_config.setVisibility(View.VISIBLE);
    }
    private void HideAll() {
        HideFrameOnly();
        HideFormOnly();
        HideLoader();
    }
    private void HideFrameOnly()
    {
        FrameLayout fl = (FrameLayout) tabView.findViewById(R.id.frame_mask);
        fl.setVisibility(View.GONE);
    }
    private void HideFormOnly()
    {
        RelativeLayout rl_config = (RelativeLayout) tabView.findViewById(R.id.rl_show_config);
        rl_config.setVisibility(View.GONE);
    }
    private void ShowLoader()
    {
        lanListView.setEnabled(false);
        ProgressBar loader=(ProgressBar)tabView.findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);
    }
    private void HideLoader()
    {
        lanListView.setEnabled(true);
        ProgressBar loader=(ProgressBar)tabView.findViewById(R.id.loader);
        loader.setVisibility(View.GONE);
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
        HideAll();
        ipList.clear();
        simpleAdapter.notifyDataSetChanged();
        LoadNetSub();
        scanNum = 0;
        MyThreadFactory myThreadFactory = new MyThreadFactory();
        executorService = Executors.newFixedThreadPool(MyConfig.INT_SCAN_LAN_THREAD_COUNT, myThreadFactory);
        String scanIp = ipRecord;
        for (int i = 1; i <= 254; i++) {
                ScanRemotePc scanPc = new ScanRemotePc(scanIp + "." + i, this,localIp);
                executorService.execute(scanPc);
        }
    }

    private void ScanEnd() {
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
    public void UpdateIpList(LanInfo pLanInfo) {
        Message mes = new Message();
        mes.what = 0;
        mes.obj = pLanInfo;
        myHandler.sendMessage(mes);
    }

    @Override
    public void CallBackCheckResult(Boolean isSuccess, String identity) {
        Message mes=new Message();
        mes.what=1;
        CheckResult checkResult=new CheckResult();
        checkResult.isSuccess=isSuccess;
        checkResult.identity=identity;
        mes.obj=checkResult;
        myHandler.sendMessage(mes);

    }

    private void ClosePopWindow() {
        HideAll();
    }



    class MyHandler extends Handler {
        private void ScanRefresh()
        {
            synchronized (lock) {
                if(scanNum<254)
                    scanNum++;
            }
            int per = scanNum * 100 / 254;
            lanLoader.setVisibility(View.VISIBLE);
            lanLoader.setProgress(per);
            tv_tip.setText("正在扫描内网中可用的远端设备,已扫描"+per + "%");
            if (per == 100) {
                ScanEnd();
                ScanEndAnimation();
            }
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    LanInfo lanInfo = (LanInfo) msg.obj;
                    if(lanInfo.isExist) {
                        HashMap<String, String> hs = new HashMap<>();
                        if (!lanInfo.lanIp.equals(localIp)) {
                            hs.put("lanIp", lanInfo.lanIp);
                            hs.put("lanMac", lanInfo.lanMac);
                            hs.put("lanDevice", lanInfo.lanDevice);
                            ipList.add(hs);
                        }
                        simpleAdapter.notifyDataSetChanged();
                    }
                    ScanRefresh();
                    break;
                case 1:
                    CheckResult checkResult=(CheckResult)msg.obj;
                    if(checkResult.isSuccess) {
                        Toast.makeText(getContext(), "success", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        HideAll();
                        Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
                    }
                    break;
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
            tv_tip.setText("正在准备扫描IP...");
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
    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(),"ScanIp");
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(this.getContext(),"ScanIp");
    }
}
