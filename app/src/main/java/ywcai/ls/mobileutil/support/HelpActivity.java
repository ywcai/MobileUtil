package ywcai.ls.mobileutil.support;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baidu.mobstat.StatService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ywcai.ls.mobileutil.R;

/**
 * Created by zmy_11 on 2016/8/21.
 */
public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        TextView title_back = (TextView) findViewById(R.id.title_back);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpActivity.this.finish();
            }
        });
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_assist_title);
        toolbar_title.setText(R.string.menu3);
        loadHtml();
    }
    private void loadHtml()
    {
        WebView webView=(WebView)findViewById(R.id.web_container);
        WebSettings wSet = webView.getSettings();
        webView.loadUrl("file:///android_asset/help.html");
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
}
