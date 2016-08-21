package ywcai.ls.mobileutil;


import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.baidu.appx.BDBannerAd;
import com.baidu.autoupdatesdk.BDAutoUpdateSDK;
import com.baidu.autoupdatesdk.UICheckUpdateCallback;

import ywcai.ls.mobileutil.R;
import ywcai.ls.ui.FirstFragment;


public class MainActivity extends AppCompatActivity {
    private static final String APP_KEY="4WbdujL7dRcFtP8977XQn3Kv";
    private static final String APP_ID="6892883";
    private ProgressDialog dialog;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        InitView();
        InstallFirstFragment();
        InitBanner();
    }
    private void InitBanner()
    {
        final LinearLayout ll=(LinearLayout)findViewById(R.id.ad_banner);
        final BDBannerAd bdBannerAd=new BDBannerAd(MainActivity.this);
        bdBannerAd.setAdSize(BDBannerAd.SIZE_FLEXIBLE);
        bdBannerAd.setAdListener(new BDBannerAd.BannerAdListener() {
            @Override
            public void onAdvertisementDataDidLoadSuccess() {
                if (ll != null) {
                    ll.addView(bdBannerAd);
                    Toast.makeText(MainActivity.this,"load the BDBannerAd success",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"find the container error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAdvertisementDataDidLoadFailure() {
                Toast.makeText(MainActivity.this,"onAdvertisementDataDidLoadFailure",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdvertisementViewDidShow() {
                Toast.makeText(MainActivity.this,"onAdvertisementViewDidShow",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdvertisementViewDidClick() {
                Toast.makeText(MainActivity.this,"onAdvertisementViewDidClick",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdvertisementViewWillStartNewIntent() {
                Toast.makeText(MainActivity.this,"onAdvertisementViewWillStartNewIntent",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitView() {
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setTitle("检查更新");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
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
                        //BDAutoUpdateSDK.asUpdateAction(context, new MyUICheckUpdateCallback());
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
        return true;
    }

    private class MyUICheckUpdateCallback implements UICheckUpdateCallback {
        @Override
        public void onCheckComplete() {
            dialog.dismiss();
        }
    }
}
