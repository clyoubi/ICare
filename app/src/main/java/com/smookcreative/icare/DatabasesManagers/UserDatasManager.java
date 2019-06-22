package com.smookcreative.icare.DatabasesManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Y.c on 21/02/2017.
 */
public class UserDatasManager extends SQLiteOpenHelper {


    public static final String DATABASE_NAME="Xlm95Dat.db";
    public static final String DATA_TABLE="CONNEXION_SETTINGS";
    public static final int DATABASE_VERSION=1;
    public static final String CNI="CNI";
    public static final String PASSWORD="PASSWORDE";
    public static final String NUMBER="NUMBER";
    public static final String EMAIL="EMAIL";
    public String CONNEXION_STATE="TRUE";

    public Context context;



    public UserDatasManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
        SQLiteDatabase db=this.getReadableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+DATA_TABLE+
                "(_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " "+CNI+" TEXT, "+PASSWORD+" TEXT, "+NUMBER+" TEXT, "+EMAIL+" TEXT, "+CONNEXION_STATE+" TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DATA_TABLE);
        onCreate(db);
    }



    public boolean AddUsersDatas(){
       SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(CNI,"114791702");
        contentValues.put(PASSWORD,"einstein");
        db.insert(DATA_TABLE,null,contentValues);

        return true;
    }

    public boolean CheckConnexion(String cni, String password){

        SQLiteDatabase db=this.getReadableDatabase();
        String query = "SELECT * FROM " +
                        DATA_TABLE+
                        " WHERE " +
                        CNI +
                        "=" +
                        "'"+cni+"'" +
                        " AND " +
                        PASSWORD +
                        " =" +
                        "'"+password+"'";



        Cursor cursor = db.rawQuery(query, null);
        if(cursor.getCount()>0){
           return true;
        }else {
            return false;
        }

    }

}


