package ywcai.ls.mobileutil.main.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import ywcai.ls.adapter.ShowImageAdapter;
import ywcai.ls.mobileutil.R;
import ywcai.ls.util.MyConfig;
import ywcai.ls.util.MyUtil;


public class ImageActivity extends AppCompatActivity {

    private String filePath;
    private int selectPos;
    private RelativeLayout relativeLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        filePath=bundle.getString(MyConfig.STR_INTENT_IMAGES_PATH);
        selectPos=bundle.getInt(MyConfig.STR_INTENT_IMAGES_INDEX);
        setContentView(R.layout.activity_images);
        InitView();


    }
    private void InitView() {
        relativeLayout=(RelativeLayout)findViewById(R.id.toolbar_sub);
        List<File> imgList= MyUtil.getImgList(filePath);
        final int Count=imgList.size();
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_assist);
        setSupportActionBar(mToolbar);
        TextView back=(TextView)findViewById(R.id.title_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageActivity.this.finish();
            }
        });
        final TextView title=(TextView)findViewById(R.id.toolbar_assist_title);
        FragmentManager fm = this.getSupportFragmentManager();
        ViewPager viewPager=(ViewPager)findViewById(R.id.image_pager);
        viewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.setVisibility(View.GONE);
            }
        });
        ShowImageAdapter showImageAdapter=new ShowImageAdapter(fm,imgList);
        viewPager.setAdapter(showImageAdapter);
        viewPager.setCurrentItem(selectPos);
        title.setText((selectPos+1)+"/"+imgList.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                title.setText((position+1)+"/"+Count);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
