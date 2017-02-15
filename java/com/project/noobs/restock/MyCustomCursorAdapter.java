package com.project.noobs.restock;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.project.noobs.restock.data.CityPreference;
import com.project.noobs.restock.data.DBHandler;

/**
 * Created by Noobs on 11/2/2016.
 */
public class MyCustomCursorAdapter extends SimpleCursorAdapter implements View.OnClickListener {
     String updateitem,updatequantity;

     TextView tv,item,quantity;
     Button delete,update;
     DBHandler db;
     DisplayList dl = new DisplayList();
     LinearLayout ln;
    SimpleCursorAdapter simpleCursorAdapter2;


    public MyCustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return super.getView(position, convertView, parent);
    }

    @Override
    public void bindView(final View view, Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);
        final String[] glid = new String[cursor.getCount()];
        final String[] glitem = new String[cursor.getCount()];
        final String[] glquantity = new String[cursor.getCount()];

        final String listid = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(0)));
        final String listitem = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
        final String listquantity = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)));

        for(int i=0;i<getCount();i++){
            glid[i] = listid;
            glitem[i]= listitem;
            glquantity[i]= listquantity;
            Log.d("LauncherTag", "Item iteration " + listitem + " " + glid[i] + " " + glitem[i] + " " + glquantity[i]);
        }
        ln = (LinearLayout)view.findViewById(R.id.lay);
        tv = (TextView)view.findViewById(R.id.gID);
        item = (TextView)view.findViewById(R.id.gItem);
        quantity = (TextView)view.findViewById(R.id.gQuantity);
        update = (Button)view.findViewById(R.id.updateGrocery);
        update.setTag(R.string.glcursor,cursor.getPosition());
        update.setTag(R.string.glid,listid);
        update.setTag(R.string.glitem,listitem);
        update.setTag(R.string.glquantity,listquantity);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(v.getContext(),listid,listitem,listquantity);
                Log.d("LauncherTag","info " + v.getTag() + " " + v.getTag(R.string.glcursor));
              }

        });
        delete = (Button)view.findViewById(R.id.deleteGrocery);
        delete.setTag(listid);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new DBHandler(v.getContext());
                int getid = Integer.parseInt(v.getTag().toString());
                db.deleteGrocery(getid);
                view.animate().alpha(0).translationY(100);


             }
        });
        Log.d("LauncherTag","Info " + " " + cursor.getPosition() + " " + listid+ " "+ listitem + " "+ listquantity);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return super.newView(context, cursor, parent);
    }
    public class updateList extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            Log.d("LauncherTag","Deletion made ");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }
    }

    public boolean checkitem(String item){

        if (!item.equals("")){
            return true;
        }
        return false;
    }
    public boolean checkquantity(String quantity){
        boolean intornot = false;
        try {
            int num = Integer.parseInt(quantity);
            intornot=true;
            Log.d("LauncherTag",num+" is a number");
        } catch (NumberFormatException e) {
            Log.d("LauncherTag",quantity+" is not a number");
        }

        if (!quantity.equals("")&&intornot){
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.deleteGrocery:

                break;
            case R.id.updateGrocery:
                break;
        }
    }
    private void showDialog(final Context con, String id, String item , String quantity){
        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setTitle("Update Item");

        LinearLayout layout = new LinearLayout(con);
        layout.setMinimumWidth(500);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        final TextView tv = new TextView(con);
        tv.setInputType(InputType.TYPE_CLASS_TEXT);
        tv.setText(id);
        tv.setWidth(75);
        tv.setPadding(25,0,0,25);
        layout.addView(tv);
        final EditText iteml = new EditText(con);
        iteml.setInputType(InputType.TYPE_CLASS_TEXT);
        iteml.setHint("Please fill in an item");
        iteml.setText(item);
        iteml.setWidth(200);
        layout.addView(iteml);
        final EditText quantityl = new EditText(con);
        quantityl.setInputType(InputType.TYPE_CLASS_TEXT);
        quantityl.setHint(quantity);
        quantityl.setWidth(75);
        quantityl.setText(quantity);
        layout.addView(quantityl);
        builder.setView(layout);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db = new DBHandler(con);
                int getid = Integer.parseInt(tv.getText().toString());
                db.updateGroceryList(getid,  iteml.getText().toString(),  quantityl.getText().toString());
                Toast.makeText(con,"Item has been changed",Toast.LENGTH_SHORT).show();


            }

        });
        builder.show();
    }
    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

}
