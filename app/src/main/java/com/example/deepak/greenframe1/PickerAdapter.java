package com.example.deepak.greenframe1;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created on 18/7/16.
 */
public class PickerAdapter extends PagerAdapter{
    private Context ctx;
    private LayoutInflater layoutInflater;
    private Uri uri[];
    private ImageLoader imageLoader;
    private int selected[];

    public PickerAdapter(Context ctx,Uri uri[]) {
        imageLoader = ImageLoader.getInstance();
        this.ctx = ctx;
        this.uri=uri;
        selected=new int[uri.length];
        for(int i=0;i<uri.length;i++){
            selected[i]=0;
        }
    }

    @Override
    public int getCount() {
        return (int)Math.ceil(((double)uri.length)/4);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (GridLayout) object);
    }



    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
       int tmp=0;

        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.adapter_picker, container, false);
        ImageView imageView1 = (ImageView) view.findViewById(R.id.image_view1);
        ImageView imageView2=(ImageView)view.findViewById(R.id.image_view2);
        ImageView imageView3=(ImageView)view.findViewById(R.id.image_view3);
        ImageView imageView4=(ImageView)view.findViewById(R.id.image_view4);
        FrameLayout frameLayout1= (FrameLayout) view.findViewById(R.id.flcontainer1);
        FrameLayout frameLayout2= (FrameLayout) view.findViewById(R.id.flcontainer2);
        FrameLayout frameLayout3= (FrameLayout) view.findViewById(R.id.flcontainer3);
        FrameLayout frameLayout4= (FrameLayout) view.findViewById(R.id.flcontainer4);
        frameLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v,position*4);
            }
        });
        frameLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v,(position*4)+1);
            }
        });
        frameLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v,(position*4)+2);
            }
        });
        frameLayout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v,(position*4)+3);
            }
        });

        tmp=position*4;

        if(uri[tmp]!=null) {
            if(selected[tmp]==1){
                frameLayout1.setSelected(true);
            }
            imageLoader.displayImage(uri[tmp].toString(), imageView1);
        }
        if(uri[tmp+1]!=null) {
            if(selected[tmp+1]==1){
                frameLayout2.setSelected(true);
            }
            imageLoader.displayImage(uri[tmp + 1].toString(), imageView2);
        }
        if(uri[tmp+2]!=null) {
            if(selected[tmp+2]==1){
                frameLayout3.setSelected(true);
            }
            imageLoader.displayImage(uri[tmp + 2].toString(), imageView3);
        }
        if(uri[tmp+3]!=null) {
            if(selected[tmp+3]==1){
                frameLayout4.setSelected(true);
            }
            imageLoader.displayImage(uri[tmp + 3].toString(), imageView4);
        }
        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((GridLayout) object);
    }

    public int[] getSelected(){
        return selected;
    }

    public void onItemClick(View v, int position) {
        if(!v.isSelected()){
         v.setSelected(true);
            selected[position]=1;
        }else{
            v.setSelected(false);
            selected[position]=0;
        }
    }
}
