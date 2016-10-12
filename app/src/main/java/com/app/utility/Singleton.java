package com.app.utility;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.app.partynearby.R;

/**
 * Created by ram on 19/05/16.
 */
public class Singleton {

    private static volatile Singleton instance = null;
    private Context context;

    public String userName = null;
    public String userPhoto = null;
    public String userDOB = null;
    public String userEmail = null;


    public String ev_id = null;
    public String ev_name = null;
    public String ev_thumbnail = null;
    public String ev_date = null;
    public String ev_time = null;
    public String ev_desc = null;
    public String ev_contact_no = null;
    public String ev_address = null;
    public String ev_cost = null;
    public String ev_entrytype = null;


    private Singleton(Context context) {
        this.context = context;
    }

    public static Singleton getInstance(Context context) {
        if (instance == null) {
            synchronized (Singleton.class) {
                instance = new Singleton(context);
            }
        }
        return instance;
    }

    public static void setInstance(Singleton instance) {
        Singleton.instance = instance;
    }

    public void ShowToast(String msg, CoordinatorLayout coordinatorLayout) {
        if (msg != null && !msg.trim().equalsIgnoreCase("")) {
            //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, msg, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(ContextCompat.getColor(context, R.color.red));

            snackbar.show();
        }
    }

    public void ShowToastMessage(String msg, Context ctx) {
        if (msg != null && !msg.trim().equalsIgnoreCase("")) {
            Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();

        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String capitalize(String s) {
        if(s == null) return null;
        if(s.length() == 1){
            return s.toUpperCase();
        }
        if(s.length() > 1){
            return s.substring(0,1).toUpperCase() + s.substring(1);
        }
        return ""; }

}
