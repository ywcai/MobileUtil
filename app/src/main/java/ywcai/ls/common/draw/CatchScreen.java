package ywcai.ls.common.draw;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import ywcai.ls.mobileutil.MyApplication;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CatchScreen implements CatchScreenInf {
    private int mWidth,mHeight,realWidth,mScreenDensity,tempWidth;
    private ImageReader mImageReader;
    private MediaProjectionManager mediaProjectionManager;
    private WindowManager windowManager;
    private MediaProjection mediaProjection;

    public CatchScreen()
    {
         mediaProjectionManager = (MediaProjectionManager) MyApplication.getInstance().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
         windowManager=(WindowManager) MyApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
    }
    @Override
    public void requestPermission(Activity activity) {
            Intent captureIntent = mediaProjectionManager.createScreenCaptureIntent();
            activity.startActivityForResult(captureIntent, 7);
    }
    @Override
    public void initScreen( Intent mResultData) {
        mediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, mResultData);
        setScreenSize();
        mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888,2);
        mediaProjection.createVirtualDisplay(
                "MobileUtil",mWidth ,mHeight , mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
        tempWidth=mWidth;
    }
    @Override
    public String getScreenSize()
    {
        String size=mWidth+"|"+mHeight;
        return  size;
    }

    private void setScreenSize()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mWidth = metrics.widthPixels;
        mHeight= metrics.heightPixels;
    }

    @Override
    public boolean checkChange()
    {
        setScreenSize();
        if(tempWidth==mWidth)
        {
            return false ;
        }
        else
        {
            createNewScreen();
            tempWidth=mWidth;
            return true ;
        }
    }

    private void createNewScreen()
    {
        mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888,2);
        mediaProjection.createVirtualDisplay(
                "MobileUtil",mWidth ,mHeight , mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
    }
    @Override
    public byte[] getDeskByte() {
        byte[] data=null;
        try {
                Image image = mImageReader.acquireLatestImage();
                Image.Plane[] planes = image.getPlanes();
                ByteBuffer buffer = planes[0].getBuffer();
                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * mWidth;
                realWidth = mWidth + rowPadding / pixelStride;
                Bitmap bitmap = Bitmap.createBitmap(realWidth, mHeight, Bitmap.Config.ARGB_8888);
                bitmap.createBitmap(bitmap,0,0,realWidth,mHeight);
                bitmap.copyPixelsFromBuffer(buffer);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, stream);
                data = stream.toByteArray();
                image.close();
        }
        catch(Exception e) {}
        return data;
    }
    @Override
    public void stopCatch()
    {
        mediaProjection.stop();
    }

}