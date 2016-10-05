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
import android.view.WindowManager;

import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;
import com.baidu.mobstat.StatService;

import ywcai.ls.mobileutil.main.fragment.NetMenuFragment;

public class HomeActivity extends AppCompatActivity {
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitView();
        InstallMenuFragment();
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
    private void InstallMenuFragment()
    {
        NetMenuFragment netWorkFragment=new NetMenuFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.homePage_main,netWorkFragment);
        transaction.commit();
    }
    private void showVersionInfo()
    {
        Intent intent=new Intent();
        intent.setClass(HomeActivity.this, VersionActivity.class);
        this.startActivity(intent);
    }
    private  void showHelpInfo()
    {

    }
    private void showCopyInfo()
    {
        Intent intent=new Intent();
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
}
