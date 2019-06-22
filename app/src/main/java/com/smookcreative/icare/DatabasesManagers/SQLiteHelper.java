package com.smookcreative.icare.DatabasesManagers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by Y.c on 24/02/2017.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static String DB_PATH = "/data/data/com.smookcreative.cly13.icare/databases/";

    // Data Base Name.
    public static final String DATABASE_NAME = "XlsDatR1.db";

    public static final String TABLE_NAME = "Profile";
    // Data Base Version.
    private static final int DATABASE_VERSION = 1;
    // Table Names of Data Base.
    public Context mcontext;
    public static SQLiteDatabase sqliteDataBase;





    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mcontext=context;

    }


    public void checkDB(Context context){
        File databaseFile = context.getDatabasePath(DATABASE_NAME);

        if(false==databaseFile.exists()){
            getReadableDatabase();

            if(copyDataBase(context)){
                Toast.makeText(context,"copied!!!! ", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context,"NOT copied!!!! ", Toast.LENGTH_LONG).show();
            }
        }
    }


    public boolean copyDataBase(Context context){

        try{

            InputStream myInput = context.getAssets().open(DATABASE_NAME);
            // Path to the just created empty db
            String outFileName = DB_PATH + DATABASE_NAME;
            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            //transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }



    public void openDataBase(){
        String myPath = mcontext.getDatabasePath(DATABASE_NAME).getPath();

        if(sqliteDataBase!=null && sqliteDataBase.isOpen()){
            return;
        }
        sqliteDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }





    public void closeDataBase(){
        if(sqliteDataBase!=null){
            sqliteDataBase.close();
        }

    }



    public void AddInfos(SQLiteDatabase database, String table, String nom, String quartier, String lieu, String numero, String type){

        ContentValues contentValues = new ContentValues();

        contentValues.put("NOMS",nom);
        contentValues.put("QUARTIER",quartier);
        contentValues.put("TELEPHONE",numero);
        contentValues.put("LOCALISATION",lieu);
        contentValues.put("TYPE",type);

        database.insert(table,null,contentValues);

    }



    public void UpdateInfos(SQLiteDatabase database, String id, String table, String nom, String quartier, String lieu, String numero, String type){


        ContentValues contentValues = new ContentValues();
        String clause ="WHERE _id LIKE ?";
        String args[]={id};

        contentValues.put("NOMS",nom);
        contentValues.put("QUARTIER",quartier);
        contentValues.put("TELEPHONE",numero);
        contentValues.put("LOCALISATION",lieu);
        contentValues.put("TYPE",type);

        database.update(table,contentValues,clause,args);

    }


    //insert Profile picture into the database
    public void addEntry(SQLiteDatabase database, String name, Bitmap bitmap) throws SQLiteException {
        ContentValues cv = new ContentValues();
        byte[] image= DbBitmapUtility.getBytes(bitmap);
        cv.put("KEY_NAME",name);
        cv.put("KEY_IMAGE", image);
        database.insert(TABLE_NAME, null, cv );
    }



    //U


    public void UpdateRows(SQLiteDatabase db, String city, int rows){

        ContentValues contentValues = new ContentValues();
        String table="Downloaded";
        String clause ="WHERE Name LIKE ?";
        String args[]={city};
        contentValues.put(city,rows);
        db.update(table,contentValues,clause,args);

    }


    public void CretaeTable(SQLiteDatabase db, String tableName){
        String query ="CREATE TABLE IF NOT EXISTS "+tableName +"( `_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `NOMS` TEXT, `VILLE` TEXT, `QUARTIER` TEXT, `LOCALISATION` TEXT, `TELEPHONE` TEXT, `TELEPHONE2` TEXT, `EMAIL` TEXT, `TYPE` TEXT )";
        db.execSQL(query);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
