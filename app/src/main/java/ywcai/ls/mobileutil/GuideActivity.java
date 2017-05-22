package ywcai.ls.mobileutil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.baidu.mobstat.StatService;


public class GuideActivity extends Activity {
    TimeHandler timeHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_guide);
        timeHandler=new TimeHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {

                int time=4;
                while (time>0)
                {
                    time--;
                    Message msg=new Message();
                    msg.what=time;
                    timeHandler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private void setTimer(int time)
    {
        TextView textView=(TextView)findViewById(R.id.textTimer);
        textView.setText(time+"s");
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onPause(this);
    }
    private void loadApp()
    {
        Intent intent=new Intent();
        intent.setClass(this,HomeActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {

    }

    class TimeHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int time=msg.what;
            if(time>0)
            {
                setTimer(time);
            }
            else
            {
                loadApp();
            }

        }
    }

}
