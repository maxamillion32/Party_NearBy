package com.app.pubs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by ram on 19/05/16.
 */
public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Button forgot_btn;
    private EditText input_email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar_elevated);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle(getResources().getString(R.string.lb_forgot_password));
        inItWidget();
    }

    void inItWidget() {
        forgot_btn = (Button) findViewById(R.id.forgot_btn);
        input_email = (EditText) findViewById(R.id.input_email);

        forgot_btn.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;
            case R.id.action_settings:

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgot_btn:

                break;
            default:
                break;
        }
    }
}
