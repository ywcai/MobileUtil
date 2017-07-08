package ywcai.ls.module.mouse.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mobstat.StatService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ywcai.ls.common.eventbus.MouseViewUpdate;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.mobileutil.R;
import ywcai.ls.module.mouse.presenter.ControlImpl;
import ywcai.ls.module.mouse.presenter.inf.ControlInf;


public class MouseActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private ControlInf controlInf;
    private int yDown, yUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouse_main);
        controlInf = new ControlImpl();
        addClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateView(MouseViewUpdate mouseViewUpdate) {
        switch (mouseViewUpdate.mouseViewUpdateType) {
            case SESSION_CLOSED:
                showInfo("当前SOCKET连接断开!");
                sessionClosed(mouseViewUpdate.tip);
                break;
        }
    }

    private void showInfo(String tip) {
            Toast.makeText(MyApplication.getInstance().getApplicationContext(), tip, Toast.LENGTH_LONG).show();
    }

    private void sessionClosed(String tip) {
        this.finish();
    }

    private void addClickListener() {
        Button escBtn = (Button) this.findViewById(R.id.btn_esc);
        Button backBtn = (Button) this.findViewById(R.id.btn_exit_mouse);
        escBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        Button leftBtn = (Button) this.findViewById(R.id.bt_left);
        Button rightBtn = (Button) this.findViewById(R.id.bt_right);
        Button leftAndMoveBtn = (Button) this.findViewById(R.id.bt_left_move);
        Button moveBtn = (Button) this.findViewById(R.id.bt_main);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_esc:
                controlInf.clickEsc();
                break;
            case R.id.btn_exit_mouse:
                this.finish();
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
        controlInf.clickExitMouse();
        super.onDestroy();
    }


    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}


