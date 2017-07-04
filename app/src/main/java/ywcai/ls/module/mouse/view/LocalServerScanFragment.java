package ywcai.ls.module.mouse.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.mobstat.StatService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;
import ywcai.ls.common.ComponentStatus;
import ywcai.ls.common.eventbus.MouseViewUpdate;
import ywcai.ls.controls.loader.LoaderView;
import ywcai.ls.controls.popwindow.PopView;
import ywcai.ls.controls.popwindow.inf.OnWindowEventListener;
import ywcai.ls.controls.popwindow.inf.PopWinEvent;
import ywcai.ls.controls.pull.LsPullView;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.module.mouse.model.PullTask;
import ywcai.ls.module.mouse.presenter.ActionImpl;

public class LocalServerScanFragment extends Fragment   {
    private View tabView;
    private LsPullView lanListView;
    public ActionImpl action;
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tabView = inflater.inflate(R.layout.fragment_homepage_scan_pc, container, false);
        InitPullView();
        InitViewEvent();
        InitBtnEvent();
        LoadStatus();
        return tabView;
    }
    private void LoadStatus() {
        action=new ActionImpl();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(MouseViewUpdate mouseViewUpdate)
    {
        switch (mouseViewUpdate.mouseViewUpdateType)
        {
            case LOAD_END:
                loaded(mouseViewUpdate.tip);
                break;
            case LOADING:
                loading(mouseViewUpdate.tip);
                break;
            case LOAD_NONE_VIEW:
                loaded(mouseViewUpdate.tip);
                setNormalMode();
                break;
            case LOAD_CONN_VIEW:
                loaded(mouseViewUpdate.tip);
                setConnMode();
                break;
            case LOAD_SHADOW_VIEW:
                loaded(mouseViewUpdate.tip);
                setShadowMode();
                break;
            case LOAD_MOUSE_VIEW:
                loaded(mouseViewUpdate.tip);
                setMouseMode();
                break;
            case CLICK_LIST_VIEW:
                showConnPop(mouseViewUpdate.tip);
                break;
            case SHOW_TOAST:
                showInfo(mouseViewUpdate.tip);
                break;
            case SHOW_PSW_BOX:
                closeLoader();
                showPswBox();
                break;
            case SESSION_CLOSED:
                sessionClosed(mouseViewUpdate.tip);
                break;
            case REPEAT_SUCCESS:
                action.repeatConnSuccess();
                break;
        }
    }

    private void sessionClosed(String tip) {
        switch (ComponentStatus.getInstance().mouseViewType)
        {
            case NONE:
                loaded("TCP连接断开!");
                break;
            default:
                showInfo(tip);
                action.repeatConn();
        }
    }

    private void showPswBox() {

        PopView inputPsw = (PopView) tabView.findViewById(R.id.rl_input_psw);
        inputPsw.setVisibility(View.VISIBLE);
    }

    private void InitPullView() {
        lanListView = (LsPullView) tabView.findViewById(R.id.list_lan);
        List list=new ArrayList();
        lanListView.setEmptyString("下拉自动搜索列表");
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(),
                list,
                R.layout.listview_lan, new String[]{"lanIp", "lanMac", "lanDevice"},
                new int[]{R.id.lan_ip, R.id.lan_mac, R.id.lan_device});
        lanListView.setAdapter(simpleAdapter,list);
        lanListView.setCanPull(true);
        PullTask task = new PullTask();
        lanListView.InjectionTask(task);
    }
    private void InitViewEvent() {
        TextView btn_setIp = (TextView) tabView.findViewById(R.id.rpc_tv_set_ip);
        final PopView inputIp = (PopView) tabView.findViewById(R.id.rl_input_ip);
        final PopView inputPsw = (PopView) tabView.findViewById(R.id.rl_input_psw);
        TextView btn_breakScan = (TextView) tabView.findViewById(R.id.rpc_tv_stop_scan);
        btn_setIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanListView.manualBreakThread();
                inputIp.setVisibility(View.VISIBLE);
            }
        });
        btn_breakScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lanListView.manualBreakThread();
            }
        });
        inputIp.setWinEventListener(new OnWindowEventListener() {
            @Override
            public void onEvent(PopWinEvent popWinEvent) {
                switch (popWinEvent) {
                    case SUBMIT:
                        action.requestConn(inputIp.getInput());
                        break;
                }
            }
        });
        inputPsw.setWinEventListener(new OnWindowEventListener() {
            @Override
            public void onEvent(PopWinEvent popWinEvent) {
                switch (popWinEvent) {
                    case SUBMIT:
                        action.checkPsw(inputPsw.getInput());
                        break;
                    case CLOSED:
                        action.cancalConn();
                        break;
                }
            }
        });
    }
    private void InitBtnEvent()
    {
        Button mouseBtn = (Button) tabView.findViewById(R.id.bt_mouse);
        Button shadowBtn = (Button) tabView.findViewById(R.id.bt_shadow);
        Button disconnectBtn = (Button) tabView.findViewById(R.id.bt_exit);
        mouseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.clickMouseBtn();
            }
        });
        shadowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.clickShadowBtn(getActivity());
            }
        });
        disconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.cancalConn();
            }
        });

    }

    private void setNormalMode() {
        RelativeLayout rlNormal=(RelativeLayout)tabView.findViewById(R.id.mouse_init_view);
        rlNormal.setVisibility(View.VISIBLE);
        RelativeLayout rlConn=(RelativeLayout)tabView.findViewById(R.id.mouse_conn_view);
        rlConn.setVisibility(View.GONE);

        Button mouseBtn = (Button) tabView.findViewById(R.id.bt_mouse);
        Button shadowBtn = (Button) tabView.findViewById(R.id.bt_shadow);
        Button disconnectBtn = (Button) tabView.findViewById(R.id.bt_exit);
        mouseBtn.setText("进入空中鼠标模式");
        mouseBtn.setEnabled(true);
        shadowBtn.setText("进入手机投影模式");
        disconnectBtn.setText("断开远端会话连接");

    }
    private void setConnMode() {
        RelativeLayout rlNormal=(RelativeLayout)tabView.findViewById(R.id.mouse_init_view);
        rlNormal.setVisibility(View.GONE);
        RelativeLayout rlConn=(RelativeLayout)tabView.findViewById(R.id.mouse_conn_view);
        rlConn.setVisibility(View.VISIBLE);
        //无需启动其他页面

        Button mouseBtn = (Button) tabView.findViewById(R.id.bt_mouse);
        Button shadowBtn = (Button) tabView.findViewById(R.id.bt_shadow);
        Button disconnectBtn = (Button) tabView.findViewById(R.id.bt_exit);
        mouseBtn.setText("进入空中鼠标模式");
        mouseBtn.setEnabled(true);
        shadowBtn.setText("进入手机投影模式");
        disconnectBtn.setText("断开远端会话连接");
    }
    private void setMouseMode() {
        RelativeLayout rlNormal=(RelativeLayout)tabView.findViewById(R.id.mouse_init_view);
        rlNormal.setVisibility(View.GONE);
        RelativeLayout rlConn=(RelativeLayout)tabView.findViewById(R.id.mouse_conn_view);
        rlConn.setVisibility(View.VISIBLE);

        Button mouseBtn = (Button) tabView.findViewById(R.id.bt_mouse);
        Button shadowBtn = (Button) tabView.findViewById(R.id.bt_shadow);
        Button disconnectBtn = (Button) tabView.findViewById(R.id.bt_exit);
        mouseBtn.setText("进入空中鼠标模式");
        mouseBtn.setEnabled(true);
        shadowBtn.setText("进入手机投影模式");
        disconnectBtn.setText("断开远端会话连接");
        Intent intent = new Intent();
        intent.setClass(getContext(), MouseActivity.class);
        this.startActivity(intent);
    }
    private void setShadowMode() {
        RelativeLayout rlNormal=(RelativeLayout)tabView.findViewById(R.id.mouse_init_view);
        rlNormal.setVisibility(View.GONE);
        RelativeLayout rlConn=(RelativeLayout)tabView.findViewById(R.id.mouse_conn_view);
        rlConn.setVisibility(View.VISIBLE);

        Button mouseBtn = (Button) tabView.findViewById(R.id.bt_mouse);
        Button shadowBtn = (Button) tabView.findViewById(R.id.bt_shadow);
        Button disconnectBtn = (Button) tabView.findViewById(R.id.bt_exit);
        mouseBtn.setText("进入空中鼠标模式");
        mouseBtn.setEnabled(false);
        shadowBtn.setText("退出手机投影模式");
        disconnectBtn.setText("断开远端会话连接");
    }
    private void showConnPop(final String tip) {
        final MaterialDialog myDialog = new MaterialDialog(getContext());
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setTitle("是否确认建立连接？");
        myDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action.requestConn(tip);
                myDialog.dismiss();
            }
        });
        myDialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }
    private void loaded(String info) {
        LoaderView loader = (LoaderView) tabView.findViewById(R.id.loader);
        loader.setText(info);
        loader.setVisibility(View.GONE);
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        loader.startAnimation(animation);
    }
    private void loading(String info) {
        LoaderView loader = (LoaderView) tabView.findViewById(R.id.loader);
        loader.setVisibility(View.VISIBLE);
        loader.setText(info);
    }
    private void closeLoader()
    {
        LoaderView loader = (LoaderView) tabView.findViewById(R.id.loader);
        loader.setText("");
        loader.setVisibility(View.GONE);
    }

    public void showInfo(String info) {
        Toast.makeText(MyApplication.getInstance().getApplicationContext(), info, Toast.LENGTH_LONG).show();
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
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
