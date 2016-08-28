package ywcai.ls.assist;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by zmy_11 on 2016/8/24.
 */
public class MyProgress extends View {
    private int fillPercent=0;
    private int fillColor=0xFF46E123;
    private int boldWidth=5;
    private int boldColor=0xFFD1CFCF;
    private int fontColor=Color.RED;
    private float fontSize=24f;
    private int dbm=-100;
    public MyProgress(Context context) {
        super(context);
    }

    public MyProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyProgress(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(Color.TRANSPARENT);
        Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(boldColor);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(boldWidth);
        canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), p);

        p.setColor(fillColor);
        p.setStyle(Paint.Style.FILL);
//        int right=((this.getWidth()-2*boldWidth)*fillPercent/60)+boldWidth;
//        int bottom=this.getHeight()-boldWidth;
//        canvas.drawRect(boldWidth,boldWidth,right,bottom,p);
        int right=((this.getWidth())*fillPercent/60);
        int bottom=this.getHeight();
        canvas.drawRect(0,0,right,bottom,p);



        p.setColor(fontColor);
        p.setTextSize(fontSize);
        p.setStyle(Paint.Style.FILL);
        p.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        int stringWidth= (int) p.measureText(dbm+"dbm");
        float x = (this.getWidth()-boldWidth*2) / 2 +  stringWidth / 2;
        float y = (this.getHeight()-boldWidth*2)/2 - (fontMetrics.ascent - fontMetrics.descent) / 2;
        canvas.drawText(dbm+"dbm", x, y, p);
    }

    public int getFillPercent() {
        return fillPercent;
    }

    public void setFillPercent(int fillPercent) {
        this.fillPercent = fillPercent;
        this.invalidate();
    }

    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public int getBoldWidth() {
        return boldWidth;
    }

    public void setBoldWidth(int boldWidth) {
        this.boldWidth = boldWidth;
    }

    public int getBoldColor() {
        return boldColor;
    }

    public void setBoldColor(int boldColor) {
        this.boldColor = boldColor;
    }

    public int getFontColor() {
        return fontColor;
    }

    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public int getDbm() {
        return dbm;
    }

    public void setDbm(int dbm) {
        this.dbm = dbm;
        this.invalidate();
    }
}

