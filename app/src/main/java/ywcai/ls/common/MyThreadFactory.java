package ywcai.ls.common;

import java.util.concurrent.ThreadFactory;

/**
 * Created by zmy_11 on 2016/8/17.
 */
public class MyThreadFactory  implements ThreadFactory{
    public MyThreadFactory()
    {
        ;
    }
    @Override
    public Thread newThread(Runnable r) {
        Thread t =new Thread(r);
        t.setDaemon(true);
        return t;
    }
}
