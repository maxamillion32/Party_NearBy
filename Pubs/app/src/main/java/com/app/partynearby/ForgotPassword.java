package com.app.partynearby;

import android.content.Context;
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

import com.android.volley.VolleyError;
import com.app.interfaces.WebServiceInterface;
import com.app.utility.AppLog;
import com.app.utility.CheckConnectivity;
import com.app.utility.Singleton;
import com.app.utility.Validation;
import com.app.webservices.AuthorizationWebServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ram on 19/05/16.
 */
public class ForgotPassword extends AppCompatActivity implements View.OnClickListener, WebServiceInterface {
    private Toolbar toolbar;
    private Button forgot_btn;
    private EditText input_email;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        mContext = ForgotPassword.this;

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
        MenuItem item;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        item = menu.findItem(R.id.write_review);
        item.setVisible(false);
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

                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                //main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                main.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(main);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forgot_btn:

                String getEmail = input_email.getText().toString();

                if(getEmail == null || getEmail.isEmpty()) {
                    input_email.setError(getResources().getString(R.string.error_email));
                    return;
                }

                if (!Validation.isEmailValid(getEmail)) {
                    input_email.setError(getResources().getString(R.string.error_invalid_email));
                    //Singleton.getInstance(mContext).ShowToast(getResources().getString(R.string.error_email_invalid), coordinatorLayout);
                    return;
                }

                if (!CheckConnectivity.isConnected(mContext)) {
                    Singleton.getInstance(mContext).hideSoftKeyboard(this);
                    Singleton.getInstance(mContext).ShowToastMessage(getResources().getString(R.string.error_network_title), mContext);
                    return;
                }

                AuthorizationWebServices auth = new AuthorizationWebServices(this);
                auth.ForgotPasswordService(getEmail);

                break;
            default:
                break;
        }
    }

    @Override
    public void requestCompleted(String obj, int serviceCode) {
        if(obj != null) {
            String responseData = obj.toString();
            AppLog.Log("responseData", responseData);
        }

    }

    @Override
    public void requestEndedWithError(VolleyError error, int errorCode) {

        AppLog.Log("errorData", error.toString());

        if(error.networkResponse != null && error.networkResponse.data != null){
            VolleyError newerror = new VolleyError(new String(error.networkResponse.data));
            error = newerror;

        }
        AppLog.Log("ERRData: ", error.getMessage());

        JSONObject jsonObject = null;
        if(error.getMessage() != null) {
            try {
                jsonObject = new JSONObject(error.getMessage());

                String statusResponse = jsonObject.getString("message");
                AppLog.Log("statusResponse: ", jsonObject.getString("message") + "");

                Singleton.getInstance(mContext).ShowToastMessage("Email id not registered"+"", mContext);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
