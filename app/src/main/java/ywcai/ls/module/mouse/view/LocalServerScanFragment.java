package ywcai.ls.module.mouse.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

import java.util.List;

import ywcai.ls.controls.loader.LoaderView;
import ywcai.ls.controls.popwindow.PopView;
import ywcai.ls.controls.popwindow.inf.OnWindowEventListener;
import ywcai.ls.controls.popwindow.inf.PopWinEvent;
import ywcai.ls.controls.pull.LsPullView;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.module.mouse.presenter.ActionImpl;
import ywcai.ls.module.mouse.presenter.inf.UpdateViewInf;


public class LocalServerScanFragment extends Fragment implements UpdateViewInf {

    private View tabView;
    private LsPullView lanListView;
    private ActionImpl action;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tabView = inflater.inflate(R.layout.fragment_homepage_scan_pc, container, false);
        InitPullListView();
        InitEvent();
        return tabView;
    }

    private void InitEvent() {
//        TextView btn_setIp = (TextView) tabView.findViewById(R.id.rpc_tv_set_ip);
        TextView btn_breakScan = (TextView) tabView.findViewById(R.id.rpc_tv_stop_scan);
//        btn_setIp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                action.showIpConfigBox();
//            }
//        });
        btn_breakScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.breakScanManual();
            }
        });
//        PopView set_ip = (PopView) tabView.findViewById(R.id.rl_input_ip);
//        set_ip.setWinEventListener(new OnWindowEventListener() {
//            @Override
//            public void onEvent(PopWinEvent popWinEvent) {
//                switch (popWinEvent) {
//                    case SUBMIT:
//                        checkRule();
//                        break;
//                }
//            }
//        });
        PopView rl_show = (PopView) tabView.findViewById(R.id.rl_show);
        rl_show.setWinEventListener(new OnWindowEventListener() {
            @Override
            public void onEvent(PopWinEvent popWinEvent) {
                switch (popWinEvent) {
                    case SUBMIT:
                        action.requestConn();
                        break;
                }
            }
        });
    }

//    public void checkRule() {
//        InputRule inputRule = new InputRule();
//        if (inputRule.checkIpRule(getIp())) {
//            action.setIp();
//        } else {
//            showInfo("你输入的不是正确的IP地址!");
//        }
//    }

    private void InitPullListView() {
        lanListView = (LsPullView) tabView.findViewById(R.id.list_lan);
        action = new ActionImpl(this);
        action.InitConnStatus();
        lanListView.setEmptyString("下拉刷新搜索列表");
        lanListView.InjectionTask(action);
    }

    private void setFormVisible(int visible) {
        PopView rl_show = (PopView) tabView.findViewById(R.id.rl_show);
        rl_show.setVisibility(visible);
    }

    private void setIpBoxVisible(int visible) {
//        PopView rl_show = (PopView) tabView.findViewById(R.id.rl_input_ip);
//        rl_show.setVisibility(visible);
    }


    private void setLoaderVisible(int visible, String tip) {
        LoaderView loader = (LoaderView) tabView.findViewById(R.id.loader);
        loader.setVisibility(visible);
        loader.setText(tip);
    }

    private void toRemoteActivity() {
        Intent intent = new Intent();
        intent.setClass(getContext(), RemoteActivity.class);
        startActivity(intent);
    }

    @Override
    public void setPullViewStatus(boolean conn, List list) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(),
                list,
                R.layout.listview_lan, new String[]{"lanIp", "lanMac", "lanDevice"},
                new int[]{R.id.lan_ip, R.id.lan_mac, R.id.lan_device});
        lanListView.setAdapter(simpleAdapter, list);
        lanListView.setCanPull(!conn);
    }

    @Override
    public void onClickListItem() {
        setFormVisible(View.VISIBLE);
    }

    @Override
    public void sessionCreating() {
        setLoaderVisible(View.VISIBLE, "正在与远端PC建立连接...");
        setFormVisible(View.GONE);
    }

    @Override
    public void sessionCreated(boolean conn) {
        setFormVisible(View.GONE);
        lanListView.setCanPull(!conn);
        if (conn) {
            setLoaderVisible(View.VISIBLE, "与远端PC建立连接，准备发送秘钥...");
        } else {
            endLoaderAnimation("与远端PC建立连接失败");

        }
    }

    private void endLoaderAnimation(String info) {
        setLoaderVisible(View.GONE, info);
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        LoaderView loader = (LoaderView) tabView.findViewById(R.id.loader);
        loader.startAnimation(animation);
    }


    @Override
    public void sessionChecking(int count) {
        setLoaderVisible(View.VISIBLE, "正在校验秘钥，" + count + "秒后系统超时返回!");
    }

    @Override
    public void sessionChecked(boolean checked, String reason) {
        lanListView.setCanPull(!checked);
        setLoaderVisible(View.GONE, "");
        setFormVisible(View.GONE);
        if (checked) {
            toRemoteActivity();
        } else {
            endLoaderAnimation("建立连接失败");
        }
    }

    @Override
    public void sessionClosed() {
        showInfo("网络连接断开!");
        lanListView.setCanPull(true);
    }

    @Override
    public String getUsername() {
        return "username";
    }

    @Override
    public String getPsw() {
        PopView rl_show = (PopView) tabView.findViewById(R.id.rl_show);
        return rl_show.getInput();
    }

    @Override
    public String getIp() {
//        PopView rl_ip = (PopView) tabView.findViewById(R.id.rl_input_ip);
//        return rl_ip.getInput();
        return "";
    }

    @Override
    public void showInfo(String info) {
        Toast.makeText(MyApplication.getInstance().getApplicationContext(), info, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClickBreakBtn() {
            lanListView.manualBreakThread();
    }

    @Override
    public void onClickSetIpBtn() {
        setIpBoxVisible(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(), "remote");
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(this.getContext(), "remote");
    }
}
