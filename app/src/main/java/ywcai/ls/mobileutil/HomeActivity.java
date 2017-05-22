package ywcai.ls.mobileutil;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.baidu.mobstat.StatService;
import com.mediav.ads.sdk.adcore.Mvad;
import com.mediav.ads.sdk.interfaces.IMvBannerAd;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.Tencent;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.mobileutil.support.AboutActivity;
import ywcai.ls.mobileutil.support.FirstMenuFragment;
import ywcai.ls.mobileutil.support.HelpActivity;
import ywcai.ls.mobileutil.support.VersionActivity;
import ywcai.ls.module.mouse.view.LocalServerScanFragment;
import ywcai.ls.module.remote.login.presenter.RestfulAction;
import ywcai.ls.module.remote.login.presenter.inf.LoginActionInf;
import ywcai.ls.module.remote.login.view.RemoteAppFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog dialog;
    private int currentPage=3;
    private List<Fragment> fragments=new ArrayList<>();
    private List<TextView> nav=new ArrayList<>();
    private RestfulAction loginActionInf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitView();
        InstallFragment();
        selectFragment(0);
        InstallAdView();
    }

    private void InstallAdView() {
        String adSpaceid="5P5vpxwPHk";
        RelativeLayout adContainer=(RelativeLayout)findViewById(R.id.homePage_adv);
        IMvBannerAd bannerad = Mvad.showBanner(adContainer, HomeActivity.this, adSpaceid, false);
    }

    private void InstallFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        FirstMenuFragment netWorkFragment=new FirstMenuFragment();
        LocalServerScanFragment localServerScanFragment = new LocalServerScanFragment();
        RemoteAppFragment remoteControlFragment = new RemoteAppFragment();
        Fragment reserveFragment=new Fragment();
        transaction.add(R.id.homePage_main,netWorkFragment);
        transaction.add(R.id.homePage_main,localServerScanFragment);
        transaction.add(R.id.homePage_main,remoteControlFragment);
        transaction.add(R.id.homePage_main,reserveFragment);
        fragments.add(netWorkFragment);
        fragments.add(localServerScanFragment);
        fragments.add(remoteControlFragment);
        fragments.add(reserveFragment);
        transaction.hide(netWorkFragment);
        transaction.hide(localServerScanFragment);
        transaction.hide(remoteControlFragment);
        transaction.hide(reserveFragment);
        transaction.commit();
        TextView nav_1 = (TextView) findViewById(R.id.bot_nav_1);
        TextView nav_2 = (TextView) findViewById(R.id.bot_nav_2);
        TextView nav_3 = (TextView) findViewById(R.id.bot_nav_3);
        TextView nav_4 = (TextView) findViewById(R.id.bot_nav_4);
        nav_1.setOnClickListener(this);
        nav_2.setOnClickListener(this);
        nav_3.setOnClickListener(this);
//        nav_4.setOnClickListener(this);
        nav.add(nav_1);
        nav.add(nav_2);
        nav.add(nav_3);
        nav.add(nav_4);
        nav_1.setTextColor(0xFF666967);
        nav_1.setTextSize(12);
        nav_2.setTextColor(0xFF666967);
        nav_2.setTextSize(12);
        nav_3.setTextColor(0xFF666967);
        nav_3.setTextSize(12);
        nav_4.setTextColor(0xFF666967);
        nav_4.setTextSize(12);
    }
    private void selectFragment(int selectPage) {
        if(selectPage!=currentPage)
        {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.hide(fragments.get(currentPage));
            transaction.show(fragments.get(selectPage));
            transaction.commit();
            nav.get(currentPage).setTextColor(0xFF666967);
            nav.get(selectPage).setTextColor(0xFF3eb875);
            nav.get(currentPage).setTextSize(12);
            nav.get(selectPage).setTextSize(14);
            currentPage=selectPage;
        }
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
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bot_nav_1:
                selectFragment(0);
                break;
            case R.id.bot_nav_2:
                selectFragment(1);
                break;
            case R.id.bot_nav_3:
                selectFragment(2);
                break;
            case R.id.bot_nav_4:
                selectFragment(3);
                break;
        }
    }



    private void showVersionInfo() {
        Intent intent = new Intent();
        intent.setClass(HomeActivity.this, VersionActivity.class);
        this.startActivity(intent);
    }

    private void showHelpInfo() {
        Intent intent = new Intent();
        intent.setClass(HomeActivity.this, HelpActivity.class);
        this.startActivity(intent);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        PackageManager pm = getPackageManager();
        ResolveInfo homeInfo = pm.resolveActivity(
                      new Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_HOME), 0);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ActivityInfo ai = homeInfo.activityInfo;
            Intent startIntent = new Intent(Intent.ACTION_MAIN);
            startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(startIntent);
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== Constants.REQUEST_LOGIN) {
            RemoteAppFragment fragment = (RemoteAppFragment) fragments.get(2);
            loginActionInf = fragment.getLoginInf();
            Tencent.onActivityResultData(requestCode,resultCode,data,loginActionInf.getListener());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
