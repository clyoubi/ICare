package com.smookcreative.icare.Tips;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smookcreative.icare.R;

/**
 * Created by Y.c on 02/02/2017.
 */
public class Fonts {

    private static Context context;
    public Fonts(Context context) {
        this.context=context;
    }

    public Fonts() {}

    //Set Typefaces

    public static void setAllTextView(ViewGroup parent, Typeface font) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);

            if (child instanceof ViewGroup) {
                setAllTextView((ViewGroup) child, font);
            } else if (child instanceof TextView) {
                //((TextView) child).setTypeface(getFont());
                ((TextView) child).setTypeface(font);
            }
        }
    }


    public static void setAllTextViewWithTypeface(ViewGroup parent, int typeface) {

        Typeface font = null;
        //Set Typefaces
        Typeface ralewayRegular=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayRegular));
        Typeface ralewaybold=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayBold));
        Typeface ralewayitalic=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayItalic));
        Typeface ralewaysemibold=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewaySemiBold));
        Typeface ralewaylightitalic=Typeface.createFromAsset(context.getAssets(),context.getString(R.string.ralewayLightItalic));



        switch (typeface){

            case 0:
                font = ralewaybold;
                break;
            case 1:
                font = ralewaysemibold;
                break;
            case 2:
                font = ralewayRegular;
                break;
            case 3:
                font = ralewayitalic;
                break;
            case 4:
                font = ralewaylightitalic;
                break;
        }


        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            final View child = parent.getChildAt(i);

            if (child instanceof ViewGroup) {
                setAllTextView((ViewGroup) child, font);
            } else if (child instanceof TextView) {
                //((TextView) child).setTypeface(getFont());
                ((TextView) child).setTypeface(font);
            }
        }
    }


}






