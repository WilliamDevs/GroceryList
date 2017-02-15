package com.project.noobs.restock;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Noobs on 6/20/2016.
 */
public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context cont;

    private MySingleton(Context context){
        this.cont = context;
        requestQueue = getRequestQueue();

    }


    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(cont.getApplicationContext());


        }
        return requestQueue;
    }

    public static synchronized MySingleton getInstance(Context context){
        if (mInstance==null){
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public<T> void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }

}
