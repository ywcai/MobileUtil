package ywcai.ls.module.tools.wifi.model.draw;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class CurveView extends View {
    private int boldWidth=3;
    private int boldColor=0xFFD1CFCF;
    private int fontColor=Color.GREEN;
    private float fontSize=30f;
    private int textTop=10;
    private int pointTop=4;
    private int minHeight=50;
    private String viewName="test";
    public CurveView(Context context) {
        super(context);
    }
    public CurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CurveView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(Color.TRANSPARENT);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        if(this.getMeasuredHeight()>minHeight) {
            p.setColor(boldColor);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(boldWidth);
            Path pLeft = new Path();
            Path pRight = new Path();
            pLeft.moveTo(0, this.getMeasuredHeight());
            pLeft.quadTo(0, pointTop, this.getMeasuredWidth() / 2, pointTop);
            canvas.drawPath(pLeft, p);
            pRight.moveTo(this.getMeasuredWidth(), this.getMeasuredHeight());
            pRight.quadTo(this.getMeasuredWidth(), pointTop, this.getMeasuredWidth() / 2, pointTop);
            canvas.drawPath(pRight, p);
        }
        if(this.getMeasuredHeight()>minHeight) {
            p.setColor(fontColor);
            p.setTextSize(fontSize);
            p.setStyle(Paint.Style.FILL);
            p.setTextAlign(Paint.Align.CENTER);
            int stringWidth = (int) p.measureText(viewName);
            float x = this.getMeasuredWidth()/2;
            float y = textTop+fontSize/ 2;
            canvas.drawText(viewName, x, y, p);
        }
    }


    public void setBoldWidth(int boldWidth) {
        this.boldWidth = boldWidth;
        this.invalidate();
    }


    public void setBoldColor(int boldColor) {
        this.boldColor = boldColor;
        this.invalidate();
    }
    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
        this.invalidate();
    }
    public void setViewName(String viewName) {
        this.viewName = viewName;
        this.invalidate();
    }
}

