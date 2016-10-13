package ywcai.ls.core.draw;

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


    public DrawImgChanel(int w, int h) {
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
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
    }
}
