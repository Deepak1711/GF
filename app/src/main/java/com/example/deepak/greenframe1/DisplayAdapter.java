package com.example.deepak.greenframe1;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by deepak on 22/7/16.
 */
public class DisplayAdapter extends PagerAdapter{
    private ArrayList<URL> urls;
    private ImageLoader imageLoader;
    Context ctx;

    public DisplayAdapter(Context ctx,ArrayList<URL> urls){
        imageLoader = ImageLoader.getInstance();
        this.urls=urls;
        this.ctx=ctx;
    }

    @Override
    public int getCount() {return (int)Math.ceil(((double)urls.size())/4);}

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (GridLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        int tmp = 0;
        View view;
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.adapter_display, container, false);
        ImageView imageView1= (ImageView) view.findViewById(R.id.image1);
        ImageView imageView2= (ImageView) view.findViewById(R.id.image2);
        ImageView imageView3= (ImageView) view.findViewById(R.id.image3);
        ImageView imageView4= (ImageView) view.findViewById(R.id.image4);

        tmp = position * 4;
        if(tmp<urls.size()) {
            imageLoader.displayImage(urls.get(tmp).toString(), imageView1);
        }
        if((tmp+1)<urls.size()) {
            imageLoader.displayImage(urls.get(tmp + 1).toString(), imageView2);
        }
        if((tmp+2)<urls.size()) {
            imageLoader.displayImage(urls.get(tmp + 2).toString(), imageView3);
        }
        if((tmp+3)<urls.size()) {
            imageLoader.displayImage(urls.get(tmp + 3).toString(), imageView4);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((GridLayout) object);
    }
}
