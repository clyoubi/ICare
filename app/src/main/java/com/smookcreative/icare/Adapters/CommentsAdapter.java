package com.smookcreative.icare.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smookcreative.icare.R;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;

public class CommentsAdapter extends BaseAdapter {

    private ArrayList<CommentsObject> comments;
    private Context context;

    public CommentsAdapter(Context context, ArrayList<CommentsObject> comments) {
        this.comments = comments;
        this.context = context;
    }

    @Override
    public int getCount() {
        return comments.size();
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
        View row =  layoutInflater.inflate(R.layout.comments_item,parent, false);

        //HtmlTextView comment = row.findViewById(R.id.comment);
        TextView comment = row.findViewById(R.id.comment);
        TextView source = row.findViewById(R.id.commentator);
        TextView postOn = row.findViewById(R.id.comment_date);


            //comment.setHtml(comments.get(position).getComment());
            source.setText(comments.get(position).getName());
            postOn.setText(comments.get(position).getPost_date());
            comment.setText(Html.fromHtml(comments.get(position).getComment()));

            if(comments.get(position).isReply()){
                RelativeLayout rel = row.findViewById(R.id.container);
                ImageView icon = row.findViewById(R.id.icon);
                    icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_subdirectory_arrow_right_black_24dp));
                    rel.setPadding(70,0,0,0);
            }


        return row;
    }


}
