package com.example.finalprojectmobileapplication.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.finalprojectmobileapplication.R;

public class GlideUtils {
    public static void loadUrlBanner(String url, ImageView imageView){
        if(imageView == null){
            return;
        }
        if(StringUtil.isEmpty(url)){
            imageView.setImageResource(R.drawable.img_no_image);
            return;
        }

        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.img_no_image)
                .dontAnimate()
                .into(imageView);
    }

    public static void loadUrl(String url, ImageView imageView){
        if(imageView == null){
            return;
        }
        if(StringUtil.isEmpty(url)){
            imageView.setImageResource(R.drawable.img_no_available);
            return;
        }

        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.img_no_available)
                .dontAnimate()
                .into(imageView);
    }
}
