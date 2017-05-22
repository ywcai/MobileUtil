package ywcai.ls.module.components.lan.presenter;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ywcai.ls.common.net.PingTest;
import ywcai.ls.controls.pull.inf.WorkTask;
import ywcai.ls.common.net.LocalInfo;



public class ScanWorkImpl implements WorkTask {
    private Context context;
    private String localIp="0.0.0.0",inputIp="0.0.0.0";
    private boolean scanLocal=true;

    public ScanWorkImpl(Context _context) {
        context = _context;
    }
    public void setInputIp(String _inputIp)
    {
        inputIp=_inputIp;
        scanLocal=false;
    }
    public void resetLocalIp()
    {
        inputIp="0.0.0.0";
        scanLocal=true;
    }
    @Override
    public List execute(int i) {
        if(scanLocal)
        {
            return pingForLocal(i);
        }
        else
        {
            return pingForInput(i);
        }
    }

    @Override
    public void onItemClick(View view, int i, Object o) {

    }

    private List pingForLocal(int i)
    {
        List tempList = new ArrayList();
        if (i == 1) {
            LocalInfo localInfo = new LocalInfo(context);
            localIp = localInfo.getLocalIp();
            tempList.addAll(localInfo.getLocalList(localIp));
        }
        String[] temp=localIp.split("\\.");
        String pingIp=temp[0]+"."+temp[1]+"."+temp[2]+"."+i;
        if(pingIp.equals(localIp))
        {
            return tempList;
        }
        PingTest pingTest =new PingTest();
        if(pingTest.pingTest(pingIp)) {
            tempList.addAll(pingTest.getListInfo(pingIp));
        }
        return tempList;
    }
    private List pingForInput(int i)
    {
        List tempList = new ArrayList();
        String[] temp=inputIp.split("\\.");
        String pingIp=temp[0]+"."+temp[1]+"."+temp[2]+"."+i;
        PingTest pingTest =new PingTest();
        if(pingTest.pingTest(pingIp)) {
            tempList.addAll(pingTest.getListInfo(pingIp));
        }
        return tempList;
    }

}
