package ywcai.ls.mobileutil;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.baidu.mobads.AdSettings;
import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
;
import org.json.JSONObject;

import ywcai.ls.assist.AboutActivity;
import ywcai.ls.assist.HelpActivity;
import ywcai.ls.ui.FirstFragment;




public class MainActivity extends AppCompatActivity {
    private final String MSSP_BANNER_AD="2875764";
    private  final String APPID="8569145";
    private  final String APPKEY="qQFsduOTaU2681KsOGcY0bVhgv4rInN6";
    private ProgressDialog dialog;
    private Context context;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        InitView();
        LoadMsspBanner();
        InstallFirstFragment();
//        InitSplash();
//        InitBanner();
    }
    private void LoadMsspBanner()
    {
        adView = new AdView(this,MSSP_BANNER_AD);
        AdSettings.setKey(new String[]{"baidu", "中国"});
        RelativeLayout rl=(RelativeLayout)findViewById(R.id.ad_banner);
        rl.addView(adView);
//        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.WRAP_CONTENT);
//        rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        // 创建广告View
        // 设置监听器
        adView.setListener(new AdViewListener() {
            @Override
            public void onAdReady(AdView adView) {
                //rl.setVisibility(View.VISIBLE);
                //Toast.makeText(context, "onAdReady", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdShow(JSONObject jsonObject) {
                //Toast.makeText(context, "onAdShow", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClick(JSONObject jsonObject) {
                //Toast.makeText(context, "onAdClick", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailed(String s) {
                //Toast.makeText(context, "onAdFailed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdSwitch() {
                //Toast.makeText(context, "onAdSwitch", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClose(JSONObject jsonObject) {
                //Toast.makeText(context, "onAdClose", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitView() {
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setTitle("检查更新");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.nav);
        BDAutoUpdateSDK.asUpdateAction(context, new MyUICheckUpdateCallback());
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //InstallFirstFragment();
                switch (item.getItemId()) {
                    case R.id.menu1:
                        dialog.show();
                        BDAutoUpdateSDK.asUpdateAction(context, new MyUICheckUpdateCallback());
                        break;
                    case R.id.menu2:
                        showHelpInfo();
                        break;
                    case R.id.menu3:
                        showCopyInfo();
                        break;
                }
                return false;
            }
        });
    }

    private void InstallFirstFragment()
    {

        FirstFragment ff=new FirstFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_container, ff);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return true;
    }

    private class MyUICheckUpdateCallback implements UICheckUpdateCallback {
        @Override
        public void onCheckComplete() {
            dialog.dismiss();
        }
    }

    private void showHelpInfo()
    {
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, HelpActivity.class);
        this.startActivity(intent);
    }
    private void showCopyInfo()
    {
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, AboutActivity.class);
        this.startActivity(intent);
    }
}
