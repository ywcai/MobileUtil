package ywcai.ls.mobileutil;

import android.app.ProgressDialog;
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

import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.baidu.mobads.AdSettings;
import com.baidu.mobads.AdView;
import com.baidu.mobstat.StatService;
import com.mediav.ads.sdk.adcore.Mvad;

import ywcai.ls.mobileutil.main.fragment.NetMenuFragment;
import ywcai.ls.mobileutil.main.fragment.LocalServerScanFragment;

public class HomeActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    private final String MSSP_BANER_AD_ID = "2875764";
    private final String BANER_360_AD_ID = "5P5vpxwPHk";
    private String isAdBaidu = "baidu_ad";//baidu_ad,360_ad,qq_ad
    private int nowSelect=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitView();
        InstallFragment(1);
    }

    private void InitView() {
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setTitle(R.string.menu1);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.nav);
        BDAutoUpdateSDK.uiUpdateAction(this, new MyUICheckUpdateCallback());
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu1:
                        dialog.show();
                        BDAutoUpdateSDK.uiUpdateAction(HomeActivity.this, new MyUICheckUpdateCallback());
                        break;
                    case R.id.menu2:
                        showVersionInfo();
                        break;
                    case R.id.menu3:
                        showHelpInfo();
                        break;
                    case R.id.menu4:
                        showCopyInfo();
                        break;
                }
                return false;
            }
        });
        if (isAdBaidu.equals("baidu_ad")) {
            CreateBaiduBanner();
        }
        if (isAdBaidu.equals("360_ad")) {
            Create360Banner();
        }
        if (isAdBaidu.equals("qq_ad")) {
            CreateQQBanner();
        }
        TextView tv_del = (TextView) findViewById(R.id.tv_del_ad);
        tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout adContainer = (RelativeLayout) findViewById(R.id.ad_banner);
                adContainer.setVisibility(View.GONE);
                v.setVisibility(View.GONE);
            }
        });

        TextView nav_1=(TextView)findViewById(R.id.bot_nav_1);
        TextView nav_2=(TextView)findViewById(R.id.bot_nav_2);
        TextView nav_3=(TextView)findViewById(R.id.bot_nav_3);
        TextView nav_4=(TextView)findViewById(R.id.bot_nav_4);

        nav_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InstallFragment(1);
            }
        });
        nav_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InstallFragment(2);
            }
        });

        nav_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InstallFragment(3);
            }
        });

        nav_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InstallFragment(4);
            }
        });


    }



    private void InstallFragment(int nowPage) {
        TextView nav_1=(TextView)findViewById(R.id.bot_nav_1);
        TextView nav_2=(TextView)findViewById(R.id.bot_nav_2);
        TextView nav_3=(TextView)findViewById(R.id.bot_nav_3);
        TextView nav_4=(TextView)findViewById(R.id.bot_nav_4);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (nowPage)
        {
            case 1:
                if(nowSelect!=1) {
                    NetMenuFragment netWorkFragment = new NetMenuFragment();
                    transaction.replace(R.id.homePage_main, netWorkFragment);
                    nowSelect=1;
                    nav_1.setTextColor(0xFF3eb875);
                    nav_1.setTextSize(14);
                    nav_2.setTextColor(0xFF666967);
                    nav_2.setTextSize(12);
                    nav_3.setTextColor(0xFF666967);
                    nav_3.setTextSize(12);
                    nav_4.setTextColor(0xFF666967);
                    nav_4.setTextSize(12);
                }
                break;
            case 2:
                if(nowSelect!=2) {
                    LocalServerScanFragment localServerScanFragment = new LocalServerScanFragment();
                    transaction.replace(R.id.homePage_main, localServerScanFragment);
                    nowSelect=2;
                    nav_1.setTextColor(0xFF666967);
                    nav_1.setTextSize(12);
                    nav_2.setTextColor(0xFF3eb875);
                    nav_2.setTextSize(14);
                    nav_3.setTextColor(0xFF666967);
                    nav_3.setTextSize(12);
                    nav_4.setTextColor(0xFF666967);
                    nav_4.setTextSize(12);
                }
                break;
            case 3:
                if(nowSelect!=3) {
//                    NetMenuFragment netWorkFragment = new NetMenuFragment();
//                    transaction.replace(R.id.homePage_main, netWorkFragment);
                    nowSelect=3;
                    nav_1.setTextColor(0xFF666967);
                    nav_2.setTextSize(12);
                    nav_2.setTextColor(0xFF666967);
                    nav_2.setTextSize(12);
                    nav_3.setTextColor(0xFF3eb875);
                    nav_3.setTextSize(14);
                    nav_4.setTextColor(0xFF666967);
                    nav_4.setTextSize(12);
                }
                break;
            case 4:
                if(nowSelect!=4) {
//                    NetMenuFragment netWorkFragment = new NetMenuFragment();
//                    transaction.replace(R.id.homePage_main, netWorkFragment);
                    nowSelect=4;
                    nav_1.setTextColor(0xFF666967);
                    nav_2.setTextSize(12);
                    nav_2.setTextColor(0xFF666967);
                    nav_2.setTextSize(12);
                    nav_3.setTextColor(0xFF666967);
                    nav_3.setTextSize(12);
                    nav_4.setTextColor(0xFF3eb875);
                    nav_4.setTextSize(14);
                }
                break;
        }
        transaction.commit();
    }

    private void showVersionInfo() {
        Intent intent = new Intent();
        intent.setClass(HomeActivity.this, VersionActivity.class);
        this.startActivity(intent);
    }

    private void showHelpInfo() {

    }

    private void showCopyInfo() {
        Intent intent = new Intent();
        intent.setClass(HomeActivity.this, AboutActivity.class);
        this.startActivity(intent);
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

    private void CreateBaiduBanner() {

        RelativeLayout adContainer = (RelativeLayout) this.findViewById(R.id.ad_banner);
        AdView adView = new AdView(this, MSSP_BANER_AD_ID);
        AdSettings.setKey(new String[]{"baidu", "中国"});
        adContainer.addView(adView);
    }

    private void Create360Banner() {

        RelativeLayout adContainer = (RelativeLayout) this.findViewById(R.id.ad_banner);
        Mvad.showBanner(adContainer, this, BANER_360_AD_ID, false);
    }

    private void CreateQQBanner() {
        RelativeLayout adContainer = (RelativeLayout) this.findViewById(R.id.ad_banner);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
