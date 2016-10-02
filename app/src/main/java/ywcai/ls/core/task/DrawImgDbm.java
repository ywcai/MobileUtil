package ywcai.ls.core.task;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ywcai.ls.bean.BsrLineObj;
import ywcai.ls.inf.CallBackClearBg;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.util.MyConfig;
import ywcai.ls.util.MyUtil;

/**
 * Created by zmy_11 on 2016/9/20.
 */
public class DrawImgDbm {
    public Bitmap bg;
    private Canvas canvas;
    private Paint p;
    private int bmpWidth;
    private int bmpHeight;
    private int baseLeft = 0;
    private int baseTop = 300;
    private int lineLenth = 800;
    private int lineSpace = 100;
    private int fontRight = 50;
    private int fontTitleSize = 20;
    public Bitmap temp;
    private int[] dbmColor={0xFF99B3FF,0xffFF8000,0xffD9D900,0xff6DD900,0xff00FFBF,0xFFFFDFBF,0xff0000FF,0xffFF26FF,0xff00468C,
            0xffFF9999,0xff000000,0xff00661A,0xff400000,0xff96FF73,0xffDFBFFF,0xffD90000,0xffBDBDAE,0xff003040,0xff8C008C,0xffFF007F};
    public int colorNum;
    private HashMap <String ,Integer> hashColor;
    private CallBackClearBg callBack;
    private String makeTime;

    public DrawImgDbm(int w, int h,CallBackClearBg pCallBack) {
        bmpWidth = w;
        bmpHeight = h;
        callBack=pCallBack;
        init(bmpWidth,bmpHeight);
    }

    public void DrawBaseLine() {

        int LineMaxColor = 0xFFF30C0C;
        int Line110dbmColor = 0xFF747070;
        int Line100dbmColor = 0xFFE2DEDE;
        int Line90dbmColor = 0xFFE2DEDE;
        int Line80dbmColor = 0xFFE2DEDE;
        int Line70dbmColor = 0xFFE2DEDE;
        int Line60dbmColor = 0xFFE2DEDE;


        float LineMidThickness = 1.0f;

        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(LineMidThickness);
        p.setColor(LineMaxColor);
        canvas.drawLine(baseLeft, baseTop-10, baseLeft + lineLenth - fontRight, baseTop-10, p);
        p.setColor(Line60dbmColor);
        canvas.drawLine(baseLeft, baseTop + lineSpace, baseLeft + lineLenth - fontRight, baseTop + lineSpace, p);
        p.setColor(Line70dbmColor);
        canvas.drawLine(baseLeft, baseTop + 2 * lineSpace, baseLeft + lineLenth - fontRight, baseTop + 2 * lineSpace, p);
        p.setColor(Line80dbmColor);
        canvas.drawLine(baseLeft, baseTop + 3 * lineSpace, baseLeft + lineLenth - fontRight, baseTop + 3 * lineSpace, p);
        p.setColor(Line90dbmColor);
        canvas.drawLine(baseLeft, baseTop + 4 * lineSpace, baseLeft + lineLenth - fontRight, baseTop + 4 * lineSpace, p);
        p.setColor(Line100dbmColor);
        canvas.drawLine(baseLeft, baseTop + 5 * lineSpace, baseLeft + lineLenth - fontRight, baseTop + 5 * lineSpace, p);
        p.setColor(Line110dbmColor);
        canvas.drawLine(baseLeft, baseTop + 6 * lineSpace, baseLeft + lineLenth - fontRight, baseTop + 6 * lineSpace, p);
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(fontTitleSize);
        p.setTextAlign(Paint.Align.LEFT);
        p.setColor(LineMaxColor);
        canvas.drawText("-50+", bmpWidth - fontRight, baseTop + 10, p);
        p.setColor(Line60dbmColor);
        canvas.drawText("-60", +bmpWidth - fontRight, baseTop + lineSpace - 5, p);
        p.setColor(Line70dbmColor);
        canvas.drawText("-70", bmpWidth - fontRight, baseTop + 2 * lineSpace - 5, p);
        p.setColor(Line80dbmColor);
        canvas.drawText("-80", +bmpWidth - fontRight, baseTop + 3 * lineSpace - 5, p);
        p.setColor(Line90dbmColor);
        canvas.drawText("-90", bmpWidth - fontRight, baseTop + 4 * lineSpace - 5, p);
        p.setColor(Line100dbmColor);
        canvas.drawText("-100", bmpWidth - fontRight, baseTop + 5 * lineSpace - 5, p);
        p.setColor(Line110dbmColor);
        canvas.drawText("Loss", bmpWidth - fontRight, baseTop + 6 * lineSpace - 5, p);

    }

    public Bitmap getRecordImg(HashMap tempHash, HashMap nowDbmHash) {
        int bsrWidth = 3;
        Bitmap record = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
        int xStep = bmpWidth / MyConfig.INT_WIFI_RECORD_X_MAX;
        Canvas canTemp = new Canvas(record);
        canTemp.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));
        Matrix m = new Matrix();
        m.setTranslate(xStep, 0);
        canTemp.drawBitmap(temp, m, null);
        Iterator iterator = nowDbmHash.entrySet().iterator();
        while (iterator.hasNext()) {
            int lastDbm;
            int nowDbm;
            int LineBsrColor;
            Map.Entry entry = (Map.Entry) iterator.next();
            Object key = entry.getKey();

            nowDbm = (int) entry.getValue();
            if (nowDbm > -50) {
                nowDbm = -50;
            }
            if (nowDbm < -110) {
                nowDbm = -110;
            }

            if (!tempHash.containsKey(key)) {
                lastDbm = -500;
            } else {
                lastDbm = (int) tempHash.get(key);
                if (lastDbm > -50) {
                    lastDbm = -50;
                }
                if (lastDbm < -110) {
                    lastDbm = -110;
                }
            }

            float xStart = 0;
            float yStart = baseTop - nowDbm * lineSpace / 10 - 5 * lineSpace;
            float xEnd = xStep;
            float yEnd = baseTop - lastDbm * lineSpace / 10 - 5 * lineSpace;
            p.setAntiAlias(true);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(bsrWidth);
            if(lastDbm!=-500) {
                if(!hashColor.containsKey(key))
                {
                    if(colorNum>=dbmColor.length)
                    {
                        reset();
                        return Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
                    }
                    int colorIndex=colorNum;
                    hashColor.put((String)key,colorIndex);
                    addText(key,colorIndex);
                    colorNum++;
                }
                LineBsrColor=dbmColor[((int)hashColor.get(key))%dbmColor.length];
                p.setColor(LineBsrColor);
                canTemp.drawLine(xStart, yStart, xEnd, yEnd, p);
            }
        }
        temp = record;
        return record;
    }

    private void addText(Object key, int num) {
        int fontSize=18;
        int x=num/10;
        int y=num%10;
        int textLine=10;
        int lineWidth=30;
        int lineLeft=10;
        int lineHeight=5;
        int textLeft=20;
        int textWidth=300;
        int lineColor=dbmColor[colorNum%dbmColor.length];


        p.setStyle(Paint.Style.FILL);
        p.setTextSize(fontSize);
        p.setTextAlign(Paint.Align.LEFT);
        p.setColor(lineColor);
        canvas.drawText(key.toString(),textLeft+x*(textLeft+textWidth+lineWidth+lineLeft) ,(y+1)*(fontSize+textLine), p);

        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(lineHeight);
        canvas.drawLine((textLeft+textWidth+lineLeft)*(x+1)+lineWidth*x,(y+1)*(fontSize+textLine)-fontSize/3,(textLeft+textWidth+lineLeft+lineWidth)*(x+1),(y+1)*(fontSize+textLine)-fontSize/3, p);
    }

    public void saveLog() {
        Bitmap dbmLog=Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
        Canvas c=new Canvas(dbmLog);
        c.drawBitmap(bg,0,0,null);
        c.drawBitmap(temp,0,0,null);
        String fileName= MyUtil.getDetailTime();
        String fileDirPath = MyUtil.getImgDirPath(MyConfig.STR_INTENT_LOG_PATH_PING);
        if (!(MyUtil.saveLogImGg(dbmLog, fileDirPath, fileName+".jpg")).equals("success")) {
            Toast.makeText(MyApplication.getInstance().getApplicationContext(), "日志保存失败!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(MyApplication.getInstance().getApplicationContext(), "日志保存成功!", Toast.LENGTH_LONG).show();
        }
    }

    public void init(int w, int h)
    {
        bg = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bg);
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        Rect rect=new Rect();
        rect.set(0,0,bmpWidth,bmpHeight);
        canvas.drawRect(rect,p);
        hashColor=new HashMap<>();
        temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        colorNum=0;
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));
        DrawBaseLine();
        drawLogo(11);
    }
    public void reset()
    {
        saveLog();
        temp = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
        hashColor=new HashMap<>();
        colorNum=0;
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        Rect rect=new Rect();
        rect.set(0,0,bmpWidth,baseTop-10);
        canvas.drawRect(rect,p);
        callBack.clearImg();
    }
    public void reset(int chanel)
    {
        reset();
        drawLogo(chanel);
    }
    private  void drawLogo(int chanel)
    {
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        Rect rect2=new Rect();
        rect2.set(0,bmpHeight-2*fontTitleSize,bmpWidth,bmpHeight);
        canvas.drawRect(rect2,p);

        int titleColor=0xFF747070;
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(fontTitleSize);
        p.setTextAlign(Paint.Align.LEFT);
        p.setColor(titleColor);
        makeTime= MyUtil.getDetailTime();
        canvas.drawText("Chanel "+chanel+" : "+ makeTime + " , Power By MobileUtil", bmpWidth / 5, bmpHeight -  fontTitleSize/2, p);
    }
}
