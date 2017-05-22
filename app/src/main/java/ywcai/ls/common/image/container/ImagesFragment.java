package ywcai.ls.common.image.container;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import ywcai.ls.mobileutil.R;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class ImagesFragment extends Fragment {

    private File showFile;
    @SuppressLint("ValidFragment")
    public ImagesFragment(File img)
    {
        showFile=img;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_tab_image, container, false);
        final ImageView img=(ImageView)view.findViewById(R.id.imagePager_img);
        TextView imgName=(TextView)view.findViewById(R.id.imagePager_path);
        Bitmap bmp = BitmapFactory.decodeFile(String.valueOf(showFile),null);
        img.setImageBitmap(bmp);
        imgName.setText(showFile.getPath());
        return view;
    }
    public static ImagesFragment newInstance(File img) {
        ImagesFragment fragment = new ImagesFragment(img);
        return fragment;
    }
}
