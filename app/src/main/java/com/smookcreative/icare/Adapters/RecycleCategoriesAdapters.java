package com.smookcreative.icare.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.smookcreative.icare.DatabasesManagers.AppController;
import com.smookcreative.icare.DatabasesManagers.LoadArticlesRequest;
import com.smookcreative.icare.ILearn.ILearn;
import com.smookcreative.icare.R;
import com.smookcreative.icare.Tips.CircularNetworkImageView;

import java.util.ArrayList;


public class RecycleCategoriesAdapters extends RecyclerView.Adapter<RecycleCategoriesAdapters.MyViewHolder> {

    private ArrayList<CategoryObject> TypeList;
    private LayoutInflater mInflater;
    private Context context;
    private LoadArticlesRequest loadArticlesRequest;
    public static String favouritesList="";
    int POSITION;




    // data is passed into the constructor
   public RecycleCategoriesAdapters(Context context, ArrayList<CategoryObject> list) {
        this.context=context;
        this.TypeList=list;
    }



    // inflates the row layout from xml when needed
    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.ilearn_categories_circular_item, parent, false);
        return new MyViewHolder(view);
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtview;
        public MyViewHolder(View view) {
            super(view);
            txtview=(TextView) view.findViewById(R.id.category);
        }

        @Override
        public void onClick(View v) {


        }
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        holder.txtview.setText(TypeList.get(position).getCatName());

        if(!TypeList.get(position).isLiked()){
            //v.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grayBack)));
            holder.txtview.setBackgroundColor(context.getResources().getColor(R.color.grayBack));
            TypeList.get(position).setLiked(true);
        }else{
            holder.txtview.setBackgroundColor(context.getResources().getColor(R.color.white));
            TypeList.get(position).setLiked(false);
        }

       //setBackgroundTintList(holder, holder.getLayoutPosition());

        holder.txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*
                POSITION = position;
               if(!TypeList.get(POSITION).isLiked()){
                   TypeList.get(POSITION).setLiked(true);
               }else{
                   TypeList.get(POSITION).setLiked(false);
               }
                    notifyDataSetChanged();
                    */
            }
        });
    }


    @Override
    public int getItemCount() {
        return TypeList.size();
    }


    public void setBackgroundTintList(MyViewHolder holder, int position){

       if(TypeList.get(position).isLiked()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.txtview.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grayBack)));
            }else{
                holder.txtview.setBackgroundColor(context.getResources().getColor(R.color.grayBack));
            }
            //notifyDataSetChanged();
        }

    }




    public static String GetFavourtesSelected(String categoryIdLike){
            favouritesList=favouritesList+"," +categoryIdLike + ",";
       return favouritesList;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}