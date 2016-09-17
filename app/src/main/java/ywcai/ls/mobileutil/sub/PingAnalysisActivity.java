package ywcai.ls.mobileutil.sub;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import ywcai.ls.mobileutil.R;
import ywcai.ls.ui.child.PingAnalysisFragment;
import ywcai.ls.util.MyConfig;


public class PingAnalysisActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_main);
        InitView();
        Bundle bundle = this.getIntent().getExtras();
        ArrayList<Integer> logList=bundle.getIntegerArrayList(MyConfig.STR_INTENT_LIST_ARGS);
        CreateFragment(logList);
    }
    private void InitView() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_assist);
        setSupportActionBar(mToolbar);
        TextView back=(TextView)findViewById(R.id.title_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PingAnalysisActivity.this.finish();
            }
        });
    }
    private void CreateFragment(ArrayList<Integer> logList) {
        Bundle bundle=new Bundle();
        bundle.putIntegerArrayList(MyConfig.STR_INTENT_LIST_ARGS,logList);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        PingAnalysisFragment pingAnalysisFragment = new PingAnalysisFragment();
        pingAnalysisFragment.setArguments(bundle);
        transaction.replace(R.id.net_main_container, pingAnalysisFragment);
        transaction.commit();
        TextView back=(TextView)findViewById(R.id.toolbar_assist_title);
        back.setText("Ping结果分析");;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
