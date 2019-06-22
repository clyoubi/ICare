package com.smookcreative.icare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.R;

import java.util.ArrayList;

public class ActicleAdapterRebuilt extends BaseAdapter {

    private Context context;
    private ArrayList<ArticleObject2> articles;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public ActicleAdapterRebuilt(Context context, ArrayList<ArticleObject2> articles) {
        this.context = context;
        this.articles = articles;
    }

    public ActicleAdapterRebuilt() {
    }


    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }




    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.article_item,parent,false);


        TextView title = (TextView) row.findViewById(R.id.title);
        TextView source = (TextView) row.findViewById(R.id.source);
        TextView postOn = (TextView) row.findViewById(R.id.postOn);

        title.setText(articles.get(position).getTitle());
        source.setText(articles.get(position).getAuthor());
        postOn.setText(articles.get(position).getCreatedAt());

        imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) row.findViewById(R.id.image_banner);
        thumbNail.setImageUrl(articles.get(position).getPostImg(), imageLoader);


        return row;
    }

}
