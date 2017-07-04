package ywcai.ls.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zmy_11 on 2017/6/30.
 */

public class FileLookUtil {

    public  List<File> getImgList(String subFilePath) {

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
        FileSaveUtil fileSaveUtil=new FileSaveUtil();
        File[] images = new File(fileSaveUtil.getImgDirPath(subFilePath)).listFiles(fileFilter);
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
}
