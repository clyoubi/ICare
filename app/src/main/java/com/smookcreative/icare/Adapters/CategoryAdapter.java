package com.smookcreative.icare.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;


public class CategoryAdapter extends BaseAdapter {

    private ArrayList<CategoryObject> category;
    private Context context;
    private MyPreferences myPreferences;
    JSONArray favourites, jsonArray;




    public CategoryAdapter(Context context, ArrayList<CategoryObject> category) {
        this.context = context;
        this.category = category;
        //SetFavourites(context, category);
    }



    @Override
    public int getCount() {
        return category.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.articles_categories_favorites,parent,false);

        TextView catName = row.findViewById(R.id.catName);
        TextView artCount = row.findViewById(R.id.article_count);
        final RelativeLayout card = row.findViewById(R.id.card);
        final LottieAnimationView starLottie= row.findViewById(R.id.starLottie);
        final ImageView star = row.findViewById(R.id.star);


        catName.setText(category.get(position).getCatName());
        artCount.setText(category.get(position).getArticles_count());


        if(category.get(position).isLiked()){
            star.setImageDrawable(context.getResources().getDrawable(R.drawable.star_fill));
        }else{
            star.setImageDrawable(context.getResources().getDrawable(R.drawable.star_empty));
        }

        card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                           /* star.setVisibility(View.INVISIBLE);
                            starLottie.setVisibility(View.VISIBLE);
                            starLottie.playAnimation();
                            starLottie.loop(false);
                            starLottie.setVisibility(View.INVISIBLE);
                            star.setVisibility(View.VISIBLE);*/

                category.get(position).setLiked( !category.get(position).isLiked());
                notifyDataSetChanged();
            }
        });

        return row;
    }


    public String getFavourites() {
     JSONArray jsonArray = new JSONArray();

        for (int i=0; i<category.size(); i++){
            if(category.get(i).isLiked()){
                jsonArray.put(category.get(i).getID());
            }
        }
        return jsonArray.toString();
    }



    private ArrayList<CategoryObject> SetFavourites(Context context, ArrayList<CategoryObject> category){
        ArrayList<CategoryObject> main = new ArrayList<>();
        myPreferences =new MyPreferences(context);
        try {
             favourites = new JSONArray(myPreferences.readUserInfos().user_favourites);
           /* Toast.makeText(context, favourites.toString(), Toast.LENGTH_LONG).show();

                for (int i=0;i<favourites.length();i++){
                   

                    for (CategoryObject cat:category) {
                        Toast.makeText(context, favourites.getString(i), Toast.LENGTH_LONG).show();
                                if (favourites.getString(i).equals(cat.getID())){
                                    cat.setLiked(true);
                                    main.add(cat);
                                }
                           }
                }
                notifyDataSetChanged();*/

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        return main;
    }
}
