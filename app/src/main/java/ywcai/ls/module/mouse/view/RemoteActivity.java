package ywcai.ls.module.mouse.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.baidu.mobstat.StatService;
import ywcai.ls.mobileutil.R;
import ywcai.ls.module.mouse.lsenum.ViewType;
import ywcai.ls.module.mouse.presenter.ControlImpl;
import ywcai.ls.module.mouse.presenter.inf.ControlInf;
import ywcai.ls.module.mouse.presenter.inf.UpdateActivityInf;


public class RemoteActivity extends AppCompatActivity implements UpdateActivityInf,View.OnClickListener,View.OnTouchListener {
    private ControlInf controlInf;
    private int yDown, yUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse_main);
        controlInf = new ControlImpl(this);
        controlInf.init();
        addClickListener();
    }
    private void addClickListener()
    {
        Button mouseBtn = (Button) this.findViewById(R.id.bt_mouse);
        Button shadowBtn = (Button) this.findViewById(R.id.bt_shadow);
        Button disconnectBtn = (Button) this.findViewById(R.id.bt_exit);
        Button escBtn = (Button) this.findViewById(R.id.btn_esc);
        Button backBtn = (Button) this.findViewById(R.id.btn_exit_mouse);
        mouseBtn.setOnClickListener(this);
        shadowBtn.setOnClickListener(this);
        disconnectBtn.setOnClickListener(this);
        escBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        Button leftBtn=(Button) this.findViewById(R.id.bt_left);
        Button rightBtn=(Button) this.findViewById(R.id.bt_right);
        Button leftAndMoveBtn=(Button) this.findViewById(R.id.bt_left_move);
        Button moveBtn=(Button)this.findViewById(R.id.bt_main);
        leftBtn.setOnTouchListener(this);
        rightBtn.setOnTouchListener(this);
        leftAndMoveBtn.setOnTouchListener(this);
        moveBtn.setOnTouchListener(this);
    }
    private void SetBtnEnable(int id, boolean flag) {
        Button btn = (Button) this.findViewById(id);
        btn.setEnabled(flag);
    }
    private void SetBtnText(int id, String text) {
        Button btn = (Button) this.findViewById(id);
        btn.setText(text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        controlInf.requestShadowConn(requestCode,resultCode,data);
    }

    @Override
    public void disconnectSession() {
        RemoteActivity.this.finish();
    }

    @Override
    public void changeViewType(ViewType viewType) {
        RelativeLayout mouse=(RelativeLayout)findViewById(R.id.mouse_ui);
        RelativeLayout menu=(RelativeLayout)findViewById(R.id.remote_menu);
        switch (viewType)
        {
            case SHADOW:
                menu.setVisibility(View.VISIBLE);
                mouse.setVisibility(View.GONE);
                SetBtnEnable(R.id.bt_shadow,true);
                SetBtnEnable(R.id.bt_exit,true);
                SetBtnText(R.id.bt_shadow,"退出手机投影模式!");
                break;
            case CONN:
                menu.setVisibility(View.VISIBLE);
                mouse.setVisibility(View.GONE);
                SetBtnEnable(R.id.bt_shadow,true);
                SetBtnEnable(R.id.bt_exit,true);
                SetBtnText(R.id.bt_shadow,"进入手机投影模式");
                break;
            case MOUSE:
                menu.setVisibility(View.GONE);
                mouse.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void loadShadowMode() {
        SetBtnEnable(R.id.bt_shadow,false);
        SetBtnText(R.id.bt_shadow,"Loading...");
        SetBtnEnable(R.id.bt_exit,false);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showTip(String tip) {
        Toast.makeText(this,tip,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_mouse:
                controlInf.clickMenuMouse();
                break;
            case R.id.bt_shadow:
                controlInf.clickMenuShadow();
                break;
            case R.id.bt_exit:
                controlInf.clickMenuDisConnect();
                break;
            case R.id.btn_esc:
                controlInf.clickEsc();
                break;
            case R.id.btn_exit_mouse:
                controlInf.clickExitMouse();
                break;
        }
    }

    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            case R.id.bt_main:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    yDown = (int) event.getRawY();
                    controlInf.moveDown();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    yUp = (int) event.getRawY();
                    controlInf.moveUp();
                    if (yUp - yDown <= -50) {
                        controlInf.onTouchUp();
                    }
                    if (yUp - yDown >= 50) {
                        controlInf.onTouchDown();
                    }
                }
                break;
            case R.id.bt_left:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    controlInf.leftDown();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    controlInf.leftUp();
                }
                break;
            case R.id.bt_right:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    controlInf.rightDown();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    controlInf.rightUp();
                }
                break;
            case R.id.bt_left_move:
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    controlInf.leftDown();
                    controlInf.moveDown();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    controlInf.leftUp();
                    controlInf.moveUp();
                }
                break;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        controlInf.closeActivity();
        super.onDestroy();
    }
}


