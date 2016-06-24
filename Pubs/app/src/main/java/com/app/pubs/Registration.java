package com.app.pubs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
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

/**
 * Created by ram on 18/05/16.
 */
public class Registration  extends AppCompatActivity implements View.OnClickListener, WebServiceInterface{
    private Toolbar toolbar;
    private Context mContext;
    //private CoordinatorLayout coordinatorLayout;
    private EditText input_fname, input_lname , input_email, input_contact,input_password, input_dob, input_ann;
    private Button btn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        mContext = Registration.this;


        toolbar = (Toolbar) findViewById(R.id.toolbar_elevated);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.lb_registration));
        }
        initWidget();
    }

    void initWidget() {

        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);
        input_fname = (EditText) findViewById(R.id.input_fname);
        input_lname = (EditText) findViewById(R.id.input_lname);
        input_contact = (EditText) findViewById(R.id.input_contact);
        input_dob = (EditText) findViewById(R.id.input_dob);
        input_ann = (EditText) findViewById(R.id.input_ann);

        btn_signup = (Button) findViewById(R.id.btn_signup);

        btn_signup.setOnClickListener(this);

        input_contact.setText("+91");
        input_contact.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (!s.toString().contains("+91")) {
                    input_contact.setText("+91");
                    Selection.setSelection(input_contact.getText(), input_contact.getText().length());

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signup:

                String getFirstName = input_fname.getText().toString();
                String getLastName = input_lname.getText().toString();
                String getEmail = input_email.getText().toString();
                String getContact = input_contact.getText().toString().trim();
                String getPass = input_password.getText().toString().trim();
                String getDob = input_dob.getText().toString();
                String getAnni = input_ann.getText().toString();

                if(getFirstName == null || getFirstName.isEmpty()) {
                    input_fname.setError(getResources().getString(R.string.error_fname));
                    return;
                }

                if(getLastName == null || getLastName.isEmpty()) {
                    input_lname.setError(getResources().getString(R.string.error_lname));
                    return;
                }

                if(getEmail == null || getEmail.isEmpty()) {
                    input_email.setError(getResources().getString(R.string.error_email));
                    return;
                }

                if (!Validation.isEmailValid(getEmail)) {
                    input_email.setError(getResources().getString(R.string.error_invalid_email));
                    //Singleton.getInstance(mContext).ShowToast(getResources().getString(R.string.error_email_invalid), coordinatorLayout);
                    return;
                }

                if(getContact == null || getContact.isEmpty()) {
                    input_contact.setError(getResources().getString(R.string.error_contact));
                    return;
                }

                if(getContact.length() < 13) {
                    input_contact.setError(getResources().getString(R.string.error_contact_10));
                    return;
                }

                if(getPass == null || getPass.isEmpty()) {
                    input_password.setError(getResources().getString(R.string.error_password));
                    return;
                }

                if (!CheckConnectivity.isConnected(mContext)) {
                    Singleton.getInstance(mContext).hideSoftKeyboard(this);
                    Singleton.getInstance(mContext).ShowToastMessage(getResources().getString(R.string.error_network_title), mContext);
                    return;
                }

                AuthorizationWebServices awService = new AuthorizationWebServices(mContext);
                awService.UserRegistrationRequest(getFirstName, getLastName, getContact, getEmail, getPass, getDob, getAnni, "","", true);


                break;

            default:
                break;

        }
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
    public void requestCompleted(String obj, int serviceCode ) {
        AppLog.Log("RegSuccess", obj.toString());
    }

    @Override
    public void requestEndedWithError(VolleyError error, int serviceCode ) {
        AppLog.Log("RegError", error.toString());

    }
}