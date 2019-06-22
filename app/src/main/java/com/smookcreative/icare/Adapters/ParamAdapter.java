package com.smookcreative.icare.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.smookcreative.icare.DatabasesManagers.MyPreferences;
import com.smookcreative.icare.ILearn.ILearn;
import com.smookcreative.icare.Login;
import com.smookcreative.icare.R;
import com.smookcreative.icare.Settings;

import java.util.ArrayList;



public class ParamAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<paramsObject> paramsObjects;
    private MyPreferences myPreferences;
    boolean AUDIO = true, small= false;
    public static String help_website="https://icare.smookcreative.com/help";

    public ParamAdapter(Context context, ArrayList<paramsObject> paramsObjects) {
        this.context= context;
        this.paramsObjects = paramsObjects;
    }

    public ParamAdapter(Context context, ArrayList<paramsObject> paramsObjects, boolean small) {
        this.context= context;
        this.paramsObjects = paramsObjects;
        this.small = small;
    }

    @Override
    public int getCount() {
        return paramsObjects.size();
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
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.params_single_item, parent, false);

        Typeface ralewayRegular=Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.ralewayRegular));

        RelativeLayout relativeLayout = row.findViewById(R.id.param);


        ImageView icon = row.findViewById(R.id.icon);
        if(small){
            icon.setScaleX((float) 0.5);
            icon.setScaleY((float) 0.5);
        }

        TextView paramName = row.findViewById(R.id.paramName);
        final Switch change = row.findViewById(R.id.change);

        paramName.setTypeface(ralewayRegular);


        icon.setImageDrawable(paramsObjects.get(position).getIcon());
        paramName.setText(paramsObjects.get(position).getParamName());

        boolean isSwicthState = paramsObjects.get(position).isSwitchState();
        change.setChecked(isSwicthState);


        if(paramsObjects.get(position).isSwitch()){
            change.setVisibility(View.VISIBLE);

                change.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ConfigAudio(change, isChecked, position);
                    }
                });

        }

        return row;
    }


   public void OnClickListenner(String slug){

        switch (slug){

            case "my_profile":
                //Toast.makeText(context, slug, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, com.smookcreative.icare.SettingsClasses.Profile.class);
                    context.startActivity(intent);
                break;

            case "notification":
                Toast.makeText(context, slug, Toast.LENGTH_SHORT).show();
                break;

            case "language":
                ChangeLanguage();
                break;

            case "invite":
                inviteFriends();
                break;

            case "help":
                try{
                    Intent showWebsite = new Intent(Intent.ACTION_VIEW, Uri.parse(help_website));
                    context.startActivity(showWebsite);
                }catch (Exception e){

                }
                break;

            case "signout":
                ExitApplication();
                break;

            case "delete_account":
                Toast.makeText(context, slug, Toast.LENGTH_SHORT).show();
                break;

        }

   }

    private void ConfigAudio(final Switch change, final boolean isSwitchState, final int position) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        String title, message, action;

        myPreferences = new MyPreferences(context);

            if(isSwitchState){
                title = "Activer les controlleurs audio";
                message = "Les articles seront précrées avant d'être lus vous permettant d'efectuer les actions: Play, Pause, En avant, En arrière";
                action ="Activer";
            }else{
                title = "Désactiver les controlleurs audio";
                message = "Les articles seront seront lus directement sans être convertis pour les controles: Play, Pause, En avant, En arrière";
                action ="Désactiver";
            }


        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton(action,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        boolean state = myPreferences.isAudioConversion();
                        myPreferences.ActiveAudio(isSwitchState);
                        paramsObjects.get(position).setSwitchState(isSwitchState);
                        AUDIO = true;
                        notifyDataSetChanged();
                    }
                });

        alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
               notifyDataSetChanged();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }



    private void inviteFriends(){

        String text= "Salut!! j'utilise I Care pour avoir la derniere actualité, les astuces, conseils sur la santé du quotidien. Téléchargement gratuit sur https://icare.smookcreative.com/download";

       Intent i = new Intent(Intent.ACTION_SEND);
       i.setType("text/plain");
       i.putExtra(Intent.EXTRA_TEXT,text);

       context.startActivity(Intent.createChooser(i,"Inviter via"));

   }



   private boolean ChangeLanguage(){

       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

       alertDialogBuilder.setTitle("Langue");
       alertDialogBuilder.setMessage("Choisissez votre langue");
       alertDialogBuilder.setView(LayoutInflater.from(context).inflate(R.layout.settings_dialog_language, null));

       alertDialogBuilder.setPositiveButton("Ok",
               new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface arg0, int arg1) {

                   }
               });

       alertDialogBuilder.setNegativeButton("Annuler",new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {

           }
       });

       AlertDialog alertDialog = alertDialogBuilder.create();
       alertDialog.show();

        return true;
   }




    private void ExitApplication(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            alertDialogBuilder.setTitle("Déconnexion");
            alertDialogBuilder.setMessage("Voulez-vous vraiment vous déconnecter? Cette action effacera vos données d'utilisation dans l'application!");

            alertDialogBuilder.setPositiveButton("Oui",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {


                            myPreferences = new MyPreferences(context);
                            myPreferences.DeleteUserInfos();
                            disconnectFromFacebook();
                            Intent login = new Intent(context, Login.class);
                            context.startActivity(login);
                            ((Settings)context).finish();
                            //((ILearn)context).finish();
                        }
                    });

            alertDialogBuilder.setNegativeButton("Non",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void disconnectFromFacebook()
    {
        if (AccessToken.getCurrentAccessToken() == null) {

        }else{
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                    .Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    LoginManager.getInstance().logOut();
                }
            }).executeAsync();
        }

    }

}
