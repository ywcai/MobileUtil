package ywcai.ls.util;


import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by zmy_11 on 2016/8/28.
 */
public class MyUtil {

    public static String ConvertIpToStr(int ip) {
        String ipaddr = "";
        ipaddr = (String.valueOf((ip & 0x000000FF))) + "." + String.valueOf((ip & 0x0000FFFF) >>> 8) +
                "." + String.valueOf((ip & 0x00FFFFFF) >>> 16) + "." + String.valueOf(ip >>> 24);
        return ipaddr;
    }

    public static int ConvertFrequencyToChannel(int frequency) {
        int channel = 0;
        switch (frequency) {
            case 2412:
                channel = 1;
                break;
            case 2417:
                channel = 2;
                break;
            case 2422:
                channel = 3;
                break;
            case 2427:
                channel = 4;
                break;
            case 2432:
                channel = 5;
                break;
            case 2437:
                channel = 6;
                break;
            case 2442:
                channel = 7;
                break;
            case 2447:
                channel = 8;
                break;
            case 2452:
                channel = 9;
                break;
            case 2457:
                channel = 10;
                break;
            case 2462:
                channel = 11;
                break;
            case 2467:
                channel = 12;
                break;
            case 2472:
                channel = 13;
                break;
            case 5745:
                channel = 149;
                break;
            case 5765:
                channel = 153;
                break;
            case 5785:
                channel = 157;
                break;
            case 5805:
                channel = 161;
                break;
            case 5825:
                channel = 165;
                break;
            default:
                channel = 0;
                break;
        }
        return channel;
    }

    public static String getNowDate() {
        String nowDate = Calendar.getInstance().get(Calendar.YEAR) + "-" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return nowDate;
    }

    public static String getNowTime() {
        String nowTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND);
        return nowTime;
    }

    public static String getDetailTime() {
        return getNowDate() + " " + getNowTime();
    }

    public static String saveLogImg(Bitmap bitmap, String filePath, String logImgName) {
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

    public static boolean isHaveSD() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static String getImgDirPath(String subFilePath) {
        return Environment.getExternalStorageDirectory().toString() + File.separator + MyConfig.STR_INTENT_LOG_PATH + File.separator + subFilePath;
    }

    @Nullable
    public static List<File> getImgList(String subFilePath) {

        List<File> imgList = new ArrayList<>();
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
        File[] images = new File(MyUtil.getImgDirPath(subFilePath)).listFiles(fileFilter);
        try {
            if (images.length <= 0) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        for (File img : images) {
            imgList.add(img);
        }
        return imgList;
    }

    public static String saveLogText(String text) {
        String fileDirPath = MyUtil.getImgDirPath("MyLog");
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
            if(!f.exists()) {
                f.createNewFile();
                if (!f.exists()) {
                    tip = "创建文件失败!";
                    return tip;
                }
            }
            FileOutputStream out = new FileOutputStream(f,true);
            out.write(text.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            tip = "保存数据发生错误,\n错误信息:" + e.toString();
            return tip;
        }
        return "success";
    }

    public static void sleep(int future,int use)
    {
        if (future > use) {
            future = future - use;
        } else {
            future = 0;
        }
        try {
            Thread.sleep(future);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
