package ywcai.ls.module.tools.ping.analysis.view;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mobstat.StatService;

import java.util.ArrayList;

import ywcai.ls.mobileutil.R;
import ywcai.ls.module.tools.ping.analysis.presenter.PingAnalysis;
import ywcai.ls.util.statics.MyConfig;

/**
 * A simple {@link Fragment} subclass.
 */
public class PingAnalysisFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View tab_view = inflater.inflate(R.layout.fragment_tab_ping_analysis, container, false);
        String[] logInfo=this.getArguments().getStringArray(MyConfig.STR_INTENT_LOG_ARGS);
        ArrayList<Integer> logList=this.getArguments().getIntegerArrayList(MyConfig.STR_INTENT_LIST_ARGS);
        new PingAnalysis(tab_view,logList,logInfo,this);
        return tab_view;
    }
    @Override
    public void onPause() {
        super.onPause();
        StatService.onPageEnd(this.getContext(),"PingAnalysis");
    }

    @Override
    public void onResume() {
        super.onResume();
        StatService.onPageStart(this.getContext(),"PingAnalysis");
    }
}
