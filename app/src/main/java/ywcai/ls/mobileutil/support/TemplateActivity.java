package ywcai.ls.mobileutil.support;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.inf.CallBackMainTitle;
import ywcai.ls.module.components.ble.view.BleFragment;
import ywcai.ls.module.components.gps.GpsFragment;
import ywcai.ls.module.components.lan.view.LanFragment;
import ywcai.ls.module.components.orientation.OrientationFragment;
import ywcai.ls.module.components.ping.operation.view.PingFragment;
import ywcai.ls.module.components.sensor.view.SensorFragment;
import ywcai.ls.module.components.station.StationFragment;
import ywcai.ls.module.components.wifi.view.WifiFragment;
import ywcai.ls.util.MyConfig;


public class TemplateActivity extends AppCompatActivity implements CallBackMainTitle {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        InitView();
        Bundle bundle = this.getIntent().getExtras();
        int pos = bundle.getInt(MyConfig.STR_INTENT_ARGS);
        CreateFragment(pos);
    }
    private void InitView() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_assist);
        setSupportActionBar(mToolbar);
        TextView back=(TextView)findViewById(R.id.title_back);
        if (back != null) {
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TemplateActivity.this.finish();
                }
            });
        }
    }

    private void setToolTitle(int pos)
    {
        TextView titleText=(TextView)findViewById(R.id.toolbar_assist_title);
        String[] titles=this.getResources().getStringArray(R.array.homepage_menu_title);
        if (titleText != null) {
            titleText.setText(titles[pos]);
        }
    }
    private void CreateFragment(int pos) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        setToolTitle(pos);
        switch (pos) {
            case 0:
                PingFragment pingFragment = new PingFragment();
                transaction.replace(R.id.net_main_container, pingFragment);
                transaction.commit();
                break;
            case 1:
                LanFragment lanFragment= new LanFragment();
                transaction.replace(R.id.net_main_container,lanFragment);
                transaction.commit();
                break;
            case 2:
                WifiFragment wifiFragment = new WifiFragment();
                transaction.replace(R.id.net_main_container, wifiFragment);
                transaction.commit();
                break;
            case 9:

                break;
            case 3:
                StationFragment stationFragment = new StationFragment();
                transaction.replace(R.id.net_main_container, stationFragment);
                transaction.commit();
                break;
            case 4:
                GpsFragment gpsFragment = new GpsFragment();
                transaction.replace(R.id.net_main_container, gpsFragment);
                transaction.commit();
                break;
            case 5:
                BleFragment bleFragment = new BleFragment();
                transaction.replace(R.id.net_main_container, bleFragment);
                transaction.commit();
                break;
            case 6:
                SensorFragment sensorFragment = new SensorFragment();
                transaction.replace(R.id.net_main_container, sensorFragment);
                transaction.commit();
                break;
            case 7:
                OrientationFragment orientationFragment= new OrientationFragment();
                transaction.replace(R.id.net_main_container,orientationFragment);
                transaction.commit();
                break;
            case 8:

                break;
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void callBackSetTitle(String content) {
        TextView titleText=(TextView)findViewById(R.id.toolbar_assist_title);
        if (titleText != null) {
            titleText.setText(content);
        }
    }
}
