package ywcai.ls.ui.child;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.core.Ping;
import ywcai.ls.core.ping.PingAnalysis;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.sub.PingAnalysisActivity;
import ywcai.ls.util.MyConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class PingAnalysisFragment extends Fragment {
    private PingAnalysis pingAnalysis;
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context= MyApplication.getInstance().getApplicationContext();
        View tab_view = inflater.inflate(R.layout.fragment_tab_ping_analysis, container, false);
        ArrayList<Integer> logList=this.getArguments().getIntegerArrayList(MyConfig.STR_INTENT_LIST_ARGS);
        PingAnalysis pingAnalysis =new PingAnalysis(tab_view,logList);
        return tab_view;
    }
}
