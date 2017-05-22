package ywcai.ls.mobileutil.support;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.baidu.mobstat.StatService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ywcai.ls.mobileutil.R;
import ywcai.ls.util.MyConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstMenuFragment extends Fragment {

    private Context context;
    private void CreateIntent(int pos)
    {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putInt(MyConfig.STR_INTENT_ARGS, pos);
        intent.putExtras(bundle);
        intent.setClass(context, TemplateActivity.class);
        this.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context=this.getContext();
        View view=inflater.inflate(R.layout.fragment_homepage_menu, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.homePage_menuList);
        int[] images = { R.drawable.homepage_menu_ping,
                R.drawable.homepage_menu_lan,
                R.drawable.homepage_menu_wifi,
//                R.drawable.homepage_menu_more,
                R.drawable.homepage_menu_station,
                R.drawable.homepage_menu_gps,
                R.drawable.homepage_menu_ble,
                R.drawable.homepage_menu_sensor,
                R.drawable.homepage_menu_orientation,
                R.drawable.homepage_menu_step,
                R.drawable.homepage_menu_more,
                R.drawable.homepage_menu_more
               };
        String[] titles =context.getResources().getStringArray(R.array.homepage_menu_title);
        List<Map<String, Object>> items = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem",images[i]);
            item.put("textItem", titles[i]);
            items.add(item);
        }
        SimpleAdapter adapter = new SimpleAdapter(context,
                items,
                R.layout.listview_homepage_menu,
                new String[]{"imageItem", "textItem"},
                new int[]{R.id.menu_image, R.id.menu_title});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               CreateIntent(position);
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(),"Menu");
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(this.getContext(),"Menu");
    }
}
