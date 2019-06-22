package com.smookcreative.icare.Adapters;

import android.animation.Animator;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.model.Font;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.smookcreative.icare.Adapters.ILearn.CentersAdapter;
import com.smookcreative.icare.Adapters.ILearn.LoadAllCenters;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.ISearch.BookingObject;
import com.smookcreative.icare.MainActivity;
import com.smookcreative.icare.R;
import com.smookcreative.icare.Tips.CircularNetworkImageView;
import com.smookcreative.icare.Tips.Fonts;

import org.json.JSONException;

import java.util.ArrayList;



public class RecycleHelthTypeAdapters extends RecyclerView.Adapter<RecycleHelthTypeAdapters.MyViewHolder> {

    private ArrayList<HealthTypeObject> TypeList;
    //public static ArrayList<PlacesObject> centers = MainActivity.places;
    public static ArrayList<BookingObject> centers = MainActivity.bookingList;
    public static ArrayList<ArrayList<BookingObject>> MAINLIST = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    private ListView listView;
    private Toolbar Maintoolbar, toolbar;
    private RelativeLayout detailledView;
    private boolean isDetailledViewOpen=false;
    private CentersAdapter center_adapter;
    private PlacesAdapter place_adapter;
    private int POSITION;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    ArrayList<BookingObject> pharm = MainActivity.bookingList;
    int X, Y, TypeListIndex =0;
    public static int index = 0, id;

    private LoadAllCenters ALL_DATAS=new LoadAllCenters();


    // data is passed into the constructor
   public  RecycleHelthTypeAdapters(Context context, ArrayList<HealthTypeObject> list) {
        this.context=context;
        this.TypeList=list;

    }

    public  RecycleHelthTypeAdapters(Context context, ArrayList<HealthTypeObject> list, ListView listView, Toolbar toolbar) {
        this.context=context;
        this.TypeList=list;
        this.listView=listView;
        this.Maintoolbar = toolbar;
    }

    public  RecycleHelthTypeAdapters(Context context, ArrayList<HealthTypeObject> list, ListView listView, Toolbar maintoolbar, Toolbar toolbar, RelativeLayout relativeLayout) {
        this.context=context;
        this.TypeList=list;
        this.listView=listView;
        this.Maintoolbar = maintoolbar;
        this.toolbar = toolbar;
        this.detailledView=relativeLayout;
        relativeLayout.setVisibility(View.INVISIBLE);

    }


    public  RecycleHelthTypeAdapters(Context context, ArrayList<HealthTypeObject> list, ListView listView, PlacesAdapter place_adapter, Toolbar toolbar) {
        this.context=context;
        this.TypeList=list;
        this.listView=listView;
        this.place_adapter = place_adapter;
        this.toolbar = toolbar;
        this.center_adapter = center_adapter;
        MAINLIST = MainActivity.MainbookingList;
            toolbar.setTitle(list.get(0).getName());
    }


    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.health_type_card, parent, false);
        return new MyViewHolder(view);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircularNetworkImageView imageView;
        TextView txtview;
        CardView main;
        public MyViewHolder(View view) {
            super(view);
            imageView=(CircularNetworkImageView) view.findViewById(R.id.icon);
            txtview=(TextView) view.findViewById(R.id.healthtype);
            main=(CardView)view.findViewById(R.id.cards);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position)  {

        //holder.imageView.setImageResource(TypeList.get(position).imageId);
        holder.txtview.setText(Html.fromHtml(String.format("%s(%d)", TypeList.get(position).getName(), TypeList.get(position).getCount())));
        imageLoader = AppController.getInstance().getImageLoader();
        holder.imageView.setImageUrl(TypeList.get(position).getImageUrl(), imageLoader);


        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                id=TypeList.get(position).getId();
                index = position;
                listView.setAdapter(new PlacesAdapter(context, MAINLIST.get(position), 1));
                MainActivity.bookingList=MAINLIST.get(position);
                toolbar.setTitle(TypeList.get(position).getName());

            }
        });

        Typeface ralewaybold = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.ralewaySemiBold));
        holder.txtview.setTypeface(ralewaybold);

    }

    @Override
    public int getItemCount() {
       // return TypeList.size();
        return TypeList.size();
    }



}