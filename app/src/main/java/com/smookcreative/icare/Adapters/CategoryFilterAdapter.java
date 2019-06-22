package com.smookcreative.icare.Adapters;


import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.ILearn.Common;
import com.smookcreative.icare.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

class CustomViewHolder extends RecyclerView.ViewHolder {

    public TextView textView;
    ItemClickListenner itemClickListenner;

    public void setItemClickListenner(ItemClickListenner itemClickListenner) {
        this.itemClickListenner = itemClickListenner;
    }

    public CustomViewHolder(View itemView) {
        super(itemView);
        textView=(TextView)itemView.findViewById(R.id.category);
    }


}


public class CategoryFilterAdapter extends RecyclerView.Adapter<CustomViewHolder>{

    ArrayList<CategoryObject> categoryObjects;
    Context context;
    public static ArrayList<String> categories_selected = new ArrayList<>();

    int row_indew = -1;
    public static boolean filterActivated =false;

    public CategoryFilterAdapter(Context context, ArrayList<CategoryObject> categoryObjects) {
        this.categoryObjects = categoryObjects;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.ilearn_categories_circular_item, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {
            holder.setIsRecyclable(false);
            holder.textView.setText(categoryObjects.get(position).getCatName());

            if(categoryObjects.get(position).isLiked()){

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.textView.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.grayBack)));
                    }else{
                        holder.textView.setBackgroundColor(context.getResources().getColor(R.color.grayBack));
                    }

            }else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.textView.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.white)));
                }else{
                    holder.textView.setBackgroundColor(context.getResources().getColor(R.color.white));
                }

            }

            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,categoryObjects.get(position).getCatName(),Toast.LENGTH_LONG).show();
                        if(position>1){
                            categoryObjects.get(0).setLiked(false);
                            categoryObjects.get(1).setLiked(false);
                        }else{
                            for(int i=0; i<categoryObjects.size(); i++){
                                categoryObjects.get(i).setLiked(false);
                            }
                        }


                    if(categoryObjects.get(position).isLiked()){
                        categoryObjects.get(position).setLiked(false);
                        //add id to the static variable
                            categories_selected.add(categoryObjects.get(position).getID());
                    }else{
                        categoryObjects.get(position).setLiked(true);
                        //remove id to the static variable
                            categories_selected.remove(categoryObjects.get(position).getID());
                    }

                    notifyDataSetChanged();
                       filterActivated = true;
                }

            });

    }

    @Override
    public int getItemCount() {
        return categoryObjects.size();
    }

    public String Categories_Choosen() throws JSONException {

        MyPreferences myPreferences = new MyPreferences(context);
         String list="";

        JSONArray jsonArray = new JSONArray();

            for (int i=0; i<categoryObjects.size(); i++){
                if(categoryObjects.get(i).isLiked()){
                    jsonArray.put(categoryObjects.get(i).getID());
                }
            }
            list=jsonArray.toString();


            if(jsonArray.getString(0).equals("123457")){
                list=myPreferences.readUserInfos().user_favourites;
            }


        return list;
    }

}
