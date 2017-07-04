package ywcai.ls.module.tools.ble.presenter;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.ListViewCompat;

import android.view.View;

import android.widget.TextView;

import java.util.concurrent.CopyOnWriteArrayList;

import ywcai.ls.module.tools.ble.presenter.adapter.BleAdapter;
import ywcai.ls.module.tools.ble.model.BleInfo;
import ywcai.ls.mobileutil.R;
import ywcai.ls.util.statics.MyConfig;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class Ble extends BroadcastReceiver implements BluetoothAdapter.LeScanCallback {
    private CopyOnWriteArrayList<BleInfo> bleList;
    private View tabView;
    private ListViewCompat listViewCompat;
    private Context context;
    private BleAdapter bleAdapter;
    private BluetoothAdapter bleA;
    private TextView bleTitle;
    private BleHandler bleHandle;


    public Ble(View view,Context pContext) {
        context=pContext;
        InitView(view);
        InitBroadCast();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            InitBLE();
        } else {
            bleTitle.setText("系统版本太低，不支持蓝牙！");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                reFresh();
            }
        }).start();
    }

    private void InitView(View view) {
        bleHandle = new BleHandler();
        tabView = view;
        listViewCompat = (ListViewCompat) tabView.findViewById(R.id.now_ble);
        bleList = new CopyOnWriteArrayList<>();
        bleAdapter = new BleAdapter(bleList);
        listViewCompat.setAdapter(bleAdapter);
        bleTitle = (TextView) tabView.findViewById(R.id.tv_ble_title);
    }


    private void InitBroadCast() {
        IntentFilter filter_enable_status = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter_enable_status.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        context.registerReceiver(this, filter_enable_status);
    }

    private boolean checkRequestLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (context.getPackageManager().checkPermission(Manifest.permission.BLUETOOTH, "ywcai.ls.mobileutil")
                == PackageManager.PERMISSION_GRANTED
                && context.getPackageManager().checkPermission(Manifest.permission.BLUETOOTH_ADMIN, "ywcai.ls.mobileutil")
                == PackageManager.PERMISSION_GRANTED
                && context.getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, "ywcai.ls.mobileutil")
                == PackageManager.PERMISSION_GRANTED
                && context.getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, "ywcai.ls.mobileutil")
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        bleTitle.setText("你没有授予应用权限");
        return false;
    }

    private void InitBLE() {
        if (!checkRequestLocation()) {
            return;
        }
        bleA = BluetoothAdapter.getDefaultAdapter();
        if (bleA == null) {
            bleTitle.setText("该手机不支持蓝牙");
        } else {
            if (!bleA.isEnabled()) {
                bleA.enable();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ScanBle();
                } else {
                    ScanOld();
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void ScanOld() {
        bleA.startLeScan(this);
        bleTitle.setText("Android 5.0-,仅支持蓝牙4.0协议，持续扫描中......");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ScanBle() {
        BluetoothLeScanner scanner = bleA.getBluetoothLeScanner();
        ScanCallback leCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                BleInfo newBle = new BleInfo();
                newBle.mac = result.getDevice().getAddress();
                newBle.device = result.getDevice().getName();
                newBle.dbm = result.getRssi();
                newBle.changeFrequency = 0;
                processResult(newBle);
            }
        };
        scanner.startScan(leCallback);
        bleTitle.setText("Android 5.0+,仅支持蓝牙4.0协议，持续扫描中......");
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        BleInfo newBle = new BleInfo();
        newBle.mac = device.getAddress();
        newBle.device = device.getName();
        newBle.dbm = rssi;
        newBle.changeFrequency = 0;
        processResult(newBle);
    }

    private void ShowTip() {
        if (bleA.getState() == BluetoothAdapter.STATE_ON) {
            bleTitle.setText("检测到蓝牙模块开启！");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ScanBle();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                ScanOld();
            } else {
                bleTitle.append("\n系统版本太低，不支持蓝牙");
            }
        }
        if (bleA.getState() == BluetoothAdapter.STATE_OFF) {
            bleTitle.setText("检测到蓝牙模块关闭!");
            bleList.clear();
            bleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
            ShowTip();
        }
    }

    private void processResult(BleInfo newInfo) {
        for (BleInfo bleInfo : bleList) {
            if (newInfo.mac.equals(bleInfo.mac)) {
                bleInfo.dbm = newInfo.dbm;
                bleInfo.changeFrequency = bleInfo.changeFrequency + 1;
                return;
            }
        }
        bleList.add(newInfo);
    }

    private void reFresh() {
        while (true) {
            try {
                Thread.sleep(MyConfig.INT_BLE_PRINT_INFO_REFRESH);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            reFreshUI();
        }
    }

    private void reFreshUI() {
        bleHandle.sendEmptyMessage(0);
    }

    private void checkCache() {
        for (BleInfo bleInfo : bleList) {
            if (bleInfo.dbm < -80 && bleInfo.changeFrequency == 0) {
                bleList.remove(bleInfo);
            }
            bleInfo.changeFrequency = 0;
        }
    }

    class BleHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            {
                checkCache();
                TextView tv_noBle=(TextView)tabView.findViewById(R.id.tv_has_no);
                if(bleList.size()==0)
                {

                    tv_noBle.setVisibility(View.VISIBLE);
                }
                else
                {
                    tv_noBle.setVisibility(View.GONE);
                }
                bleAdapter.notifyDataSetChanged();
                super.handleMessage(msg);
            }
        }
    }
}
