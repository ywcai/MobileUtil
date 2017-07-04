package ywcai.ls.module.remote.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.widget.FButton;
import ywcai.ls.common.ComponentStatus;
import ywcai.ls.controls.loader.LoaderView;
import ywcai.ls.mobileutil.R;
import ywcai.ls.common.DeviceInfo;
import ywcai.ls.module.remote.model.DeviceListAdapter;
import ywcai.ls.module.remote.model.LoginUser;
import ywcai.ls.module.remote.presenter.LoginAction;
import ywcai.ls.common.eventbus.RemoteViewUpdate;
import ywcai.ls.util.statics.ResultCode;


public class RemoteAppFragment extends Fragment  {
    public LoginAction LoginActionInf;
    private View view;
    private List<DeviceInfo> list=new ArrayList();
    private DeviceListAdapter deviceListAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepage_remote, container, false);
        InitAction();
        InitEvent();
        return view;
    }
    private void InitAction() {
        LoginActionInf = new LoginAction(getActivity(),getContext());
    }
    private void InitEvent() {
        deviceListAdapter=new DeviceListAdapter(list);
        ListView listView=(ListView)view.findViewById(R.id.device_list);
        listView.setAdapter(deviceListAdapter);
        FButton imageView = (FButton) view.findViewById(R.id.remote_login_qq);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActionInf.LoginIn();
            }
        });
        TextView logOut = (TextView) view.findViewById(R.id.remote_logout);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActionInf.LoginOut();
            }
        });
        TextView add = (TextView) view.findViewById(R.id.remote_add_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加外部设备接入吗
//                createEditWin(0, OperationDeviceType.ADD_OUTSIDE_DEVICE);
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(RemoteViewUpdate remoteViewUpdate){
        switch (remoteViewUpdate.remoteViewUpdateType)
        {
            case LOADING:
                loadIng(remoteViewUpdate.tip);
                break;
            case LOAD_END:
                loaded(remoteViewUpdate.tip);
                break;
            case QQ_LOGIN_OK:
                qqLoginIn((LoginUser) remoteViewUpdate.obj);
                LoginActionInf.ConnServer();
                break;
            case QQ_LOGIN_OUT:
                qqLoginOut();
                break;
            case CONNECT_OK:
                loadIng(remoteViewUpdate.tip);
                LoginActionInf.RegDevice();
                break;
            case CONNECT_FAIL:
                loaded(remoteViewUpdate.tip);
                LoginActionInf.LoginOut();
                break;
            case REG_OK:
                loaded(remoteViewUpdate.tip);
                updateDeviceList((List<DeviceInfo>) remoteViewUpdate.obj);
                break;
            case REG_FAIL:
                loaded(remoteViewUpdate.tip);
                LoginActionInf.LoginOut();
                break;
            case INTERNET_SESSION_CLOSED:
                showToast(remoteViewUpdate.tip);
                break;
            case TURN_ON:
                turnStatus(remoteViewUpdate.tip,ResultCode.device_status_online);
                break;
            case TURN_OFF:
                turnStatus(remoteViewUpdate.tip,ResultCode.device_status_offline);
                break;
            case TURN_BUSY:
                turnStatus(remoteViewUpdate.tip,ResultCode.device_status_busy);
                break;
        }
    }
    private void turnStatus(String tip,String status)
    {
        for (DeviceInfo deviceInfo:list) {
            if(deviceInfo.accessCode.equals(tip))
            {
                deviceInfo.status=status;
                break;
            }
        }
        deviceListAdapter.sortList();
        deviceListAdapter.notifyDataSetChanged();
    }
    public void loadIng(String msg) {
        LoaderView loader = (LoaderView) view.findViewById(R.id.remote_login_loader);
        loader.setVisibility(View.VISIBLE);
        loader.setText(msg);
    }
    public void loaded(String msg) {
        LoaderView loader = (LoaderView) view.findViewById(R.id.remote_login_loader);
        loader.setVisibility(View.INVISIBLE);
        loader.setText(msg);
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        loader.startAnimation(animation);
    }
    public void qqLoginIn(LoginUser loginUser)
    {
        ComponentStatus.getInstance().qqUserId=loginUser.openId;
        RelativeLayout in = (RelativeLayout) view.findViewById(R.id.qq_login_in);
        in.setVisibility(View.VISIBLE);
        RelativeLayout out = (RelativeLayout) view.findViewById(R.id.qq_login_out);
        out.setVisibility(View.GONE);
        CircleImageView logo = (CircleImageView) view.findViewById(R.id.remote_logo);
        Glide.with(this).load(loginUser.headPicUrl).centerCrop().into(logo);
        TextView username = (TextView) view.findViewById(R.id.remote_username);
        username.setText(loginUser.nickName);
        TextView openId = (TextView) view.findViewById(R.id.remote_open_id);
        openId.setText(loginUser.openId);
        ListView listView=(ListView)view.findViewById(R.id.device_list);
        listView.setVisibility(View.VISIBLE);
    }
    public void qqLoginOut()
    {
        RelativeLayout in = (RelativeLayout) view.findViewById(R.id.qq_login_in);
        in.setVisibility(View.GONE);
        RelativeLayout out = (RelativeLayout) view.findViewById(R.id.qq_login_out);
        out.setVisibility(View.VISIBLE);
        ListView listView=(ListView)view.findViewById(R.id.device_list);
        listView.setVisibility(View.GONE);
        TextView accessCode = (TextView) view.findViewById(R.id.device_code);
        accessCode.setText("");
        list.clear();
    }
    private void updateDeviceList(List<DeviceInfo> deviceInfoList) {
        list.addAll(deviceInfoList);
        deviceListAdapter.sortList();
        deviceListAdapter.notifyDataSetChanged();
        setHeadCode();
    }
    private void setHeadCode() {
        TextView accessCode = (TextView) view.findViewById(R.id.remote_code);
        accessCode.setText(list.get(0).accessCode);
    }

    private void showToast(String tip)
    {
        Toast.makeText(getContext(),tip,Toast.LENGTH_LONG).show();
    }

    /*----------------------------------------
   Draw the component
   -----------------------------------------*/
//    private void createEditWin(final int did, final OperationDeviceType op) {
//        final MaterialDialog editDialog = new MaterialDialog(getContext());
//        editDialog.setCanceledOnTouchOutside(true);
//        final EditText contentView = new EditText(getContext());
//        contentView.setTextColor(0xFF7F8C8D);
//        contentView.setTextSize(14);
//        switch (op)
//        {
//            case UPDATE_DEVICE_NAME:
//                editDialog.setTitle("请输入新的设备名称");
//                break;
//            case ADD_OUTSIDE_DEVICE:
//                editDialog.setTitle("请输入外部设备接入码");
//                break;
//            case UPDATE_LOCAL_ACCESS_CODE:
//                editDialog.setTitle("请输入新的设备接入码");
//                break;
//        }
//        editDialog.setContentView(contentView);
//        editDialog.setPositiveButton("确认", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String deviceName = contentView.getText().toString();
//                if (deviceName.length() > 3 && deviceName.length() <= 30) {
//                    editDialog.dismiss();
//                    LoginActionInf.operationDeviceInfo(op, did, deviceName);
//                } else {
//                    Toast.makeText(getContext(), "不允许少于3，大于30字节!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        editDialog.setNegativeButton("放弃", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editDialog.dismiss();
//            }
//        });
//        editDialog.show();
//    }
//
//    private void setupOperation(final int did) {
//        final MaterialDialog mMaterialDialog = new MaterialDialog(getContext());
//        mMaterialDialog.setCanceledOnTouchOutside(true);
//        View view = LayoutInflater.from(getContext())
//                .inflate(R.layout.control_dialog_pop,
//                        null);
//        FButton fb_del = (FButton) view.findViewById(R.id.btn_device_del);
//        FButton fb_edit = (FButton) view.findViewById(R.id.btn_device_edit);
//        FButton fb_conn = (FButton) view.findViewById(R.id.btn_device_conn);
//        FButton fb_cancal = (FButton) view.findViewById(R.id.btn_device_cancal);
//        fb_del.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMaterialDialog.dismiss();
////                LoginActionInf.operationDeviceInfo(OperationDeviceType.DEL, did, null);
//            }
//        });
//        fb_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMaterialDialog.dismiss();
//                createEditWin(did, OperationDeviceType.UPDATE_DEVICE_NAME);
//            }
//        });
//        fb_conn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMaterialDialog.dismiss();
//                LoginActionInf.createCore(did);
//            }
//        });
//        fb_cancal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mMaterialDialog.dismiss();
//            }
//        });
//        mMaterialDialog.setContentView(view).show();
//    }
//
//    private void setupOperationForDisconnect() {
//        final MaterialDialog myDialog = new MaterialDialog(getContext());
//        myDialog.setCanceledOnTouchOutside(true);
//        myDialog.setTitle("断开控制连接");
//        myDialog.setPositiveButton("确认", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginActionInf.disconnectLink();
//                myDialog.dismiss();
//            }
//        });
//        myDialog.setNegativeButton("取消", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myDialog.dismiss();
//            }
//        });
//        myDialog.show();
//    }
//
//    private void setupOperationForCtrl(final int did) {
//        final MaterialDialog myDialog = new MaterialDialog(getContext());
//        myDialog.setCanceledOnTouchOutside(true);
//        myDialog.setTitle("发起控制请求");
//        myDialog.setPositiveButton("确认", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LoginActionInf.requestControl(did);
//                myDialog.dismiss();
//            }
//        });
//        myDialog.setNegativeButton("取消", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myDialog.dismiss();
//            }
//        });
//        myDialog.show();
//    }
//

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
