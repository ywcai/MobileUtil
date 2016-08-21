package ywcai.ls.core;


import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.method.ScrollingMovementMethod;
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
import ywcai.ls.task.MyThreadFactory;
import ywcai.ls.task.PingFast;
import ywcai.ls.task.PingNormal;
import ywcai.ls.ui.UpdateViewInf;


/**
 * Created by zmy_11 on 2016/8/12.
 */
public class Ping implements UpdateViewInf {

    private View tabView;
    private Context context;
    private List<HashMap<String, String>> list;
    private Resources rs;
    private String[] title;
    private PingResult pingResult;
    private SimpleAdapter simpleAdapter;
    public PingParameter pingParameter;
    private HashMap<String, String> hs;
    public Ping(View view) {
        context = MyApplication.getInstance().getApplicationContext();
        tabView = view;
        rs = context.getResources();
        title = rs.getStringArray(R.array.ping_results);
        InitView();
        setWorkMethod();

        ListViewCompat listViewCompat = (ListViewCompat) tabView.findViewById(R.id.now_ping);
        list = getList();
        simpleAdapter = new SimpleAdapter(context, list, R.layout.listview_ping, title, new int[]{R.id.send_num, R.id.received_num
                , R.id.percent, R.id.max_delay, R.id.min_delay, R.id.average_delay});
        listViewCompat.setAdapter(simpleAdapter);
    }

    private void InitView() {
        TextView textView = (TextView) tabView.findViewById(R.id.tv_ping_log);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        pingParameter = new PingParameter();
        pingResult = new PingResult();
        SwitchCompat switchCompat = (SwitchCompat) tabView.findViewById(R.id.send_method);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    pingParameter.sendMethod = true;
                    TextView textView4 = (TextView) tabView.findViewById(R.id.textView4);
                    TextView textView5 = (TextView) tabView.findViewById(R.id.textView5);
                    Spinner thread_count = (Spinner) tabView.findViewById(R.id.tv_thread_count);
                    Spinner data_count = (Spinner) tabView.findViewById(R.id.tv_package_count);
                    textView4.setVisibility(View.VISIBLE);
                    textView5.setVisibility(View.VISIBLE);
                    thread_count.setVisibility(View.VISIBLE);
                    data_count.setVisibility(View.VISIBLE);
                } else {
                    pingParameter.sendMethod = false;
                    TextView textView4 = (TextView) tabView.findViewById(R.id.textView4);
                    TextView textView5 = (TextView) tabView.findViewById(R.id.textView5);
                    Spinner thread_count = (Spinner) tabView.findViewById(R.id.tv_thread_count);
                    Spinner data_count = (Spinner) tabView.findViewById(R.id.tv_package_count);
                    textView4.setVisibility(View.GONE);
                    textView5.setVisibility(View.GONE);
                    thread_count.setVisibility(View.GONE);
                    data_count.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setWorkMethod() {
        Button ctrl = (Button) tabView.findViewById(R.id.ping_ctrl);
        Button saveLog = (Button) tabView.findViewById(R.id.ping_save_log);
        Button clearLog = (Button) tabView.findViewById(R.id.ping_clear_log);
        ctrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pingParameter.isWorking) {
                    pingParameter.isWorking = true;
                    if (!pingParameter.sendMethod) {
                        startNormalPing();
                    } else {
                        startFastPing();
                    }
                } else {
                    if (!pingParameter.sendMethod)
                    {
                        stopPing();
                    }
                    else
                    {
                        changUiToEnd();
                        TextView log = (TextView) tabView.findViewById(R.id.tv_ping_log);
                        log.append("\n----PING OVER !----");
                    }
                }
            }
        });
        saveLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLog();
            }
        });

        clearLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView log = (TextView) tabView.findViewById(R.id.tv_ping_log);
                log.setText("");
                ProgressBar loader = (ProgressBar) tabView.findViewById(R.id.fast_ping_loader);
                loader.setIndeterminate(false);
                changLoader(0);
            }
        });
    }

    private void startNormalPing() {
        if(!checkInput())
        {
            return;
        }
        changUiToStart();
        ProgressBar loader = (ProgressBar) tabView.findViewById(R.id.fast_ping_loader);
        loader.setIndeterminate(true);
        Handler pingHandler = new PingHandler();
        MyThreadFactory myThreadFactory = new MyThreadFactory();
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(myThreadFactory);
        PingNormal pingNormal = new PingNormal(pingHandler, pingParameter);
        executorService.execute(pingNormal);
    }

    private boolean checkInput() {
        TextView tv_ipAddress = (TextView) tabView.findViewById(R.id.tv_ip);
        TextView tv_log = (TextView) tabView.findViewById(R.id.tv_ping_log);
        pingParameter.ipAddress = tv_ipAddress.getText().toString();
        if (pingParameter.ipAddress.equals("")) {

            tv_log.setText("填写IP地址");
            return false;
        }
        TextView packageLenth = (TextView) tabView.findViewById(R.id.tv_package_lenth);
        if (!packageLenth.getText().toString().equals("")) {
            try {
                pingParameter.lenth = Integer.parseInt(packageLenth.getText().toString());
                if (pingParameter.lenth > 10000) {
                    tv_log.setText("负载长度不能大于10000");
                    return false;
                }
                if (pingParameter.lenth < 32) {
                    return false;
                }
            } catch (Exception e) {
                tv_log.setText("负载填写超出范围:" + e.toString());
                return false;
            }
        }
        return true;
    }
    private void startFastPing() {
        if(!checkInput())
        {
            return;
        }
        Spinner data_size = (Spinner) tabView.findViewById(R.id.tv_package_count);
        pingParameter.count = Integer.parseInt(data_size.getSelectedItem().toString());
        Spinner thread_size = (Spinner) tabView.findViewById(R.id.tv_thread_count);
        int threadPoolSize = Integer.parseInt(thread_size.getSelectedItem().toString());
        changUiToStart();
        ProgressBar loader = (ProgressBar) tabView.findViewById(R.id.fast_ping_loader);
        loader.setIndeterminate(false);
        changLoader(0);

        Handler pingHandler = new PingHandler();
        MyThreadFactory myThreadFactory = new MyThreadFactory();
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize, myThreadFactory);
        PingFast pingFast = new PingFast(pingHandler, pingParameter);
        for (int i = 0; i < pingParameter.count; i++) {
            executorService.execute(pingFast);
        }
    }

    private void stopPing() {
        pingParameter.isWorking = false;

    }

    private void setClose()
    {
        Button ctrl = (Button) tabView.findViewById(R.id.ping_ctrl);
        ctrl.setText("正在停止");
        ctrl.setEnabled(false);

    }


    private void changUiToStart() {
        TextView log = (TextView) tabView.findViewById(R.id.tv_ping_log);
        log.setText("");
        Button ctrl = (Button) tabView.findViewById(R.id.ping_ctrl);
        ctrl.setText("停止测试");
        Button saveLog = (Button) tabView.findViewById(R.id.ping_save_log);
        saveLog.setEnabled(false);
        TextView packageLenth=(TextView)tabView.findViewById(R.id.tv_package_lenth);
        packageLenth.setEnabled(false);
        TextView tv_ipAddress = (TextView) tabView.findViewById(R.id.tv_ip);
        tv_ipAddress.setEnabled(false);
        Spinner thread_size = (Spinner) tabView.findViewById(R.id.tv_thread_count);
        thread_size.setEnabled(false);
        Spinner data_size = (Spinner) tabView.findViewById(R.id.tv_package_count);
        data_size.setEnabled(false);
    }

    private void changUiToEnd() {
        setClose();
        ProgressBar loader = (ProgressBar) tabView.findViewById(R.id.fast_ping_loader);
        loader.setIndeterminate(false);
        //loader.setProgress(0);
        Button ctrl = (Button) tabView.findViewById(R.id.ping_ctrl);
        ctrl.setText("开始测试");
        ctrl.setEnabled(true);
        Button saveLog = (Button) tabView.findViewById(R.id.ping_save_log);
        saveLog.setEnabled(true);
        TextView packageLenth=(TextView)tabView.findViewById(R.id.tv_package_lenth);
        packageLenth.setEnabled(true);
        TextView tv_ipAddress = (TextView) tabView.findViewById(R.id.tv_ip);
        tv_ipAddress.setEnabled(true);
        Spinner thread_size = (Spinner) tabView.findViewById(R.id.tv_thread_count);
        thread_size.setEnabled(true);
        Spinner data_size = (Spinner) tabView.findViewById(R.id.tv_package_count);
        data_size.setEnabled(true);
    }

    private void changLoader(int per) {
        ProgressBar loader = (ProgressBar) tabView.findViewById(R.id.fast_ping_loader);
        loader.setProgress(per);
    }

    private void saveLog() {


    }

    private List<HashMap<String, String>> getList() {
        List<HashMap<String, String>> myList = new ArrayList<>();
        hs = new HashMap();
        pingResult = new PingResult();
        hs.put(title[0], pingResult.send + "");
        hs.put(title[1], pingResult.receive + "");
        hs.put(title[2], pingResult.percent + "%");
        hs.put(title[3], pingResult.max + "ms");
        hs.put(title[4], pingResult.min + "ms");
        hs.put(title[5], pingResult.average + "ms");
        myList.add(hs);
        return myList;
    }

    @Override
    public void updateView() {

    }

    class PingHandler extends Handler {
        int sendCount=0;
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            pingResult =(PingResult)msg.obj;
            TextView log = (TextView) tabView.findViewById(R.id.tv_ping_log);
            log.append( pingResult.log);
            list.clear();
            hs.put(title[0], pingResult.send + "");
            hs.put(title[1], pingResult.receive + "");
            hs.put(title[2], pingResult.percent + "%");
            hs.put(title[3], pingResult.max + "ms");
            hs.put(title[4], pingResult.min + "ms");
            hs.put(title[5], pingResult.average + "ms");
            list.add(hs);
            simpleAdapter.notifyDataSetChanged();
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    changUiToEnd();
                    break;
                case 2:
                    sendCount++;
                        changLoader(Math.round(sendCount * 100 / pingParameter.count));
                        if (sendCount >= pingParameter.count) {
                            log.append("\n----PING OVER !----");
                            changUiToEnd();
                        }
                    break;
            }
        }
    }

}
