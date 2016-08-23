package ywcai.ls.assist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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
        loadHelpInfo();
        TextView title_back = (TextView) findViewById(R.id.title_back);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelpActivity.this.finish();
            }
        });
    }
    private void loadHelpInfo()
    {
        List<HashMap<String,String>> list=new ArrayList<>();

        String[] date=this.getResources().getStringArray(R.array.help_update);
        String[] version=this.getResources().getStringArray(R.array.help_version);
        String[] content=this.getResources().getStringArray(R.array.help_content);
        for(int i=0;i<date.length;i++)
        {
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put("version",version[i]);
            hashMap.put("date",date[i]);
            hashMap.put("content",content[i]);
            list.add(hashMap);
        }
        ListViewCompat listViewCompat = (ListViewCompat)findViewById(R.id.list_helpInfo);
        SimpleAdapter simpleAdapter =new SimpleAdapter
                (this,list,R.layout.listview_help,new String[]{"version", "date","content"},new int[]{R.id.help_version,R.id.help_update_date,R.id.help_content});
        listViewCompat.setAdapter(simpleAdapter);
    }
}
