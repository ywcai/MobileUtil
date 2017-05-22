package ywcai.ls.module.remote.login.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.widget.FButton;
import me.drakeet.materialdialog.MaterialDialog;
import ywcai.ls.controls.loader.LoaderView;
import ywcai.ls.controls.popwindow.PopView;
import ywcai.ls.controls.popwindow.inf.OnWindowEventListener;
import ywcai.ls.controls.popwindow.inf.PopWinEvent;
import ywcai.ls.controls.pull.LsPullView;
import ywcai.ls.mobileutil.R;
import ywcai.ls.module.mouse.lsenum.OnlineType;
import ywcai.ls.module.mouse.model.ComponentStatus;
import ywcai.ls.module.remote.login.model.DeviceInfo;
import ywcai.ls.module.remote.login.model.LoginType;
import ywcai.ls.module.remote.login.model.LoginUser;
import ywcai.ls.module.remote.login.model.OperationDeviceType;
import ywcai.ls.module.remote.login.presenter.RestfulAction;
import ywcai.ls.module.remote.login.presenter.inf.LoginActionInf;
import ywcai.ls.module.remote.login.presenter.inf.UpdateViewInf;


public class RemoteAppFragment extends Fragment implements UpdateViewInf {
    private RestfulAction loginActionInf;
    private View view;
    private List devices;
    private int nowPos = 0;
    private SimpleAdapter sAdapter;
    private LsPullView lsPullView;
    private ComponentStatus status = ComponentStatus.getInstance();

    //for the home activity invoked
    public RestfulAction getLoginInf() {
        return loginActionInf;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepage_remote, container, false);
        Init();
        InitView();
        InitEvent();
        return view;
    }
    /*----------------------------------------
    Ui init
    -----------------------------------------*/
    private void Init() {
        loginActionInf = new RestfulAction(this);
    }
    private void InitView() {
        devices = new ArrayList();
        sAdapter = new SimpleAdapter(this.getContext(),
                devices,
                R.layout.listview_remote_device,
                new String[]{"deviceName", "deviceID", "deviceStatus"},
                new int[]{R.id.device_name, R.id.device_id, R.id.device_status});
        ;
        lsPullView = (LsPullView) view.findViewById(R.id.device_list);
        lsPullView.setCanPull(false);
        lsPullView.setEmptyString("下拉刷新设备ID");
        lsPullView.setAdapter(sAdapter, devices);
        lsPullView.InjectionTask(loginActionInf);
    }
    private void InitEvent() {
        FButton imageView = (FButton) view.findViewById(R.id.remote_login_qq);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadIng("Loading");
                loginActionInf.login(LoginType.QQ, "", "");
            }
        });
        TextView logOut = (TextView) view.findViewById(R.id.remote_logout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadIng("Loading");
                loginActionInf.loginOut(LoginType.QQ, "", "");
            }
        });
        TextView add = (TextView) view.findViewById(R.id.remote_add_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.onlineType == OnlineType.offline) {
                    createEditWin(0, OperationDeviceType.ADD);
                }
            }
        });

    }
    /*----------------------------------------
    the normal tip
    -----------------------------------------*/
    @Override
    public Activity getMyActivity() {
        return getActivity();
    }

    @Override
    public Context getMyContext() {
        return getContext();
    }

    @Override
    public void showInfo(String msg) {
        LoaderView loader = (LoaderView) view.findViewById(R.id.remote_login_loader);
        loader.setText(msg);
    }
    @Override
    public void loadIng(String msg) {
        LoaderView loader = (LoaderView) view.findViewById(R.id.remote_login_loader);
        loader.setVisibility(View.VISIBLE);
        loader.setText(msg);
    }
    @Override
    public void loaded(String msg) {
        LoaderView loader = (LoaderView) view.findViewById(R.id.remote_login_loader);
        loader.setVisibility(View.INVISIBLE);
        loader.setText(msg);
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        loader.startAnimation(animation);
    }
    @Override
    public void loginEnd(boolean result, LoginUser userInfo) {
        if (result) {
            RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.re_add_btn);
            rl.setVisibility(View.VISIBLE);
            CircleImageView logo = (CircleImageView) view.findViewById(R.id.remote_logo);
            Glide.with(this).load(userInfo.headPicUrl).centerCrop().into(logo);
            TextView logOut = (TextView) view.findViewById(R.id.remote_logout);
            logOut.setVisibility(View.VISIBLE);
            RelativeLayout rl_logSuccess = (RelativeLayout) view.findViewById(R.id.remote_login_in);
            rl_logSuccess.setVisibility(View.VISIBLE);
            RelativeLayout rl_logout = (RelativeLayout) view.findViewById(R.id.remote_login_ui);
            rl_logout.setVisibility(View.GONE);
            TextView username = (TextView) view.findViewById(R.id.remote_username);
            username.setText(userInfo.nickName);
            lsPullView.ToLoadPosition(0);
            lsPullView.setCanPull(true);
        } else {
            CircleImageView logo = (CircleImageView) view.findViewById(R.id.remote_logo);
            logo.setImageDrawable(ContextCompat.getDrawable(getMyContext(), R.drawable.log2));
            TextView logOut = (TextView) view.findViewById(R.id.remote_logout);
            logOut.setVisibility(View.GONE);
            RelativeLayout rl_logSuccess = (RelativeLayout) view.findViewById(R.id.remote_login_in);
            rl_logSuccess.setVisibility(View.GONE);
            RelativeLayout rl_logout = (RelativeLayout) view.findViewById(R.id.remote_login_ui);
            rl_logout.setVisibility(View.VISIBLE);
            TextView username = (TextView) view.findViewById(R.id.remote_username);
            username.setText("未登录");
            devices.clear();
            sAdapter.notifyDataSetChanged();
        }
    }

    /*----------------------------------------
    update the http response
    -----------------------------------------*/
    @Override
    public void createEnd(boolean result, String err) {
        //create socket end
    }

    @Override
    public void updateDeviceList(List<DeviceInfo> list) {

    }

    @Override
    public void addDeviceSuccess(DeviceInfo deviceInfo) {
        HashMap hs = new HashMap();
        hs.put("deviceName", deviceInfo.deviceName);
        hs.put("deviceID", deviceInfo.deviceID + "");
        hs.put("deviceStatus", deviceInfo.deviceStatus);
        devices.add(hs);
        sAdapter.notifyDataSetChanged();
    }

    @Override
    public void delDeviceSuccess() {
        devices.remove(nowPos);
        sAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateDevice(DeviceInfo deviceInfo) {
        ((HashMap) devices.get(nowPos)).put("deviceName", deviceInfo.deviceName);
        sAdapter.notifyDataSetChanged();
    }
    @Override
    public void updateDevList() {
        redirectOnline();
    }
    @Override
    public void clickList(int pos) {
        nowPos = pos;
        HashMap hs = (HashMap) devices.get(nowPos);
        String temp = hs.get("deviceID").toString();
        int did = Integer.parseInt(temp);
        status.selectDeviceID = did;
        status.selectDeviceName = hs.get("deviceName").toString();
        switch (status.onlineType) {
            case offline:
                if (nowPos >= 0 && ((HashMap) devices.get(nowPos)).get("deviceStatus").equals("Free")) {
                    setupOperation(did);
                }
                break;
            case online:
                if (nowPos > 0 && ((HashMap) devices.get(nowPos)).get("deviceStatus").equals("OnLine")) {
                    setupOperationForCtrl(did);
                }
                break;
            case link:
                if (nowPos == 0) {
                    setupOperationForDisconnect();
                }
                break;
        }
    }
    @Override
    public void turnOnDev(DeviceInfo deviceInfo) {
        HashMap hs=new HashMap();
        hs.put("deviceName", deviceInfo.deviceName);
        hs.put("deviceID", deviceInfo.deviceID + "");
        hs.put("deviceStatus", deviceInfo.deviceStatus);
        devices.add(1,hs);
        sAdapter.notifyDataSetChanged();
        sortDev();
    }

    @Override
    public void turnOffDev(String deviceID) {
        int pos=0;
        for(int n=0;n<devices.size();n++)
        {
            HashMap temp= (HashMap) devices.get(n);
            if(temp.get("deviceID").equals(deviceID))
            {
                pos=n;
                break;
            }
        }
        devices.remove(pos);
        sAdapter.notifyDataSetChanged();
    }

    @Override
    public void linkUpDev(String deviceID) {
        for(int n=0;n<devices.size();n++)
        {
            HashMap temp= (HashMap) devices.get(n);
            if(temp.get("deviceID").equals(deviceID))
            {
                if(n==0) {
                    temp.put("deviceStatus", "本机-Busy");
                }
                else
                {
                    temp.put("deviceStatus", "Busy");
                }
            }
        }
        sortDev();
        sAdapter.notifyDataSetChanged();
    }

    @Override
    public void linkDownDev(String deviceID) {
        for(int n=0;n<devices.size();n++)
        {
            HashMap temp= (HashMap) devices.get(n);
            if(temp.get("deviceID").equals(deviceID))
            {
                if(n==0) {
                    temp.put("deviceStatus", "本机-OnLine");
                }
                else {
                    temp.put("deviceStatus", "OnLine");
                }
                break;
            }
        }
        sortDev();
        sAdapter.notifyDataSetChanged();
    }


    /*----------------------------------------
   Draw the component
   -----------------------------------------*/
    private void createEditWin(final int did, final OperationDeviceType op) {
        final MaterialDialog editDialog = new MaterialDialog(getContext());
        editDialog.setCanceledOnTouchOutside(true);
        final EditText contentView = new EditText(getContext());
        contentView.setTextColor(0xFF7F8C8D);
        if (op == OperationDeviceType.UPDATE) {
            editDialog.setTitle("更新设备名称");
        } else {
            editDialog.setTitle("添加新设备");
        }
        editDialog.setContentView(contentView);
        editDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deviceName = contentView.getText().toString();
                if (deviceName.length() > 3 && deviceName.length() <= 30) {
                    editDialog.dismiss();
                    loginActionInf.operationDeviceInfo(op, did, deviceName);
                } else {
                    Toast.makeText(getContext(), "不允许少于3，大于30字节!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        editDialog.setNegativeButton("放弃", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog.dismiss();
            }
        });
        editDialog.show();
    }

    private void setupOperation(final int did) {
        final MaterialDialog mMaterialDialog = new MaterialDialog(getContext());
        mMaterialDialog.setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.control_dialog_pop,
                        null);
        FButton fb_del = (FButton) view.findViewById(R.id.btn_device_del);
        FButton fb_edit = (FButton) view.findViewById(R.id.btn_device_edit);
        FButton fb_conn = (FButton) view.findViewById(R.id.btn_device_conn);
        FButton fb_cancal = (FButton) view.findViewById(R.id.btn_device_cancal);
        fb_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
                loginActionInf.operationDeviceInfo(OperationDeviceType.DEL, did, null);
            }
        });
        fb_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
                createEditWin(did, OperationDeviceType.UPDATE);
            }
        });
        fb_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
                loginActionInf.createCore(did);
            }
        });
        fb_cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMaterialDialog.dismiss();
            }
        });
        mMaterialDialog.setContentView(view).show();
    }

    private void setupOperationForDisconnect() {
        final MaterialDialog myDialog = new MaterialDialog(getContext());
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setTitle("断开控制连接");
        myDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginActionInf.disconnectLink();
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

    private void setupOperationForCtrl(final int did) {
        final MaterialDialog myDialog = new MaterialDialog(getContext());
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.setTitle("发起控制请求");
        myDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginActionInf.requestControl(did);
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

    /*----------------------------------------
    change the User Login status
    -----------------------------------------*/
    private void redirectOnline() {
        lsPullView.setCanPull(false);
        lsPullView.ToLoadPosition(0);
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.re_add_btn);
        rl.setVisibility(View.GONE);
        nowPos = 0;
    }

    private void sortDev() {

    }

}
