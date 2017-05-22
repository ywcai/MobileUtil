package ywcai.ls.module.mouse.presenter.inf;
import android.content.Context;
import java.util.List;

public interface UpdateViewInf {
     Context getContext();
     void setPullViewStatus(boolean conn,List list);
     void onClickListItem();
     void sessionCreating();
     void sessionCreated(boolean conn);
     void sessionChecking(int count);
     void sessionChecked(boolean checked,String reason);
     void sessionClosed();
     String getUsername();
     String getPsw();
     String getIp();
     void showInfo(String info);
     void onClickBreakBtn();
     void onClickSetIpBtn();
}
