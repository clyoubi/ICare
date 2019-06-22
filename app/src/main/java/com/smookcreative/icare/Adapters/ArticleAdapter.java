package com.smookcreative.icare.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.DatabasesManagers.VolleyStringRequest;
import com.smookcreative.icare.R;
import com.smookcreative.icare.Tips.Fonts;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

public class ArticleAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ArticleObject> articles;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Typeface ralewaybold;
    private Typeface ralewayRegular;
    int layout=0;
    private ProgressBar progressBar;


    public ArticleAdapter(Context context, ArrayList<ArticleObject> articles) {
        this.context = context;
        this.articles = articles;
    }

    public ArticleAdapter(Context context, ArrayList<ArticleObject> articles, int layout) {
        this.context = context;
        this.articles = articles;
        this.layout=layout;

         ralewayRegular=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayRegular));
         ralewaybold=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayBold));
    }

    public ArticleAdapter(Context context, ArrayList<ArticleObject> articles, int layout, ProgressBar progressBar) {
        this.context = context;
        this.articles = articles;
        this.layout=layout;
        this.progressBar= progressBar;

        ralewayRegular=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayRegular));
        ralewaybold=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayBold));
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
        View row =layoutInflater.inflate(R.layout.article_item,parent,false);


        if(layout==1){
            row = layoutInflater.inflate(R.layout.ilearn_depeches_item,parent,false);
            View stroke =row.findViewById(R.id.stroke);

            TextView title = (TextView) row.findViewById(R.id.title);
            TextView source = (TextView) row.findViewById(R.id.source);
            TextView postOn = (TextView) row.findViewById(R.id.postOn);

            //title.setText(articles.get(position).getTitle());
            title.setText(Html.fromHtml(articles.get(position).getTitle()));
            source.setText(articles.get(position).getSource());
            postOn.setText(articles.get(position).getPostOn());

            title.setTypeface(ralewaybold);
            source.setTypeface(ralewayRegular);
            postOn.setTypeface(ralewayRegular);

            int colorAccent=context.getResources().getColor(R.color.white);
            switch (articles.get(position).getCategories()){
                case "adverts":
                    colorAccent=context.getResources().getColor(R.color.yellowLight);
                    break;

                case "good_news":
                    colorAccent=context.getResources().getColor(R.color.greenLight);
                    break;

                case "urgents":
                    colorAccent=context.getResources().getColor(R.color.red);
                    title.setTextColor(colorAccent);
                    break;
            }

            title.setText(articles.get(position).getTitle());
            source.setText(articles.get(position).getSource());
            postOn.setText(articles.get(position).getPostOn());

            //stroke.setBackgroundColor(colorAccent);

        }else{

            if(position==0 && layout!=2){
                row = layoutInflater.inflate(R.layout.article_item_first_row,parent,false);

                TextView title0 = (TextView) row.findViewById(R.id.title0);
                TextView source0 = (TextView) row.findViewById(R.id.source0);
                TextView postOn0 = (TextView) row.findViewById(R.id.postOn0);

                title0.setText(articles.get(0).getTitle());
                source0.setText(articles.get(0).getSource());
                postOn0.setText(articles.get(0).getPostOn());

                title0.setTypeface(ralewaybold);
                source0.setTypeface(ralewayRegular);
                postOn0.setTypeface(ralewayRegular);


            }else{
                row = layoutInflater.inflate(R.layout.article_item,parent,false);

                if(layout==2){
                    ImageView logo_player_icon=(ImageView) row.findViewById(R.id.player_icon);
                    logo_player_icon.setVisibility(View.VISIBLE);
                }

                TextView title = (TextView) row.findViewById(R.id.title);
                TextView source = (TextView) row.findViewById(R.id.source);
                TextView postOn = (TextView) row.findViewById(R.id.postOn);

                title.setText(articles.get(position).getTitle());
                source.setText(articles.get(position).getSource());
                postOn.setText(articles.get(position).getPostOn());

                title.setTypeface(ralewaybold);
                source.setTypeface(ralewayRegular);
                postOn.setTypeface(ralewayRegular);

                   
            }

        }



        imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) row.findViewById(R.id.image_banner);
        thumbNail.setImageUrl(articles.get(position).getBanner(), imageLoader);


        /*if(position==getCount()-3){
            Toast.makeText(context, "load more articles", Toast.LENGTH_LONG).show();

            //progressBar.setVisibility(View.VISIBLE);
        }else{
            //progressBar.setVisibility(View.INVISIBLE);
        }
*/
        /*if(position==articles.size()-1){
            if(layout!=1)
                row = layoutInflater.inflate(R.layout.read_more,parent,false);
            else
                row = layoutInflater.inflate(R.layout.read_more_wide,parent,false);
        }*/


        return row;
    }

}
