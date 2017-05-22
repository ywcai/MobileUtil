package ywcai.ls.module.components.ping.analysis.model.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;

import ywcai.ls.util.MyConfig;

/**
 * Created by zmy_11 on 2016/9/20.
 */
public class DrawImg {
    public Bitmap bitmap;
    private Canvas canvas;
    private Paint p;
    private int bitmapBolderColor = 0xFF926985;
    private int bitmapBgColor = 0xFFC1C0C0;
    private int bitmapBolderWidth = 10;
    private int bmpWidth = 800;
    private int bmpHeight = 800;

    public DrawImg() {
        bitmap = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        DrawAllBase();
    }


    private void DrawAllBase() {
        p.setColor(bitmapBgColor);
        p.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, bmpWidth, bmpHeight, p);
        p.setColor(bitmapBolderColor);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(bitmapBolderWidth);
        canvas.drawRect(0, 0, bmpWidth, bmpHeight, p);
    }

    public void DrawPingLogBase(String makeTime) {
        int realWidth = 600;
        int realHeight = 500;
        int realLeft = 150;
        int realTop = 200;
        int fontLeft = 40;

        int Line0msColor = 0xFF0B500A;
        int Line50msColor = 0xFF79A5DB;
        int Line100msColor = 0xFFF0CAC5;
        int Line200msColor = 0xFFF1DA2C;
        int Line400msColor = 0xFFDA9D19;
        int LineLossColor = 0xFFF30C0C;
        float LineMidThickness = 1.0f;
        float LineOffsetThickness = 4.0f;
        int fontSize = 21;
        int fontColor = 0xFF0B500A;

        p.setStrokeWidth(LineOffsetThickness);
        p.setColor(Line0msColor);
        canvas.drawLine(realLeft, realTop + realHeight, realLeft + realWidth, realTop + realHeight, p);

        p.setColor(LineLossColor);
        canvas.drawLine(realLeft, realTop, realLeft + realWidth, realTop, p);

        p.setStrokeWidth(LineMidThickness);
        p.setColor(Line400msColor);
        canvas.drawLine(realLeft, realTop + (realHeight / 5), realLeft + realWidth, realTop + (realHeight / 5), p);

        p.setColor(Line200msColor);
        canvas.drawLine(realLeft, realTop + 3 * (realHeight / 5), realLeft + realWidth, realTop + 3 * (realHeight / 5), p);

        p.setStrokeWidth(LineMidThickness);
        p.setColor(Line100msColor);
        canvas.drawLine(realLeft, realTop + 4 * (realHeight / 5), realLeft + realWidth, realTop + 4 * (realHeight / 5), p);

        p.setStrokeWidth(LineMidThickness);
        p.setColor(Line50msColor);
        canvas.drawLine(realLeft, realTop + 4 * (realHeight / 5) + (realHeight / 10), realLeft + realWidth, realTop + 4 * (realHeight / 5) + (realHeight / 10), p);

        p.setStyle(Paint.Style.FILL);
        p.setColor(fontColor);
        p.setTextSize(fontSize);
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("时延(ms)", fontLeft, realTop + realHeight + fontSize / 3, p);
        canvas.drawText("50", fontLeft, realTop + 4 * (realHeight / 5) + (realHeight / 10) + fontSize / 3, p);
        canvas.drawText("100", fontLeft, realTop + 4 * (realHeight / 5) + fontSize / 3, p);
        canvas.drawText("200", fontLeft, realTop + 3 * (realHeight / 5) + fontSize / 3, p);
        canvas.drawText("400", fontLeft, realTop + (realHeight / 5) + fontSize / 3, p);
        canvas.drawText("500+", fontLeft, realTop + fontSize / 3, p);


        int titleFontSize = 24;
        int titleFontColor = 0xff000000;
        p.setColor(titleFontColor);
        p.setTextSize(titleFontSize);
        canvas.drawText(makeTime + " , Power By MobileUtil", bmpWidth * 1 / 3, bmpHeight - 2 * fontSize, p);
        canvas.drawText("目的地址", fontLeft, 5 * fontSize, p);
    }


    public void DrawPingTextBase(String[] logInfo, int nowPos, int Count) {
        int fontSize = 21;
        int fontLeft = 40;
        canvas.drawText("序列 " + (nowPos + 1) + "/" + Count, fontLeft, 3 * fontSize, p);
        canvas.drawText(logInfo[0], bmpWidth * 1 / 3, 3 * fontSize, p);
        canvas.drawText(logInfo[1], bmpWidth * 1 / 3, 5 * fontSize, p);
        canvas.drawText(logInfo[2], bmpWidth * 1 / 3, 7 * fontSize, p);
        canvas.drawText(logInfo[3], bmpWidth * 2 / 3, 3 * fontSize, p);
        canvas.drawText(logInfo[4], bmpWidth * 2 / 3, 5 * fontSize, p);
        canvas.drawText(logInfo[5], bmpWidth * 2 / 3, 7 * fontSize, p);
        canvas.drawText(logInfo[6], fontLeft, 7 * fontSize, p);
    }


    public Bitmap getPingLogImg(int num, ArrayList<Integer> logList) {
        int myLineColor = 0xFF832626;
        float myLineSize = 2f;
        int realWidth = 600;
        int realHeight = 500;
        int realLeft = 150;
        int realTop = 200;
        int lineStepX = realWidth / MyConfig.INT_PING_RESULT_X_MAX;
        p.setStyle(Paint.Style.STROKE);
        p.setColor(myLineColor);
        p.setStrokeWidth(myLineSize);
        Path path=new Path();
        path.moveTo(realLeft,realTop+realHeight-50);
        for (int n = num * MyConfig.INT_PING_RESULT_X_MAX; n < (num + 1) * MyConfig.INT_PING_RESULT_X_MAX - 1; n++) {
            if (n >= logList.size() - 1) {
                canvas.drawPath(path,p);
                return bitmap;
            }
            int leftX = realLeft + (n % MyConfig.INT_PING_RESULT_X_MAX) * lineStepX;
            int rightX = realLeft + (n % MyConfig.INT_PING_RESULT_X_MAX) * lineStepX + lineStepX;
            int leftY;
            int rightY;
            if (logList.get(n) >= MyConfig.INT_PING_RESULT_Y_MAX||logList.get(n)==0) {
                leftY = realTop;
            } else {
                leftY = realTop + (realHeight - (logList.get(n) * realHeight / MyConfig.INT_PING_RESULT_Y_MAX));
            }
            if (logList.get(n + 1) >= MyConfig.INT_PING_RESULT_Y_MAX||logList.get(n+1)==0) {
                rightY = realTop;
            } else {
                rightY = realTop + (realHeight - (logList.get(n + 1) * realHeight / MyConfig.INT_PING_RESULT_Y_MAX));
            }
//            if (logList.get(n) != 0 && logList.get(n + 1) != 0) {
//                canvas.drawLine(leftX, leftY, rightX, rightY, p);
                path.quadTo(leftX, leftY, rightX, rightY);
//            }
        }
        canvas.drawPath(path,p);
        return bitmap;
    }
}
