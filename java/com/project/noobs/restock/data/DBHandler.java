package com.project.noobs.restock.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.noobs.restock.model.Grocery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noobs on 10/29/2016.
 */
public class DBHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Grocery";

    private static final String TABLE_NAME = "Groceries";

    public static final String KEY_ID = "_id";
    public static final String KEY_ITEM = "item";
    public static final String KEY_QUANTITY = "quantity";

    public DBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_GROCERY_LIST = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_ITEM + " TEXT, "
                + KEY_QUANTITY + " TEXT" + ")";
        db.execSQL(CREATE_GROCERY_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);

    }
    public void addGrocery(Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ITEM,grocery.getItem());
        values.put(KEY_QUANTITY,grocery.getQuantity());

        db.insert(TABLE_NAME,null,values);
        db.close();

    }

    public boolean updateGroceryList(int id,String item,String quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues vals = new ContentValues();
        vals.put(KEY_ID,id);
        vals.put(KEY_ITEM,item);
        vals.put(KEY_QUANTITY,quantity);



        return db.update(TABLE_NAME,vals,KEY_ID + "=" + id,null)>0;
    }

    public boolean deleteGrocery(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,KEY_ID +"="+id,null)>0;
    }

    public List<Grocery> getGroceryList(){
        List<Grocery> gl = new ArrayList<>();

        String query = "SELECT * FROM "+ TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                Grocery gc = new Grocery();
                gc.setId(Integer.parseInt(cursor.getString(0)));
                gc.setItem(cursor.getString(1));
                gc.setQuantity(cursor.getString(2));
                gl.add(gc);
            }while (cursor.moveToNext());

        }

       return gl;

    }
    public Cursor getGroceryList2(){


        String query = "SELECT * FROM "+ TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if(cursor!=null){
            cursor.moveToFirst();
            return cursor;
        }else {
            return null;
        }


    }
}
