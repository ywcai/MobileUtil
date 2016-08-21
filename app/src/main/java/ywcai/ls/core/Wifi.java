package ywcai.ls.core;


import android.view.View;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

import ywcai.ls.ui.UpdateViewInf;


/**
 * Created by zmy_11 on 2016/8/12.
 */
public class Wifi implements UpdateViewInf {


    private List<HashMap<String, String>> list;
    private SimpleAdapter adpter;
    private View tabView;

    public Wifi(View view) {

        tabView = view;
//        ListViewCompat listViewCompat = (ListViewCompat) tabView.findViewById(R.id.now_wifi);
//        list = new ArrayList<HashMap<String, String>>();
//        list = getList();
//        adpter = new SimpleAdapter(MyApplication.getInstance().getApplicationContext(), list, R.layout.listview_wifi, new String[]{"title", "info"}, new int[]{R.id.title_wifi, R.id.info_wifi});
//        listViewCompat.setAdapter(adpter);
    }

    public List<HashMap<String, String>> getList() {
        String[] strs = toString().split(",");
        if (list.size() > 0) {
            list.clear();
        }
        for (int n = 0; n < strs.length; n++) {
            HashMap<String, String> hp = new HashMap<String, String>();
            hp.put("title", strs[n].split("=")[0]);
            hp.put("info", strs[n].split("=")[1]);
            list.add(hp);
        }
        return list;
    }

    @Override
    public void updateView() {
        list = getList();
        adpter.notifyDataSetChanged();
    }


}
