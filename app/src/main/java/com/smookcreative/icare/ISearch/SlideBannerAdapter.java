package com.smookcreative.icare.ISearch;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class SlideBannerAdapter extends SliderAdapter{
    private Context context;
    private JSONArray array;


    public SlideBannerAdapter(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {

        for(int i=0; i<array.length();i++){

            try {
                if(position==i){
                    viewHolder.bindImageSlide(array.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


}
