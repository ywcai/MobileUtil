package ywcai.ls.module.mouse.model.util;

import ywcai.ls.module.mouse.presenter.inf.TimerCountInf;

public class TimeCounter {
    private TimerCountInf callBack;
    private boolean stop=false;
    public TimeCounter(TimerCountInf _callBack)
    {
        callBack=_callBack;
    }

    public void startCounter(int maxNu)
    {
        for(int i = maxNu;i>=0;i--)
        {
            if(stop)
            {
                break;
            }
            callBack.check(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
