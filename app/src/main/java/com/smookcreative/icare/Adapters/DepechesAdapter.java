package com.smookcreative.icare.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smookcreative.icare.R;

import java.util.ArrayList;

public class DepechesAdapter extends BaseAdapter {

    private ArrayList<ArticleObject> depeches;
    private Context context;

    public DepechesAdapter(Context context, ArrayList<ArticleObject> depeches) {
        this.depeches = depeches;
        this.context = context;
    }

    @Override
    public int getCount() {
        return depeches.size();
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
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row =  layoutInflater.inflate(R.layout.ilearn_depeches_item,parent, false);

        TextView title = row.findViewById(R.id.title);
        TextView source = row.findViewById(R.id.source);
        TextView postOn = row.findViewById(R.id.postOn);

            title.setText(depeches.get(position).getTitle());
            source.setText(depeches.get(position).getSource());
            postOn.setText(depeches.get(position).getSource());



        return row;
    }


}
