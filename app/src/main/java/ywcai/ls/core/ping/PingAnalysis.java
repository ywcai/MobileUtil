package ywcai.ls.core.ping;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;


/**
 * Created by zmy_11 on 2016/9/17.
 */
public class PingAnalysis {
    private View tabView;
    private Context context;

    private int bitmapBolderColor = 0xFF926985;
    private int bitmapBgColor = 0xFFC1C0C0;
    private int bitmapBolderWidth = 10;
    private int bmpWidth =750;
    private int bmpHeight = 900;
    private int realWidth = 550;
    private int realHeight = 500;
    private int realLeft = 150;
    private int realTop = 200;

    private int Line0msColor=0xFF0B500A;
    private int Line50msColor=0xFF79A5DB;
    private int Line100msColor=0xFFF0CAC5;
    private int Line200msColor=0xFFF1DA2C;
    private int Line400msColor=0xFFDA9D19;
    private int LineLossColor=0xFFF30C0C;
    private float LineMidThickness = 1.0f;
    private float LineOffsetThickness = 4.0f;

    private int fontSize=21;
    private int fontColor=0xFF0B500A;
    private int fontLeft=40;

    public PingAnalysis(View view, ArrayList<Integer> logList) {
        InitView(view, logList);
    }

    private void InitView(View view, ArrayList<Integer> logList) {
        context=MyApplication.getInstance().getApplicationContext();
        tabView = view;
        Bitmap global=CreateBitmap();
        LinearLayout ll=(LinearLayout)tabView.findViewById(R.id.tab_ping_analysis);
        ImageView img=new ImageView(context);
        img.setImageBitmap(global);
        ll.addView(img);
    }

    private Bitmap CreateBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(bitmapBgColor);
        p.setStyle(Paint.Style.FILL);
//        canvas.drawRect(bitmapBolderWidth,bitmapBolderWidth,bmpWidth - bitmapBolderWidth * 2, bmpHeight - bitmapBolderWidth * 2, p);
        canvas.drawRect(0, 0, bmpWidth, bmpHeight, p);

        p.setColor(bitmapBolderColor);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(bitmapBolderWidth);
        canvas.drawRect(0, 0, bmpWidth, bmpHeight, p);

//        p.setColor(realBolderColor);
//        p.setStrokeWidth(realBolderWidth);
//        canvas.drawRect(realLeft, realTop, realWidth, realHeight, p);

        p.setColor(Line0msColor);
        p.setStrokeWidth(LineOffsetThickness);
        canvas.drawLine(realLeft, realTop + realHeight, realLeft + realWidth, realTop + realHeight, p);

        p.setColor(LineLossColor);
        canvas.drawLine(realLeft, realTop, realLeft + realWidth, realTop, p);

        p.setStrokeWidth(LineMidThickness);
        p.setColor(Line400msColor);
        canvas.drawLine(realLeft, realTop+(realHeight/5), realLeft + realWidth,  realTop+(realHeight/5), p);

        p.setColor(Line200msColor);
        canvas.drawLine(realLeft, realTop+3*(realHeight/5), realLeft + realWidth,  realTop+3*(realHeight/5), p);

        p.setStrokeWidth(LineMidThickness);
        p.setColor(Line100msColor);
        canvas.drawLine(realLeft, realTop+4*(realHeight/5), realLeft + realWidth,  realTop+4*(realHeight/5), p);

        p.setStrokeWidth(LineMidThickness);
        p.setColor(Line50msColor);
        canvas.drawLine(realLeft, realTop+4*(realHeight/5)+(realHeight/10), realLeft + realWidth,realTop+4*(realHeight/5)+(realHeight/10), p);

        p.setStyle(Paint.Style.FILL);
        p.setColor(fontColor);
        p.setTextSize(fontSize);
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("时延(ms)",fontLeft,realTop+realHeight+fontSize/3,p);
        canvas.drawText("50",fontLeft,realTop+4*(realHeight/5)+(realHeight/10)+fontSize/3,p);
        canvas.drawText("100",fontLeft,realTop+4*(realHeight/5)+fontSize/3,p);
        canvas.drawText("200",fontLeft,realTop+3*(realHeight/5)+fontSize/3,p);
        canvas.drawText("400",fontLeft,realTop+(realHeight/5)+fontSize/3,p);
        canvas.drawText("超时",fontLeft,realTop+fontSize/3,p);
        return bitmap;
    }

}
