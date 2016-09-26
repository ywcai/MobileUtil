package ywcai.ls.mobileutil.main.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobads.AdSettings;
import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;

import org.json.JSONObject;

import ywcai.ls.inf.FragmentCallBack;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.main.fragment.sub.BleFragment;
import ywcai.ls.mobileutil.main.fragment.sub.GpsFragment;
import ywcai.ls.mobileutil.main.fragment.sub.OrientationFragment;
import ywcai.ls.mobileutil.main.fragment.sub.PingFragment;
import ywcai.ls.mobileutil.main.fragment.sub.SensorFragment;
import ywcai.ls.mobileutil.main.fragment.sub.StationFragment;
import ywcai.ls.mobileutil.main.fragment.sub.WifiFragment;
import ywcai.ls.util.MyConfig;


public class NetActivity extends AppCompatActivity implements FragmentCallBack {
    private final String MSSP_BANER_AD="2875764";
    private ProgressDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        InitView();
        CreateBanner();
        Bundle bundle = this.getIntent().getExtras();
        int pos = bundle.getInt(MyConfig.STR_INTENT_ARGS);
        CreateFragment(pos);
    }
    private void InitView() {
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setTitle("检查更新");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_assist);
        setSupportActionBar(mToolbar);
        TextView back=(TextView)findViewById(R.id.title_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetActivity.this.finish();
            }
        });
    }
    private void setToolTitle(int pos)
    {
        TextView titleText=(TextView)findViewById(R.id.toolbar_assist_title);
        String[] titles=this.getResources().getStringArray(R.array.homepage_menu_title);
        titleText.setText(titles[pos]);
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
                WifiFragment wifiFragment = new WifiFragment(fm,this);
                transaction.replace(R.id.net_main_container, wifiFragment);
                transaction.commit();
                break;
            case 2:
                StationFragment stationFragment = new StationFragment();
                transaction.replace(R.id.net_main_container, stationFragment);
                transaction.commit();
                break;
            case 3:
                GpsFragment gpsFragment = new GpsFragment();
                transaction.replace(R.id.net_main_container, gpsFragment);
                transaction.commit();
                break;
            case 4:
                SensorFragment sensorFragment = new SensorFragment();
                transaction.replace(R.id.net_main_container, sensorFragment);
                transaction.commit();
                break;
            case 5:
                BleFragment bleFragment = new BleFragment();
                transaction.replace(R.id.net_main_container, bleFragment);
                transaction.commit();
                break;
            case 6:
                OrientationFragment orientationFragment= new OrientationFragment();
                transaction.replace(R.id.net_main_container,orientationFragment);
                transaction.commit();
                break;
            case 7:
//              StepFragment stepFragment= new StepFragment();
//              transaction.replace(R.id.net_main_container,stepFragment);
//              transaction.commit();
                break;
            case 8:
                ;
                break;
            case 9:
                ;
                break;
        }
    }
    private void CreateBanner()
    {
        RelativeLayout adContainer=(RelativeLayout)this.findViewById(R.id.ad_banner);
        AdView adView=new AdView(this,MSSP_BANER_AD);
        AdSettings.setKey(new String[]{"baidu", "中国"});
        adContainer.addView(adView);
        adView.setListener(new AdViewListener() {
            @Override
            public void onAdReady(AdView adView) {

            }

            @Override
            public void onAdShow(JSONObject jsonObject) {

            }

            @Override
            public void onAdClick(JSONObject jsonObject) {

            }

            @Override
            public void onAdFailed(String s) {

            }

            @Override
            public void onAdSwitch() {

            }

            @Override
            public void onAdClose(JSONObject jsonObject) {

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void callBackSetTitle(String content) {
        TextView titleText=(TextView)findViewById(R.id.toolbar_assist_title);
        titleText.setText(content);
    }

    @Override
    public void callBackClear(String str) {
    }

    @Override
    public void callBackLoad() {

    }
}
