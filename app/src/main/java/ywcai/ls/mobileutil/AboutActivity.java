package ywcai.ls.mobileutil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.mediav.ads.sdk.adcore.Mvad;

/**
 * Created by zmy_11 on 2016/8/21.
 */
public class AboutActivity extends AppCompatActivity {
    private final String BANER_360_AD_ID = "5P5vpxwPHk";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView title_back = (TextView) findViewById(R.id.title_back);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.this.finish();
            }
        });
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_assist_title);
        toolbar_title.setText(R.string.menu4);
        Create360Banner();
    }
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    private void Create360Banner() {
        RelativeLayout adContainer = (RelativeLayout) this.findViewById(R.id.ad_container_360);
        Mvad.showBanner(adContainer, this, BANER_360_AD_ID,false);
    }
}
