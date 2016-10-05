package ywcai.ls.core;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ywcai.ls.adapter.LocalImgAdapter;
import ywcai.ls.core.draw.DrawImg;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.mobileutil.main.activity.ImageActivity;
import ywcai.ls.mobileutil.main.fragment.sub.PingAnalysisFragment;
import ywcai.ls.util.MyConfig;
import ywcai.ls.util.MyUtil;


/**
 * Created by zmy_11 on 2016/9/17.
 */
public class PingAnalysis {
    private View tabView;
    private Context context;
    private String makeTime = "";
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
        pingAnalysisFragment = paf;
        fileDirPath = MyUtil.getImgDirPath(MyConfig.STR_INTENT_LOG_PATH_PING);
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
            DrawImg drawImg=new DrawImg();
            drawImg.DrawPingLogBase(makeTime);
            drawImg.DrawPingTextBase(logInfo, i, count);
            Bitmap temp= drawImg.getPingLogImg(i, logList);
            ImageView img = new ImageView(context);
            img.setImageBitmap(temp);
            ll.addView(img);
            bmpList.add(temp);
        }
    }

    private void showImgPathList() {
        imgSaveTip.setText(R.string.images_list_title);
        imgSaveTip.setTextColor(Color.GRAY);
        imgSaveTip.setVisibility(View.VISIBLE);
        imgSaveTip.setClickable(false);
        listView.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        setLocalImgList();
    }
    private void setLocalImgList() {
        List<File> imgList = MyUtil.getImgList(MyConfig.STR_INTENT_LOG_PATH_PING);
        if(imgList==null)
        {
            return;
        }
        Collections.sort(imgList, new SortByTime());
        LocalImgAdapter localImgAdapter = new LocalImgAdapter(imgList);
        listView.setAdapter(localImgAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showImgActivity(MyConfig.STR_INTENT_LOG_PATH_PING,position);
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

    private void showImgActivity(String path,int pos) {
        Intent intent= new Intent();
        intent.setClass(pingAnalysisFragment.getActivity(), ImageActivity.class);
        Bundle bundle=new Bundle();
        bundle.putInt(MyConfig.STR_INTENT_IMAGES_INDEX,pos);
        bundle.putString(MyConfig.STR_INTENT_IMAGES_PATH,path);
        intent.putExtras(bundle);
        pingAnalysisFragment.startActivity(intent);

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

}
