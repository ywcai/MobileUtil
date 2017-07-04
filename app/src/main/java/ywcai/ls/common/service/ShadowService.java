package ywcai.ls.common.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import org.apache.mina.core.future.WriteFuture;

import ywcai.ls.mobileutil.HomeActivity;
import ywcai.ls.mobileutil.R;
import ywcai.ls.common.em.MouseViewType;
import ywcai.ls.common.ComponentStatus;
import ywcai.ls.common.draw.CatchScreenInf;
import ywcai.ls.util.statics.MesUtil;

public class ShadowService extends IntentService {

    public ShadowService() {
        super("ShadowService");
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
        ComponentStatus status = ComponentStatus.getInstance();
        CatchScreenInf catchScreenInf = status.catchScreen;
        while (status.mouseViewType == MouseViewType.SHADOW) {
            byte[] deskByte = catchScreenInf.getDeskByte();
            if (deskByte != null && status.socket.getSessionStatus()) {
                try {
                    WriteFuture writeFuture = MesUtil.sendByte(status.socket, deskByte);
                    writeFuture.awaitUninterruptibly();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }
}
