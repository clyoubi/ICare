package com.smookcreative.icare.Adapters.ILearn;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.smookcreative.icare.Adapters.PlacesObject;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.ISearch.BookingObject;
import com.smookcreative.icare.ISearch.PicassoImageLoadingService;
import com.smookcreative.icare.R;
import com.smookcreative.icare.Tips.CircularNetworkImageView;
import com.smookcreative.icare.Tips.Fonts;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class CentersAdapter extends BaseAdapter {


    public Context context;
    public ArrayList<BookingObject> places;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    PicassoImageLoadingService picassoImageLoadingService;

    Fonts yo =new Fonts();
    private Typeface ralewaybold;
    private Typeface ralewayRegular, ralewaySemibold, ralewayItalic;
    private RelativeLayout relativeLayout;


    /*public CentersAdapter(Context context, ArrayList<PlacesObject> places) {
        this.context = context;
        this.places = places;

        ralewayRegular=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayRegular));
        ralewaybold=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayBold));
        ralewaySemibold=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewaySemiBold));
        ralewayItalic=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayItalic));
        picassoImageLoadingService = new PicassoImageLoadingService(context);

    }*/


    public CentersAdapter(Context context, ArrayList<BookingObject> places) {
        this.context = context;
        this.places = places;

        ralewayRegular=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayRegular));
        ralewaybold=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayBold));
        ralewaySemibold=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewaySemiBold));
        ralewayItalic=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayItalic));
        picassoImageLoadingService = new PicassoImageLoadingService(context);

    }


    @Override
    public int getCount() {
        return places.size();
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

        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.center_items, parent,false);

        TextView name = row.findViewById(R.id.name);
        TextView location = row.findViewById(R.id.location);
        TextView place = row.findViewById(R.id.places);
        TextView like = row.findViewById(R.id.like);
        TextView comment = row.findViewById(R.id.comments);
        TextView id = row.findViewById(R.id.textid);
        //CircularNetworkImageView thumbnail = row.findViewById(R.id.image);
        CircleImageView thumbnail = row.findViewById(R.id.circleColor);
        ImageView isopen = (ImageView)row.findViewById(R.id.isOpen);

        name.setTypeface(ralewaySemibold);
        location.setTypeface(ralewayRegular);


        //Fonts.setAllTextView(parent, ralewayRegular);

            name.setText(places.get(position).getName());
            location.setText(places.get(position).getQuater()+" - "+places.get(position).getLocation_precision());
            place.setText(String.valueOf( places.get(position).getPlaces()));
            like.setText(places.get(position).getLikes());  // create object for like
            comment.setText(places.get(position).getComments_count());  // create
            id.setText(getFirstChar(places.get(position).getName()).toString());

            //thumbnail.setImageUrl(places.get(position).getImage(), imageLoader);
            //picassoImageLoadingService.loadImage(places.get(position).getImage(),R.drawable.gray_back, position, thumbnail);
        /*try {
            picassoImageLoadingService.loadImage(places.get(position).getPictures().get(0).toString(), thumbnail);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        if(!places.get(position).isOpen())
            isopen.setVisibility(View.INVISIBLE);
        else
            isopen.setVisibility(View.VISIBLE);


        if(places.get(position).getPictures().length()==0){
            thumbnail.setImageDrawable(randomColor(places.get(position).getColor()));
            id.setText(getFirstChar(places.get(position).getName()).toString());
        }else{
            id.setVisibility(View.INVISIBLE);
            picassoImageLoadingService = new PicassoImageLoadingService(context);
            /*try {
                thumbnail.setAlpha((float)1);
                JSONArray jsonArray = places.get(position).getPictures();
                picassoImageLoadingService.loadImage(jsonArray.getString(0), thumbnail);
            } catch (JSONException e) {
                e.printStackTrace();
                id.setVisibility(View.VISIBLE);
            }*/
        }

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


}
