package ywcai.ls.mobileutil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baidu.mobstat.StatService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zmy_11 on 2016/8/21.
 */
public class VersionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        loadHelpInfo();
        TextView title_back = (TextView) findViewById(R.id.title_back);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VersionActivity.this.finish();
            }
        });
        TextView toolbar_title = (TextView) findViewById(R.id.toolbar_assist_title);
        toolbar_title.setText(R.string.menu2);
    }
    private void loadHelpInfo()
    {
        List<HashMap<String,String>> list=new ArrayList<>();
        String[] date=this.getResources().getStringArray(R.array.version_date);
        String[] version=this.getResources().getStringArray(R.array.version_num);
        String[] content=this.getResources().getStringArray(R.array.version_detail);
        for(int i=0;i<date.length;i++)
        {
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("num",version[i]);
            hashMap.put("date",date[i]);
            hashMap.put("detail",content[i]);
            list.add(hashMap);
        }
        ListViewCompat listViewCompat = (ListViewCompat)findViewById(R.id.list_versionInfo);
        SimpleAdapter simpleAdapter =new SimpleAdapter
                (this,list,R.layout.listview_version,new String[]{"num", "date","detail"},new int[]{R.id.version_num,R.id.version_date,R.id.version_content});
        listViewCompat.setAdapter(simpleAdapter);
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
