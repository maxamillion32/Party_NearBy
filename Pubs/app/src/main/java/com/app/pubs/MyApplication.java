package com.app.pubs;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.app.utility.Singleton;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {

	public static final String TAG = MyApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private static MyApplication mInstance;

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            // only for gingerbread and newer versions
            MultiDex.install(this);
        }


        mInstance = this;
		initSingleton();
		FacebookSdk.sdkInitialize(getApplicationContext());
		AppEventsLogger.activateApp(this);

	}
	
	
	protected void initSingleton() {
		Singleton.getInstance(getApplicationContext());
	}

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


}
