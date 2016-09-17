package ywcai.ls.core;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import ywcai.ls.bean.PingParameter;
import ywcai.ls.bean.PingResult;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.sub.NetActivity;
import ywcai.ls.mobileutil.sub.PingAnalysisActivity;
import ywcai.ls.task.MyThreadFactory;
import ywcai.ls.task.PingFast;
import ywcai.ls.task.PingNormal;
import ywcai.ls.ui.child.PingFragment;
import ywcai.ls.util.MyConfig;


/**
 * Created by zmy_11 on 2016/8/12.
 */
public class Ping extends Handler {

    public PingParameter pingParameter;
    public ExecutorService executorService;
    private View tabView;
    private Context context;
    private MyThreadFactory myThreadFactory ;
    private List<HashMap<String, String>> list;
    private Resources rs;
    private String[] title;
    private SimpleAdapter simpleAdapter;
    private TextView tv_log, tv_packageLenth, tv_ipAddress, tv_threadSize, tv_taskCount;
    private Button btn_ctrl, btn_saveLog, btn_clearLog;
    private Spinner spn_threadSize, spn_taskCount;
    private SwitchCompat swc_sendMethod;
    private ProgressBar bar_loading;
    private ArrayList<Integer> log=new ArrayList<>();;
    private int sendCount = 0;
    private PingFragment meFragment;

    public Ping(View view, PingFragment fragment) {
        meFragment=fragment;
        InitObj(view);
        InitView();
        regEventListener();
        InitList();
    }
    private void InitObj(View view) {

        tabView=view;
        context = MyApplication.getInstance().getApplicationContext();;
        rs = context.getResources();
        title = rs.getStringArray(R.array.ping_results);
        myThreadFactory = new MyThreadFactory();
        pingParameter = new PingParameter();
        pingParameter.sendMethod = false;
    }
    private void InitView() {
        tv_log = (TextView) tabView.findViewById(R.id.tv_ping_log);
        tv_log.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_threadSize = (TextView) tabView.findViewById(R.id.textView4);
        tv_taskCount = (TextView) tabView.findViewById(R.id.textView5);
        swc_sendMethod = (SwitchCompat) tabView.findViewById(R.id.send_method);
        btn_ctrl = (Button) tabView.findViewById(R.id.ping_ctrl);
        btn_saveLog = (Button) tabView.findViewById(R.id.ping_save_log);
        btn_clearLog = (Button) tabView.findViewById(R.id.ping_clear_log);
        tv_packageLenth = (TextView) tabView.findViewById(R.id.tv_package_lenth);
        tv_ipAddress = (TextView) tabView.findViewById(R.id.tv_ip);
        spn_threadSize = (Spinner) tabView.findViewById(R.id.tv_thread_count);
        spn_taskCount = (Spinner) tabView.findViewById(R.id.tv_package_count);
        bar_loading = (ProgressBar) tabView.findViewById(R.id.fast_ping_loader);
        tv_threadSize.setVisibility(View.GONE);
        tv_taskCount.setVisibility(View.GONE);
        spn_threadSize.setVisibility(View.GONE);
        spn_taskCount.setVisibility(View.GONE);
        tv_log.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN){

                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if(event.getAction()==MotionEvent.ACTION_MOVE){

                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if(event.getAction()==MotionEvent.ACTION_UP){
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });
    }
    private void AnalysisResult(ArrayList<Integer> log)
    {
        if(log.size()>0)
        {
            Intent intent=new Intent();
            Bundle bundle=new Bundle();
            bundle.putIntegerArrayList(MyConfig.STR_INTENT_LIST_ARGS,log);
            intent.putExtras(bundle);
            intent.setClass(context, PingAnalysisActivity.class);
            meFragment.startActivity(intent);
            tv_log.setText("数据长度:"+log.size());
        }
        else
        {
            tv_log.setText("你还没有任何日志数据！");
        }
    }
    private void InitList()
    {
        list = new ArrayList<>();
        HashMap<String, String> hs = new HashMap();
        hs.put(title[0], "0");
        hs.put(title[1], "0");
        hs.put(title[2], "0%");
        hs.put(title[3], "0ms");
        hs.put(title[4], "0ms");
        hs.put(title[5], "0ms");
        list.add(hs);
        ListViewCompat listViewCompat = (ListViewCompat) tabView.findViewById(R.id.now_ping);
        simpleAdapter = new SimpleAdapter(context, list, R.layout.listview_ping, title, new int[]{R.id.send_num, R.id.received_num
                , R.id.percent, R.id.max_delay, R.id.min_delay, R.id.average_delay});
        listViewCompat.setAdapter(simpleAdapter);
    }
    private void regEventListener() {
        swc_sendMethod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pingParameter.sendMethod = true;
                    tv_threadSize.setVisibility(View.VISIBLE);
                    tv_taskCount.setVisibility(View.VISIBLE);
                    spn_threadSize.setVisibility(View.VISIBLE);
                    spn_taskCount.setVisibility(View.VISIBLE);
                } else {
                    pingParameter.sendMethod = false;
                    tv_threadSize.setVisibility(View.GONE);
                    tv_taskCount.setVisibility(View.GONE);
                    spn_threadSize.setVisibility(View.GONE);
                    spn_taskCount.setVisibility(View.GONE);
                }
            }
        });
        btn_ctrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pingParameter.isWorking) {
                    if (!pingParameter.sendMethod) {
                        startPingNormal();
                    } else {
                        startPingFast();
                    }
                } else {
                    setWorkFalse();
                    if (!pingParameter.sendMethod) {
                        //normal ping : send over message by the background thread to draw the desk
                        setBtnCtrlFalse();

                    } else {
                        //fast ping :  draw the desk on the ui thread and shutdown the background threads  at once .
                        breakBackgroundThread();
                        changUiToOver();
                    }
                }
            }
        });
        btn_saveLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log.add(10);
                log.add(100);
                log.add(500);
                AnalysisResult(log);
            }
        });

        btn_clearLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_log.setText("");
                //bar_loading.setIndeterminate(false);
                changLoader(0);
            }
        });
    }
    private boolean checkInput() {
        pingParameter.ipAddress = tv_ipAddress.getText().toString();
        if (pingParameter.ipAddress.equals("")) {
            tv_log.setText("填写IP地址");
            return false;
        }
        if (!tv_packageLenth.getText().toString().equals("")) {
            try {
                pingParameter.lenth = Integer.parseInt(tv_packageLenth.getText().toString());
                if (pingParameter.lenth > 1460) {
                    tv_log.setText("负载长度不能大于1460");
                    return false;
                }
                if (pingParameter.lenth < 32) {
                    tv_log.setText("负载长度不能小于32");
                    return false;
                }
            } catch (Exception e) {
                tv_log.setText("负载填写错误:" + e.toString());
                return false;
            }
        }
        return true;
    }
    private void setBtnCtrlFalse() {
        btn_ctrl.setText("正在停止");
        btn_ctrl.setEnabled(false);
    }
    private void changUiToRunning() {
        tv_ipAddress.setEnabled(false);
        tv_packageLenth.setEnabled(false);
        swc_sendMethod.setEnabled(false);
        spn_threadSize.setEnabled(false);
        spn_taskCount.setEnabled(false);
        btn_ctrl.setText("停止测试");
        btn_saveLog.setEnabled(false);
        btn_clearLog.setEnabled(false);
        tv_log.setText("");
    }
    private void changUiToOver() {
        bar_loading.setIndeterminate(false);
        tv_ipAddress.setEnabled(true);
        tv_packageLenth.setEnabled(true);
        swc_sendMethod.setEnabled(true);
        spn_threadSize.setEnabled(true);
        spn_taskCount.setEnabled(true);
        btn_ctrl.setText("开始测试");
        btn_ctrl.setEnabled(true);
        btn_saveLog.setEnabled(true);
        btn_clearLog.setEnabled(true);
        tv_log.append("\n----PING OVER !----");
    }
    private void startPingNormal() {
        if (!checkInput()) {
            return;
        }
        pingParameter.isWorking = true;
        bar_loading.setIndeterminate(true);

        changUiToRunning();
        ScheduledExecutorService executorSingle = Executors.newSingleThreadScheduledExecutor(myThreadFactory);
        PingNormal pingNormal = new PingNormal(this, pingParameter);
        executorSingle.execute(pingNormal);
    }

    private void startPingFast() {
        if (!checkInput()) {
            return;
        }
        sendCount=0;
        pingParameter.isWorking = true;
        pingParameter.count = Integer.parseInt(spn_taskCount.getSelectedItem().toString());
        int threadPoolSize = Integer.parseInt(spn_threadSize.getSelectedItem().toString());
        changUiToRunning();
        tv_log.setText("多线程PING模式将会尽可能的使用设备计算资源，为减轻CPU负担，将不显示单包测试信息!");
        bar_loading.setIndeterminate(false);
        changLoader(0);
        executorService = Executors.newFixedThreadPool(threadPoolSize, myThreadFactory);
        PingFast pingFast = new PingFast(this, pingParameter);
        for (int i = 0; i < pingParameter.count; i++) {
            executorService.execute(pingFast);
        }
    }

    private void setWorkFalse() {
        //sendCount=0;
        pingParameter.isWorking = false;
    }

    private void breakBackgroundThread() {
        try {
            executorService.shutdownNow();
        } catch (Exception e) {
            //Toast.makeText(context,"shutdown thread failed ! "+e,Toast.LENGTH_SHORT).show();
        }
    }


    private void changLoader(int per) {
        bar_loading.setProgress(per);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        PingResult pingResult = (PingResult) msg.obj;
        list.clear();
        HashMap<String,String> hs=new HashMap<String,String>();
        hs.put(title[0], pingResult.send + "");
        hs.put(title[1], pingResult.receive + "");
        hs.put(title[2], pingResult.percent + "%");
        hs.put(title[3], pingResult.max + "ms");
        hs.put(title[4], pingResult.min + "ms");
        hs.put(title[5], pingResult.average + "ms");
        list.add(hs);
        simpleAdapter.notifyDataSetChanged();
        switch (msg.what) {
            //ping normal :send the work data
            case 0:
                tv_log.append(pingResult.log);
                break;
            //ping normal :send the stop message
            case 1:
                changUiToOver();
                break;
            case 2:
            //ping fast :send the all work message until  the work is end !
                sendCount++;
                changLoader(Math.round(sendCount * 100 / pingParameter.count));
                if (sendCount >= pingParameter.count)
                {
                    setWorkFalse();
                    changUiToOver();
                }
                break;
        }
    }



}
