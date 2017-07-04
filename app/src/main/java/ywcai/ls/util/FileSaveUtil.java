package ywcai.ls.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

import ywcai.ls.util.statics.MyConfig;

/**
 * Created by zmy_11 on 2017/6/30.
 */

public class FileSaveUtil {
    public  String saveLogImg(Bitmap bitmap, String filePath, String logImgName) {
        String tip = "";
        if (!isHaveSD()) {
            tip = "手机不支持外部存储";
            return tip;
        }
        try {
            File fileDir = new File(filePath);
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }
            File f = new File(fileDir, logImgName);
            f.createNewFile();
            if (!f.exists()) {
                tip = "创建文件失败!";
                return tip;
            }
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            tip = "保存数据发生错误,\n错误信息:" + e.toString();
            return tip;
        }
        return "success";
    }

    public  String saveLogText(String text) {
        String fileDirPath = getImgDirPath("MyLog");
        String tip = "";
        if (!isHaveSD()) {
            tip = "手机不支持外部存储";
            return tip;
        }
        try {
            File fileDir = new File(fileDirPath);
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }
            File f = new File(fileDir, "lsLog.log");
            if (!f.exists()) {
                f.createNewFile();
                if (!f.exists()) {
                    tip = "创建文件失败!";
                    return tip;
                }
            }
            FileOutputStream out = new FileOutputStream(f, true);
            out.write(text.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            tip = "保存数据发生错误,\n错误信息:" + e.toString();
            return tip;
        }
        return "success";
    }

    private  boolean isHaveSD() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public  String getImgDirPath(String subFilePath) {
        return Environment.getExternalStorageDirectory().toString() +
                File.separator +
                MyConfig.STR_INTENT_LOG_PATH +
                File.separator + subFilePath;
    }
}
