package com.project.noobs.restock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Noobs on 6/20/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    Context context;
    ArrayList<Contact> arrayList = new ArrayList<>();
    String url ="http://192.168.1.153/Savage/Material/restock.php";
    public RecyclerAdapter (ArrayList<Contact> arrayList,Context context1){
        this.arrayList = arrayList;
        this.context = context1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.name.setText(arrayList.get(position).getName());
        holder.prof.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("LauncherTag","Text Changed");
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    Log.d("LauncherTag","Mapid  saved");
                                    if(success){

                                    }
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
                        params.put("name",holder.name.getText().toString());
                        params.put("quantity",holder.prof.getText().toString());
                        Log.d("LauncherTag",holder.name.getText().toString() + " " +holder.prof.getText().toString() );

                        return params;

                    }
                };
                MySingleton.getInstance(context).addToRequestQueue(stringRequest);
            }
        });
        holder.prof.setText(arrayList.get(position).getProf());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        EditText prof;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.name);
            prof = (EditText)itemView.findViewById(R.id.prof);
        }
    }

}
