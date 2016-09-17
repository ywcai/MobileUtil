package ywcai.ls.mobileutil;


;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.baidu.mobads.SplashAd;
import com.baidu.mobads.SplashAdListener;


public class SplashActivity extends AppCompatActivity {
    private final String MSSP_SPLASH_AD="2875760";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_splash);
        RelativeLayout adsParent = (RelativeLayout) this.findViewById(R.id.splash_ad);
        SplashAdListener listener = new SplashAdListener() {



            @Override
            public void onAdDismissed() {
                jumpWhenCanClick(); // 跳转至您的应用主界面
            }

            @Override
            public void onAdFailed(String arg0) {
                jump();
            }

            @Override
            public void onAdPresent() {

            }

            @Override
            public void onAdClick() {

            }
        };
        new SplashAd(this, adsParent, listener,MSSP_SPLASH_AD,true);
        Button btn_skip=(Button)findViewById(R.id.bt_skip);
        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpWhenCanClick();
            }
        });
    }
    public boolean canJumpImmediately = false;
    private void jumpWhenCanClick() {
        if (canJumpImmediately) {
            this.startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            this.finish();
        } else {
            canJumpImmediately = true;
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        canJumpImmediately = false;
    }
    /**
     * 不可点击的开屏，使用该jump方法，而不是用jumpWhenCanClick
     */
    private void jump() {
        this.startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        this.finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (canJumpImmediately) {
            jumpWhenCanClick();
        }
        canJumpImmediately = true;
    }


}
