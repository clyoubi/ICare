package com.smookcreative.icare.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.ISearch.MoreCities;
import com.smookcreative.icare.MainActivity;
import com.smookcreative.icare.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MoreCitiesAdapter extends BaseAdapter{
    ArrayList<CategoryObject> List = new ArrayList<>();
    Context context;
    Typeface ralewayRegular;
    MyPreferences myPreferences;
    int old_Position=-1;


    public MoreCitiesAdapter(ArrayList<CategoryObject> list, Context context) {
        this.List = list;
        this.context = context;
        myPreferences = new MyPreferences(context);
        ralewayRegular= Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayRegular));
    }


    @Override
    public int getCount() {
        return List.size();
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
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row= inflater.inflate(R.layout.more_cities_item, parent,false);
        RadioButton city =row.findViewById(R.id.radio);
            city.setText(Html.fromHtml(String.format("%s (%s %s)", List.get(position).getCatName(), List.get(position).getArticles_count(), "resultats")));
            //city.setText(String.format("%s (%s %s)", List.get(position).getCatName(), List.get(position).getArticles_count(), "resultats"));
            city.setTypeface(ralewayRegular);

        try{

            JSONArray jsonArray = new JSONArray(myPreferences.readUserInfos().getCity());

                /*if(List.get(position).getID().equals(jsonArray.get(0))){
                    city.setChecked(true);
                }else{
                    city.setChecked(false);
                }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(position==old_Position){
            city.toggle();
        }

        city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // ShowAlerDialog(position);
                        MoreCities.idPosition = position;
                        old_Position =position;
                        notifyDataSetChanged();
                }
            });
        return row;

    }



   /* private void ShowAlerDialog(final int pos){


      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Changer de ville?");
        alertDialogBuilder.setMessage("Utiliser comme ville par défaut?");

        alertDialogBuilder.setPositiveButton("Oui",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        List.get(pos).setLiked(true);
                        JSONArray array = new JSONArray();
                            array.put(List.get(pos).getID());
                            array.put(List.get(pos).getCatName());

                        UserInfos userInfos = myPreferences.readUserInfos();
                            userInfos.setCity(array.toString());
                            myPreferences.saveUserinfos(userInfos);
                            notifyDataSetChanged();

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("city",List.get(pos).getID());
                        context.startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("Non",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("city",List.get(pos).getID());
                context.startActivity(intent);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }*/
}
