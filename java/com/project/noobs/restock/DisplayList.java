package com.project.noobs.restock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.project.noobs.restock.data.DBHandler;
import com.project.noobs.restock.model.Grocery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisplayList extends AppCompatActivity implements View.OnClickListener {
    SimpleCursorAdapter simpleCursorAdapter2;
    EditText item,quantity;
    Button add,delete;
    ListView list;
    int status,tool;
    String newitem,newquantity,name;
    DBHandler db;
    Toolbar myToolbar;
    String url ="http://192.168.1.153/Savage/Material/addItem.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);
        SharedPreferences sharedPreferences = getSharedPreferences("Color", Context.MODE_PRIVATE);
        status = sharedPreferences.getInt("status", status);
        tool = sharedPreferences.getInt("tool", tool);
        myToolbar = (Toolbar) findViewById(R.id.dl);
        setSupportActionBar(myToolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("Grocery List");
        bar.setDisplayUseLogoEnabled(false);
        bar.setDisplayShowTitleEnabled(true);
        myToolbar.setBackgroundColor(tool);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(status);
        }
        item = (EditText)findViewById(R.id.item);
        quantity = (EditText)findViewById(R.id.quantity);
        quantity.setText("0");
        add = (Button)findViewById(R.id.newGrocery);
        delete = (Button)findViewById(R.id.deleteGrocery);
        list = (ListView)findViewById(R.id.list);
        list.setItemsCanFocus(true);
        add.setOnClickListener(this);
        add.setEnabled(false);
        db = new DBHandler(this);
        displayGroceryList();
        showDialog();

        item.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newitem = item.getText().toString();
                newquantity = quantity.getText().toString();

                boolean checkitem = checkitem(newitem);
                boolean checkquantity = checkquantity(newquantity);
                Log.d("LauncherTag","item = " + checkitem + " " + "quantity = " + checkquantity);
                if(checkitem&&checkquantity){
                    add.setEnabled(true);
                    add.setTextColor(Color.DKGRAY);
                }else {
                    add.setEnabled(false);
                    add.setTextColor(Color.RED);
                    Log.d("LauncherTag","Grocery not added");
                }
            }
        });
        quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                newitem = item.getText().toString();
                newquantity = quantity.getText().toString();
                boolean checkitem = checkitem(newitem);
                boolean checkquantity = checkquantity(newquantity);
                Log.d("LauncherTag","item = " + checkitem + " " + "quantity = " + checkquantity);
                if(checkitem&&checkquantity){
                    add.setEnabled(true);
                    add.setTextColor(Color.DKGRAY);

                    Log.d("LauncherTag","Grocery added");
                }else {
                    add.setEnabled(false);
                    add.setTextColor(Color.RED);
                    Log.d("LauncherTag","Grocery not added");
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.newGrocery:
                db.addGrocery(new Grocery(newitem,newquantity));
                displayGroceryList();
                Toast.makeText(this,"Grocery added",Toast.LENGTH_SHORT).show();
                new sendItem().execute();
                break;
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
            Log.i("",num+" is a number");
        } catch (NumberFormatException e) {
            Log.i("",quantity+" is not a number");
        }

        if (!quantity.equals("")&&intornot){
            return true;
        }
        return false;
    }

    public void displayGroceryList(){

            Cursor cursor = db.getGroceryList2();

            if(cursor==null){
                Log.d("LauncherTag", "Cursor is null");
            }else{
                Log.d("LauncherTag", "Cursor is not  null");
            }
            String[] columns = new String[]{
                 DBHandler.KEY_ID,
                 DBHandler.KEY_ITEM,
                 DBHandler.KEY_QUANTITY
            };
            int[] bound = new int[]{
                    R.id.gID,
                    R.id.gItem,
                    R.id.gQuantity
            };

            simpleCursorAdapter2 = new MyCustomCursorAdapter(this, R.layout.grocerylistitem, cursor,columns,bound,0);
            list.setAdapter(simpleCursorAdapter2);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.display, menu);
        return super.onCreateOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showDialog();
        return super.onOptionsItemSelected(item);

    }
    private void showDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Whose grocery list is this?");

        final EditText cityInput = new EditText(this);
        cityInput.setInputType(InputType.TYPE_CLASS_TEXT);
        cityInput.setHint("Input your name");
        builder.setView(cityInput);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(cityInput.getText().toString().equals("")) {
                    showDialog();
                }else {
                    name = cityInput.getText().toString();
                }
            }

        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LauncherTag","MAIN tHREAD resumed");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LauncherTag","MAIN tHREAD PASUWED");

    }
    public class sendItem extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonResponse = new JSONObject(response);

                                boolean success = jsonResponse.getBoolean("success");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("LauncherTag", "Error sending suggestion" +error);
                    error.printStackTrace();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("name",name);
                    params.put("item",item.getText().toString());
                    params.put("quantity",quantity.getText().toString());
                    return params;

                }
            };

            MySingleton.getInstance(DisplayList.this).addToRequestQueue(stringRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            item.setText("");
        }
    }

}
