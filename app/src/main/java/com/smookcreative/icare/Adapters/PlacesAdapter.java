package com.smookcreative.icare.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.smookcreative.icare.Adapters.ILearn.CentersAdapter;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.ISearch.BookingObject;
import com.smookcreative.icare.ISearch.PicassoImageLoadingService;
import com.smookcreative.icare.MainActivity;
import com.smookcreative.icare.R;
import com.smookcreative.icare.Tips.CircularNetworkImageView;
import com.smookcreative.icare.Tips.Fonts;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlacesAdapter extends BaseAdapter {


    public Context context;
    public ArrayList<PlacesObject> places;
    public ArrayList<BookingObject> booking;
    PicassoImageLoadingService picassoImageLoadingService;

    Fonts yo =new Fonts();
    private Typeface ralewaybold;
    private Typeface ralewayRegular, ralewaySemibold;
    private RelativeLayout relativeLayout;
    int id;



    public PlacesAdapter(Context context, ArrayList<BookingObject> places, int id) {
        this.context = context;
        this.booking = places;
        this.id = id;

        ralewayRegular=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayRegular));
        ralewaybold=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayBold));
        ralewaySemibold=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewaySemiBold));

        picassoImageLoadingService = new PicassoImageLoadingService(context);
    }


    @Override
    public int getCount() {
        return booking.size();
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
    public View getView(int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row;

        if(id==1){
            row = inflater.inflate(R.layout.item, parent,false);
            ImageView isLike = (ImageView)row.findViewById(R.id.like);
            TextView rate=(TextView)row.findViewById(R.id.rate);

            rate.setTypeface(ralewayRegular);
            rate.setText(booking.get(position).getLikes());
            if(booking.get(position).isI_like())
                isLike.setImageDrawable(context.getResources().getDrawable(R.drawable.heart));
            else
                isLike.setImageDrawable(context.getResources().getDrawable(R.drawable.emptyheart));

        }else{
            row = inflater.inflate(R.layout.center_items, parent,false);

            TextView place = row.findViewById(R.id.places);
            TextView like = row.findViewById(R.id.like);
            TextView comment = row.findViewById(R.id.comments);

            place.setText(String.valueOf(booking.get(position).getPlaces()));
            like.setText(booking.get(position).getLikes());  // create object for like
            comment.setText(booking.get(position).getComments_count());  // create
        }


        final TextView noms_text=(TextView)row.findViewById(R.id.name);
        final TextView quartier_text=(TextView)row.findViewById(R.id.location);
        TextView numero_text=(TextView)row.findViewById(R.id.phone);
        TextView id=(TextView)row.findViewById(R.id.textid);
        relativeLayout= (RelativeLayout)row.findViewById(R.id.background);
        final CircleImageView circleImageView=(CircleImageView) row.findViewById(R.id.circleColor);
        Character character = getFirstChar(booking.get(position).getName());
        ImageView isopen = (ImageView)row.findViewById(R.id.isOpen);



        if(!booking.get(position).isOpen())
            isopen.setVisibility(View.INVISIBLE);
        else
            isopen.setVisibility(View.VISIBLE);

        JSONArray pictures=null;
        boolean empty=false;
        try {
             pictures = new JSONArray(booking.get(position).getPictures());
             if(pictures.getString(0).equals("")){
                 empty = true;
             }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assert pictures != null;
        if(pictures.length()==1 && empty){
            circleImageView.setImageDrawable(randomColor(booking.get(position).getColor()));
            id.setText(character.toString());
        }else{
            id.setVisibility(View.INVISIBLE);
            picassoImageLoadingService = new PicassoImageLoadingService(context);
            try {
                circleImageView.setAlpha((float)1);
                JSONArray jsonArray = new JSONArray(booking.get(position).getPictures());
                //picassoImageLoadingService.loadImage(jsonArray.getString(0),  R.drawable.gray_back,  position, circleImageView);
                picassoImageLoadingService.loadImage(jsonArray.getString(0), circleImageView);
            } catch (JSONException e) {
                e.printStackTrace();
                id.setVisibility(View.VISIBLE);
            }
        }





        noms_text.setText(Html.fromHtml(booking.get(position).getName()));
        quartier_text.setText(Html.fromHtml(booking.get(position).getQuater()));
        try {
            JSONArray telephone =new JSONArray(booking.get(position).getPhone());
            numero_text.setText(telephone.get(0).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        noms_text.setTypeface(ralewaySemibold);
        quartier_text.setTypeface(ralewayRegular);
        numero_text.setTypeface(ralewayRegular);
        id.setTypeface(ralewayRegular);

        return row;
    }


    public Drawable randomColor(int x){
        Random random = new Random();
        Drawable color=context.getResources().getDrawable(R.drawable.gray_back);

        switch (x){
            case 0:
                color=context.getResources().getDrawable(R.drawable.pink_back);
                break;
            case 1:
                color=context.getResources().getDrawable(R.drawable.blue_back);
                break;
            case 2:
                color=context.getResources().getDrawable(R.drawable.purple_back);
                break;
            case 3:
                color=context.getResources().getDrawable(R.drawable.orange_back);
                break;

        }


        return color;
    }


    public Character getFirstChar(String name){
        Character x = name.charAt(name.indexOf(" ")+1);
        return x;
    }

    public void setBackgroundOnlongClick(int position){

    }
}
