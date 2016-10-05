package ywcai.ls.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.io.File;
import java.util.List;

import ywcai.ls.inf.FragmentCallBack;
import ywcai.ls.mobileutil.main.fragment.ImagesFragment;

/**
 * Created by zmy_11 on 2016/8/15.
 */
public class ShowImageAdapter extends FragmentStatePagerAdapter {
    private List<File> list;

    public ShowImageAdapter(FragmentManager fm, List<File> fileList) {
        super(fm);
        list=fileList;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return ImagesFragment.newInstance(list.get(position));
    }
}
