package ywcai.ls.core.ping;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ywcai.ls.adapter.LocalImgAdapter;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.ui.child.PingAnalysisFragment;
import ywcai.ls.util.MyConfig;
import ywcai.ls.util.MyUtil;


/**
 * Created by zmy_11 on 2016/9/17.
 */
public class PingAnalysis {
    private View tabView;
    private Context context;

    private int bitmapBolderColor = 0xFF926985;
    private int bitmapBgColor = 0xFFC1C0C0;
    private int bitmapBolderWidth = 10;
    private int bmpWidth = 800;
    private int bmpHeight = 800;
    private int realWidth = 600;
    private int realHeight = 500;
    private int realLeft = 150;
    private int realTop = 200;

    private int myLineColor = 0xFF832626;
    private float myLineSize = 3.5f;

    private int Line0msColor = 0xFF0B500A;
    private int Line50msColor = 0xFF79A5DB;
    private int Line100msColor = 0xFFF0CAC5;
    private int Line200msColor = 0xFFF1DA2C;
    private int Line400msColor = 0xFFDA9D19;
    private int LineLossColor = 0xFFF30C0C;
    private float LineMidThickness = 1.0f;
    private float LineOffsetThickness = 4.0f;
    private int fontSize = 21;
    private int fontColor = 0xFF0B500A;
    private int fontLeft = 40;
    private int titleFontSize = 24;
    private int titleFontColor = 0xff000000;
    private String makeTime = "";
    private int lineStepX;
    private boolean isSaved = false;
    private List<Bitmap> bmpList = new ArrayList<>();
    private Button btnSave;
    private TextView imgSaveTip;
    private LinearLayout ll;
    private PingAnalysisFragment pingAnalysisFragment;
    private String fileDirPath;
    private ScrollView scrollView;
    private ListViewCompat listView;


    public PingAnalysis(View view, ArrayList<Integer> logList, String[] logInfo, PingAnalysisFragment paf) {
        lineStepX = realWidth / MyConfig.INT_PING_RESULT_X_MAX;
        pingAnalysisFragment = paf;
        fileDirPath = MyUtil.getImgDirPath();
        InitView(view);
        if (logList.size() != 0) {
            DrawImg(logList, logInfo);
        } else {
            showImgPathList();
        }
    }

    private void InitView(View view) {
        makeTime = MyUtil.getDetailTime();
        context = MyApplication.getInstance().getApplicationContext();
        tabView = view;
        ll = (LinearLayout) tabView.findViewById(R.id.tab_ping_analysis);
        btnSave = (Button) tabView.findViewById(R.id.bt_imgSave);
        imgSaveTip = (TextView) tabView.findViewById(R.id.imgSaveTip);
        scrollView = (ScrollView) tabView.findViewById(R.id.scroll_bmp);
        listView = (ListViewCompat) tabView.findViewById(R.id.now_local_img);
        imgSaveTip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImgPathList();
                imgSaveTip.setClickable(false);
            }
        });
    }

    private void DrawImg(ArrayList<Integer> logList, String[] logInfo) {
        listView.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        bmpList.clear();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSaved) {
                    isSaved = saveImg();
                    if (isSaved) {
                        Toast.makeText(MyApplication.getInstance().getApplicationContext(), "图片保存成功！", Toast.LENGTH_SHORT).show();
                        showImgPathList();

                    } else {
                        Toast.makeText(MyApplication.getInstance().getApplicationContext(), "图片保存失败!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        int count = 0;
        if (logList.size() % MyConfig.INT_PING_RESULT_X_MAX == 0) {
            count = logList.size() / MyConfig.INT_PING_RESULT_X_MAX;
        } else {
            count = (logList.size() / MyConfig.INT_PING_RESULT_X_MAX) + 1;
        }
        for (int i = 0; i < count; i++) {
            Bitmap global = CreateBaseBitmap(logInfo, i, count);
            drawMyLines(global, i, logList);
            ImageView img = new ImageView(context);
            img.setImageBitmap(global);
            ll.addView(img);
            bmpList.add(global);
        }
    }

    private void showImgPathList() {
        imgSaveTip.setText("本地文件目录:" + fileDirPath);
        imgSaveTip.setTextColor(Color.GRAY);
        imgSaveTip.setVisibility(View.VISIBLE);
        imgSaveTip.setClickable(false);
        listView.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        setLocalImgList();
    }

    private void setLocalImgList() {
        final ArrayList<File> imgList = new ArrayList<>();
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().lastIndexOf("jpg") != -1) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        File[] images = new File(MyUtil.getImgDirPath()).listFiles(fileFilter);
        try {
            if (images.length <= 0) {
                return;
            }
        }
        catch (Exception e)
        {
            return ;
        }
        for (File img : images) {
            imgList.add(img);
        }
        Collections.sort(imgList, new SortByTime());
        LocalImgAdapter localImgAdapter = new LocalImgAdapter(imgList);
        listView.setAdapter(localImgAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showImg(imgList.get(position));
            }
        });
    }

    class SortByTime implements Comparator {
        @Override
        public int compare(Object lhs, Object rhs) {
            File img1 = (File) lhs;
            File img2 = (File) lhs;
            if (img1.lastModified() < img2.lastModified()) {
                return 1;
            } else
                return 0;
        }
    }

    private void showImg(File img) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(img), "image/*");
        try {
            pingAnalysisFragment.startActivity(intent);
        } catch (Exception e) {
        }
    }

    private boolean saveImg() {
        for (int i = 0; i < bmpList.size(); i++) {
            String temp = "";
            if (!(temp = MyUtil.saveLogImGg(bmpList.get(i), fileDirPath, makeTime + "_" + i + ".jpg")).equals("success")) {
                imgSaveTip.setText(temp);
                return false;
            }
        }
        return true;
    }

    private void drawMyLines(Bitmap baseImg, int num, ArrayList<Integer> logList) {
        Canvas canvas = new Canvas(baseImg);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStyle(Paint.Style.STROKE);
        p.setColor(myLineColor);
        p.setStrokeWidth(myLineSize);

        for (int n = num * MyConfig.INT_PING_RESULT_X_MAX; n < (num + 1) * MyConfig.INT_PING_RESULT_X_MAX - 1; n++) {
            if (n >= logList.size() - 1) {
                return;
            }
            int leftX = realLeft + (n % MyConfig.INT_PING_RESULT_X_MAX) * lineStepX;
            int rightX = realLeft + (n % MyConfig.INT_PING_RESULT_X_MAX) * lineStepX + lineStepX;
            int leftY;
            int rightY;
            if (logList.get(n) >= MyConfig.INT_PING_RESULT_Y_MAX) {
                leftY = realTop;
            } else {
                leftY = realTop + (realHeight - (logList.get(n) * realHeight / MyConfig.INT_PING_RESULT_Y_MAX));
            }
            if (logList.get(n + 1) >= MyConfig.INT_PING_RESULT_Y_MAX) {
                rightY = realTop;
            } else {
                rightY = realTop + (realHeight - (logList.get(n + 1) * realHeight / MyConfig.INT_PING_RESULT_Y_MAX));
            }
            if (logList.get(n) != 0 && logList.get(n + 1) != 0) {
                canvas.drawLine(leftX, leftY, rightX, rightY, p);
            }
        }
    }

    private Bitmap CreateBaseBitmap(String[] logInfo, int nowPos, int Count) {
        Bitmap bitmap = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(bitmapBgColor);
        p.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, bmpWidth, bmpHeight, p);
        p.setColor(bitmapBolderColor);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(bitmapBolderWidth);
        canvas.drawRect(0, 0, bmpWidth, bmpHeight, p);

        p.setColor(Line0msColor);
        p.setStrokeWidth(LineOffsetThickness);
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

        p.setColor(titleFontColor);
        p.setTextSize(titleFontSize);
        canvas.drawText(makeTime + " , Power By MobileUtil", bmpWidth * 1 / 3, bmpHeight - 2 * fontSize, p);

        canvas.drawText("序列 " + (nowPos + 1) + "/" + Count, fontLeft, 3 * fontSize, p);
        canvas.drawText("目的地址", fontLeft, 5 * fontSize, p);
        canvas.drawText(logInfo[0], bmpWidth * 1 / 3, 3 * fontSize, p);
        canvas.drawText(logInfo[1], bmpWidth * 1 / 3, 5 * fontSize, p);
        canvas.drawText(logInfo[2], bmpWidth * 1 / 3, 7 * fontSize, p);
        canvas.drawText(logInfo[3], bmpWidth * 2 / 3, 3 * fontSize, p);
        canvas.drawText(logInfo[4], bmpWidth * 2 / 3, 5 * fontSize, p);
        canvas.drawText(logInfo[5], bmpWidth * 2 / 3, 7 * fontSize, p);
        canvas.drawText(logInfo[6], fontLeft, 7 * fontSize, p);

        return bitmap;
    }

}
