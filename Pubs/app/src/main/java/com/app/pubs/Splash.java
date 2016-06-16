package com.app.pubs;

import android.app.Activity;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        userSession = new SessionManager(getApplicationContext());

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                userSession.checkLogin();

                if(userSession.isLoggedIn()) {
                    //SplashView.this.finish();
                    intent = new Intent(Splash.this,MainActivity.class);
                    //finish();
                } else {
                    intent = new Intent(Splash.this,Login.class);
                    //finish();
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        }, 1000);
    }
}
