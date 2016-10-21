package ywcai.ls.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;


public class MyPullListView extends ListView implements AbsListView.OnScrollListener, View.OnTouchListener {
    public boolean isOnTop = false;
    public boolean isLoading = true;
    private int downY = 0;
    private int moveLastY = 0;
    public int h = 150;
    private MyPullViewCallBack callBack;

    public MyPullListView(Context context) {
        super(context);
    }

    public MyPullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPullListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void SetCallBack(MyPullViewCallBack pCallBack) {
        callBack = pCallBack;
        this.setOnScrollListener(this);
        this.setOnTouchListener(this);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem > 0||view.getChildAt(0)==null) {
            isOnTop = false;
            return;
        }
        isOnTop = (view.getChildAt(0).getTop() >= 0);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
         int moveNowY;
         int upY;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isLoading || (!isOnTop)) {
                    return false;
                }
                moveNowY = (int) event.getRawY() - downY;
                callBack.PullView(moveLastY, moveNowY);
                moveLastY = moveNowY;
                if (moveNowY <= 0) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isLoading || (!isOnTop)) {
                    return true;
                }
                upY = (int) event.getRawY() - downY;
                callBack.UpView(upY);
                moveLastY=0;
                break;
        }
        return false;
    }
}
