package ywcai.ls.assist;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;


/**
 * Created by zmy_11 on 2016/8/24.
 */
public class MyImage extends ImageView {

    public MyImage(Context context) {
        super(context);
    }

    public MyImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), p);

        p.setStyle(Paint.Style.FILL);

//        canvas.drawRect(0,0,right,bottom,p);
//        p.setColor(fontColor);
//        p.setTextSize(fontSize);
//        p.setStyle(Paint.Style.FILL);
//        p.setTextAlign(Paint.Align.CENTER);
//        Paint.FontMetrics fontMetrics = p.getFontMetrics();
//        int stringWidth= (int) p.measureText(dbm+"db");
//        float x = (this.getWidth()-boldWidth*2) / 2 +  stringWidth / 2;
//        float y = (this.getHeight()-boldWidth*2)/2 - (fontMetrics.ascent - fontMetrics.descent) / 2;
//        canvas.drawText(dbm+"dbm", x, y, p);
    }


}

