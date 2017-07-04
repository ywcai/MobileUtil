package ywcai.ls.module.tools.lan.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.controls.popwindow.PopView;
import ywcai.ls.controls.popwindow.inf.OnWindowEventListener;
import ywcai.ls.controls.popwindow.inf.PopWinEvent;
import ywcai.ls.controls.pull.LsPullView;
import ywcai.ls.mobileutil.R;
import ywcai.ls.module.tools.lan.presenter.ScanWorkImpl;
import ywcai.ls.util.InputRule;

public class LanFragment extends Fragment {

    private View tabView;
    private LsPullView scanListView;
    private PopView popView;
    private ScanWorkImpl workTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tabView = inflater.inflate(R.layout.fragment_tab_lan, container, false);
        InitCustomView();
        InitPullView();
        return tabView;
    }
    private void InitCustomView()
    {
        popView=(PopView)tabView.findViewById(R.id.pop_window);
        popView.setWinEventListener(new OnWindowEventListener() {
            @Override
            public void onEvent(PopWinEvent popWinEvent) {
                switch (popWinEvent)
                {
                    case RESET:
                        workTask.resetLocalIp();
                        popView.setVisibility(View.INVISIBLE);
                        break;
                    case SUBMIT:
                        submitInput(popView.getInput());
                        break;
                }
            }
        });
        TextView tv_config = (TextView) tabView.findViewById(R.id.tv_cf);
        tv_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popView.setVisibility(View.VISIBLE);
            }
        });
        TextView tv_stop = (TextView) tabView.findViewById(R.id.tv_stop);
        tv_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanListView.manualBreakThread();
            }
        });
    }
    private void submitInput(String input) {
        InputRule inputRule=new InputRule();
        if(inputRule.checkIpRule(input))
        {
            workTask.setInputIp(input);
        }
        else {
            workTask.resetLocalIp();
            Toast.makeText(getContext(), "Char input is not a ip address!", Toast.LENGTH_SHORT).show();
        }
    }
    private void InitPullView() {
        scanListView=(LsPullView)tabView.findViewById(R.id.scan_list_view);

        List list=new ArrayList();
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(),
                list,
                R.layout.listview_lan, new String[]{"lanIp", "lanMac", "lanDevice"},
                new int[]{R.id.lan_ip, R.id.lan_mac, R.id.lan_device});
        scanListView.setAdapter(simpleAdapter,list);
        workTask=new ScanWorkImpl(getContext());
        scanListView.InjectionTask(workTask);
    }

    @Override
    public void onDestroy() {
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
