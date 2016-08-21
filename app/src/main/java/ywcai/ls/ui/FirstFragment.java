package ywcai.ls.ui;


import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import ywcai.ls.adapter.TabFragmentAdapter;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {
    private View view;

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_first, container, false);
        TabLayout tab=(TabLayout)view.findViewById(R.id.tab);
        tab.setTabTextColors(Color.WHITE,Color.GREEN);
        ViewPager pageView=(ViewPager)view.findViewById(R.id.pageView);
        pageView.setOffscreenPageLimit(0);
        Resources r=this.getResources();
        String[] menu1_titles=r.getStringArray(R.array.menu1_tab_title);
        TabFragmentAdapter tabAdapter=new TabFragmentAdapter(getChildFragmentManager(),menu1_titles,getContext());
        pageView.setAdapter(tabAdapter);
        tab.setupWithViewPager(pageView);
        return view;
    }
}
