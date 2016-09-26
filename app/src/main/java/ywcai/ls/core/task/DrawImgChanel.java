package ywcai.ls.core.task;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

import ywcai.ls.util.MyConfig;

/**
 * Created by zmy_11 on 2016/9/20.
 */
public class DrawImgChanel {
    public Bitmap bitmap;
    private Canvas canvas;
    private Paint p;
//    private int bitmapBolderColor = 0xFF926985;
//    private int bitmapBgColor = 0xFFC1C0C0;
//    private int bitmapBolderWidth = 10;
    private int bmpWidth = 800;
    private int bmpHeight = 800;

    public DrawImgChanel(int w, int h) {
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bmpWidth = w;
        bmpHeight = h;
        canvas = new Canvas(bitmap);
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        DrawBaseLine();
    }

    public void DrawBaseLine() {
        int baseLeft = 0;
        int baseTop = 0;
        int lineLenth = 800;
        int lineSpace = 110;

        int Line100dbmColor = 0xFF0B500A;
        int Line90dbmColor = 0xFF0B500A;
        int Line80dbmColor = 0xFF79A5DB;
        int Line70dbmColor = 0xFFF0CAC5;
        int Line60dbmColor = 0xFFF1DA2C;
//        int Line50dbmColor = 0xFFDA9D19;
        int LineMaxColor = 0xFFF30C0C;
        float LineMidThickness = 1.0f;
        float LineOffsetThickness = 4.0f;
        p.setStrokeWidth(LineOffsetThickness);
        p.setColor(LineMaxColor);
        canvas.drawLine(baseLeft, baseTop, baseLeft + lineLenth, baseTop, p);
        p.setStrokeWidth(LineMidThickness);
        p.setColor(Line60dbmColor);
        canvas.drawLine(baseLeft, baseTop + lineSpace, baseLeft + lineLenth, baseTop + lineSpace, p);
        p.setColor(Line70dbmColor);
        canvas.drawLine(baseLeft, baseTop + 2 * lineSpace, baseLeft + lineLenth, baseTop + 2 * lineSpace, p);
        p.setColor(Line80dbmColor);
        canvas.drawLine(baseLeft, baseTop + 3 * lineSpace, baseLeft + lineLenth, baseTop + 3 * lineSpace, p);
        p.setColor(Line90dbmColor);
        canvas.drawLine(baseLeft, baseTop + 4 * lineSpace, baseLeft + lineLenth, baseTop + 4 * lineSpace, p);
        p.setColor(Line100dbmColor);
        canvas.drawLine(baseLeft, baseTop + 5 * lineSpace, baseLeft + lineLenth, baseTop + 5 * lineSpace, p);
//        p.setStyle(Paint.Style.FILL);
//        p.setColor(fontColor);
//        p.setTextSize(fontSize);
//        p.setTextAlign(Paint.Align.LEFT);
//        canvas.drawText("-50↑", fontLeft, baseTop + fontSize / 3, p);
//        canvas.drawText("-60", fontLeft, baseTop + 1 * lineSpace + fontSize / 3, p);
//        canvas.drawText("-70", fontLeft, baseTop + 2 * lineSpace + fontSize / 3, p);
//        canvas.drawText("-80", fontLeft, baseTop + 3 * lineSpace + fontSize / 3, p);
//        canvas.drawText("-90", fontLeft, baseTop + 4 * lineSpace + fontSize / 3, p);
//        canvas.drawText("-100", fontLeft, baseTop + 5 * lineSpace + fontSize / 3, p);
    }
}
