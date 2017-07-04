package ywcai.ls.module.mouse.presenter;
import ywcai.ls.common.em.MouseViewUpdateType;
import ywcai.ls.mobileutil.MyApplication;
import ywcai.ls.common.em.MouseViewType;
import ywcai.ls.common.ComponentStatus;
import ywcai.ls.module.mouse.model.MouseEvent;
import ywcai.ls.module.mouse.presenter.inf.ControlInf;
import ywcai.ls.module.mouse.presenter.inf.MouseEventInf;
import ywcai.ls.util.statics.MesUtil;
import ywcai.ls.util.statics.ResultCode;

public class ControlImpl implements ControlInf {
    private ComponentStatus status = ComponentStatus.getInstance();
    private MouseEventInf mouseEvent;

    public ControlImpl( ) {
        mouseEvent=new MouseEvent(MyApplication.getInstance().getApplicationContext());
    }
    @Override
    public void clickEsc() {
        mouseEvent.sendEsc();
    }


    @Override
    public void moveDown() {
        mouseEvent.sendMoveDown();
    }

    @Override
    public void moveUp() {
        mouseEvent.sendMoveUp();
    }

    @Override
    public void leftDown() {
        mouseEvent.sendLeftDown();
    }

    @Override
    public void leftUp() {
        mouseEvent.sendLeftUp();
    }

    @Override
    public void rightDown() {
        mouseEvent.sendRightDown();
    }

    @Override
    public void rightUp() {
        mouseEvent.sendRightUp();
    }

    @Override
    public void onTouchUp() {
        mouseEvent.sendPagePrev();
    }

    @Override
    public void onTouchDown() {
        mouseEvent.sendPageNext();
    }

    @Override
    public void clickExitMouse() {
        status.mouseViewType = MouseViewType.CONN;
        MesUtil.sendJson(status.socket, ResultCode.json_type_req_local_close_mouse,"");
    }

    @Override
    public void repeatMouse() {

    }
}
