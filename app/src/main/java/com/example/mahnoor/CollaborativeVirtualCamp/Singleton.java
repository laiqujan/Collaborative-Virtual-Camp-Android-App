package com.example.mahnoor.CollaborativeVirtualCamp;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;



public class Singleton {
    private static Singleton mSingleton;
    private RequestQueue requestQueue;
    private static Context context;

    private Singleton(Context context)
    {
        this.context = context;
        requestQueue = getRequestQueue();
    }


    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized Singleton getmSingleton(Context context)
    {
        if(mSingleton==null)
        {
            mSingleton =  new Singleton(context);
        }
        return mSingleton;
    }

    public<T> void addRequest(Request<T> request)
    {
        requestQueue.add(request);
    }


}
