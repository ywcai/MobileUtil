package ywcai.ls.assist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import ywcai.ls.mobileutil.R;

/**
 * Created by zmy_11 on 2016/8/21.
 */
public class AboutActivity extends AppCompatActivity {
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
        toolbar_title.setText(this.getResources().getString(R.string.menu3));
    }
}
