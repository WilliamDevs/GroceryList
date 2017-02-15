package com.example.noobs.restock;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Noobs on 6/20/2016.
 */
public class BackgroundTask {
    Context context;
    ArrayList<Contact> arrayList = new ArrayList<>();
    String url = "hey";
    String fname;
    public BackgroundTask(Context context){

        this.context = context;
     }
    public ArrayList<Contact> getList(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST,url,(String)null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int count = 0;
                        while(count<response.length()){
                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                Contact contact = new Contact(jsonObject.getString("name"),jsonObject.getString("quantity"));
                                arrayList.add(contact);
                                count++;

                                fname = jsonObject.getString("name");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.d("LauncherTag","Fnames are " + fname);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error." + error,Toast.LENGTH_LONG).show();
                Log.d("LauncherTag","Error " + error);
                error.printStackTrace();
            }
        });
        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);

        return arrayList;
    }

}
