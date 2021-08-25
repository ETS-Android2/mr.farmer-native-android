package com.snag.ink.user.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.snag.ink.user.R;

public class imageadapter extends PagerAdapter {

    private Context mContext;
    private int[] mImageids = new int[]{R.drawable.img1,R.drawable.img2,R.drawable.img3};

    public imageadapter(Context Context) {
        this.mContext = Context;
    }

    @Override
    public int getCount() {
        return mImageids.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(mImageids[position]);
        container.addView(imageView,0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }

}
