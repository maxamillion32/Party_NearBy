package com.app.partynearby;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.app.utility.SessionManager;

/**
 * Created by ram on 15/06/16.
 */
public class Splash extends Activity {
    private SessionManager userSession;
    private  Intent intent;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        mContext = Splash.this;
        userSession = new SessionManager(getApplicationContext());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //userSession.checkLogin();

                if(userSession.isLoggedIn() && mContext != null) {
                    //SplashView.this.finish();
                    intent = new Intent(mContext,MainActivity.class);
                    //finish();
                } else {
                    intent = new Intent(mContext,Login.class);
                    //finish();
                }
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent);


            }
        }, 1000);
    }
}
