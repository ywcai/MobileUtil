package ywcai.ls.common.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.widget.Toast;

import org.apache.mina.core.future.WriteFuture;

import ywcai.ls.common.em.MouseViewUpdateType;
import ywcai.ls.mobileutil.HomeActivity;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.common.em.MouseViewType;
import ywcai.ls.common.ComponentStatus;
import ywcai.ls.common.draw.CatchScreenInf;
import ywcai.ls.module.tools.orientation.Orientation;
import ywcai.ls.util.statics.MesUtil;
import ywcai.ls.util.statics.ResultCode;

public class PushShadowService extends IntentService {
    ComponentStatus instance = ComponentStatus.getInstance();

    public PushShadowService() {

        super("PushShadowService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        Intent nfIntent = new Intent(this, HomeActivity.class);
        nfIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.nav))
                .setContentTitle("投影模式")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("正在持续投影服务...")
                .setWhen(System.currentTimeMillis());
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        } else {
            //noinspection deprecation
            notification = builder.getNotification();
        }
        notification.defaults = Notification.DEFAULT_SOUND;
        startForeground(7772, notification);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        CatchScreenInf catchScreenInf = instance.catchScreen;
        while (instance.mouseViewType == MouseViewType.SHADOW) {
            if (instance.socket.getSessionStatus()) {
                byte[] deskByte = catchScreenInf.getDeskByte();
                if (deskByte != null) {
                    try {
                        WriteFuture writeFuture = MesUtil.sendByte(instance.socket, deskByte);
                        writeFuture.awaitUninterruptibly();
                    } catch (Exception e) {
                    }
                }
            } else {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void onDestroy() {
        this.stopForeground(true);
        instance.catchScreen.stopCatch();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        if (instance.socket.getSessionStatus() && instance.catchScreen.checkChange()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        WriteFuture writeFuture = MesUtil.sendJson(instance.socket, ResultCode.json_type_req_local_refresh_screen, instance.catchScreen.getScreenSize());
                        writeFuture.awaitUninterruptibly();
                    } catch (Exception e) {
                    }
                }
            }).start();

        }
        super.onConfigurationChanged(newConfig);
    }
}
