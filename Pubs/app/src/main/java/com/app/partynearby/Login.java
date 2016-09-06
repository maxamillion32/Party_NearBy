package com.app.partynearby;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.app.interfaces.WebServiceInterface;
import com.app.utility.AppLog;
import com.app.utility.CheckConnectivity;
import com.app.utility.Constant;
import com.app.utility.SessionManager;
import com.app.utility.Singleton;
import com.app.utility.Validation;
import com.app.webservices.AuthorizationWebServices;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.internal.ImageRequest;
import com.facebook.internal.Utility;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Vijay on 18/05/16.
 */
public class Login extends AppCompatActivity implements View.OnClickListener, WebServiceInterface, GoogleApiClient.OnConnectionFailedListener{

    private EditText input_email, input_password ;
    private Button btn_signup, reg, facebook;
    SignInButton btn_sign_in_google;
    private TextView forgot_pass;
    private Toolbar toolbar;
    private Context mContext;
    CallbackManager callbackManager;
    LoginButton login_button_facebook;
    private String fbID = null, fbEmail = null, fbDob = "", fbName = null, photoUrl = null;
    String fname = null, lname = null;
    GoogleApiClient mGoogleApiClient;
    private static final int GOOGLE_LOGIN_CODE = 9001;
    String responseMessage = null;
    private SessionManager sessionManager;
    private AuthorizationWebServices authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mContext = Login.this;
        sessionManager = new SessionManager(getApplicationContext());
        authService = new AuthorizationWebServices(mContext);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        toolbar = (Toolbar) findViewById(R.id.toolbar_elevated);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getResources().getString(R.string.lb_login));
        }
        initWidget();



        callbackManager = CallbackManager.Factory.create();
        //List< String > permissionNeeds = Arrays.asList("user_photos", "email",
          //      "user_birthday", "public_profile");

        List< String > permissionNeeds = Arrays.asList("email",
                "user_birthday", "public_profile");


        login_button_facebook.setReadPermissions(permissionNeeds);
        login_button_facebook.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {@Override
                public void onSuccess(LoginResult loginResult) {

                    System.out.println("onSuccess");

                    String accessToken = loginResult.getAccessToken()
                            .getToken();
                    Log.i("accessToken", accessToken);

                    //Profile profile = Profile.getCurrentProfile();

                    //AppLog.Log("IMG:", profile.getFirstName() +", "+profile.getProfilePictureUri(200, 200));

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {@Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {

                                Log.i("LoginActivity",
                                        response.toString());
                                try {
                                    fbID = object.getString("id");
                                    try {
                                        URL profile_pic = new URL(
                                                "http://graph.facebook.com/" + fbID + "/picture?height=100&type=normal&width=100&redirect=0");
                                        Log.i("profile_pic",
                                                profile_pic + "");
                                        photoUrl = profile_pic.toString();

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    //fname = object.getString("first_name");
                                    //lname = object.getString("last_name");
                                    fbName = object.getString("name");
                                    fbEmail = object.getString("email");
                                    //String pic = object.getString("picture");
                                    //fGender = object.getString("gender");
                                    //fbDob = object.getString("birthday");

                                    //String[] splited = str.split("\\s+");

                                    AppLog.Log("FacebookData: ", fbName+" ,"+fbDob +" ,"+fbEmail+" ,"+photoUrl);
                                    Singleton.getInstance(mContext).userName = fbName;
                                    Singleton.getInstance(mContext).userDOB = fbDob;
                                    Singleton.getInstance(mContext).userEmail = fbEmail;
                                    Singleton.getInstance(mContext).userPhoto = photoUrl;
                                    importFbProfilePhoto();
                                    //AuthorizationWebServices auth = new AuthorizationWebServices(mContext);
                                    //auth.GETFacebookProfileImage(photoUrl);
                                    //importFbProfilePhoto();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields",
                            "id,name,email,gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                    @Override
                    public void onCancel() {

                        System.out.println("onCancel");
                        Singleton.getInstance(mContext).ShowToastMessage("Login cancel, try again !", mContext);
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Log.v("onError", exception.getCause() +"");
                        Singleton.getInstance(mContext).ShowToastMessage(exception.getCause().toString(), mContext);
                    }
                });

    }

    private void nextActivity(int from, String uId, int fbStatus, int googleStatus, String access_token,
                              String mobile, String name, String email, String password,  String dob, String pic){
        String fname = null;
        String lname = null;

        if(from == Constant.ServiceCodeAccess.FACEBOOK_LOGIN || from == Constant.ServiceCodeAccess.GOOGLE_LOGIN) {


            if(name != null && email != null){

                if(name != null) {
                    String[] splited = name.split("\\s+");
                    List<String> itemList = Arrays.asList(splited);
                    if(itemList.size() == 0) {

                    } else if(itemList.size() == 1) {
                        fname = itemList.get(0);
                    } else if(itemList.size() == 2) {
                        fname = itemList.get(0);
                        lname = itemList.get(1);

                    }
                }
                AppLog.Log("Name: ", fname+ " "+ lname);

            }

        } else if(from == Constant.ServiceCodeAccess.LOGIN) {

        }
        AppLog.Log("Req_data: ", fname+", " +lname+ ","  +dob +", " +fbStatus+ "," +googleStatus +" ," +email+ "," +password);
        authService.UserLoginRequest(fbName, "", dob, fbStatus, googleStatus, email, password, true);

    }


    private void importFbProfilePhoto() {

        if (AccessToken.getCurrentAccessToken() != null) {

            GraphRequest request = GraphRequest.newMeRequest(
                    AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {

                            if (AccessToken.getCurrentAccessToken() != null) {

                                if (me != null) {
                                    if(ImageRequest.getProfilePictureUri(me.optString("id"), 500, 500) != null) {
                                        String profileImageUrl = ImageRequest.getProfilePictureUri(me.optString("id"), 500, 500).toString();
                                        Log.i("Vij_IMG", profileImageUrl+ ", "+ response.toString());
                                        Singleton.getInstance(mContext).userPhoto = profileImageUrl;
                                        nextActivity(Constant.ServiceCodeAccess.FACEBOOK_LOGIN, "", 1, 0,
                                                "", "", fbName, fbEmail, "", fbDob, Singleton.getInstance(mContext).userPhoto);
                                    }


                                    //JSONObject graphObject = response.getJSONObject();
                                  /*  if (graphObject != null) {

                                        //Parse graphObject to User
                                        //JSONObject jsonUser = graphUser.getInnerJSONObject();
                                        try {
                                            //mUser.setEmail(jsonUser.getString(EMAIL));
                                            String img = graphObject.getJSONObject("picture").getJSONObject("data").getString("url");
                                            AppLog.Log("IMG_FB: ",img);
                                            *//*mUser.setName(jsonUser.getString("first_name"));
                                            mUser.setSurname(jsonUser.getString("last_name"));
                                            mUser.setUsername(jsonUser.getString("username"));*//*
                                        } catch (JSONException e) {
                                            Log.e("Error_exe", e.toString());
                                            e.printStackTrace();
                                        }
                                    }*/

                                }
                            }
                        }
                    });
            GraphRequest.executeBatchAsync(request);
        }
    }

    void initWidget() {

        login_button_facebook = (LoginButton) findViewById(R.id.login_button);

        input_email = (EditText) findViewById(R.id.input_email);
        input_password = (EditText) findViewById(R.id.input_password);

        btn_signup = (Button) findViewById(R.id.btn_signup);
        facebook = (Button) findViewById(R.id.facebook);
        btn_sign_in_google = (SignInButton) findViewById(R.id.btn_sign_in);
        reg = (Button) findViewById(R.id.reg);

        forgot_pass = (TextView) findViewById(R.id.forgot_pass);

        btn_signup.setOnClickListener(this);
        facebook.setOnClickListener(this);
        btn_sign_in_google.setOnClickListener(this);
        reg.setOnClickListener(this);
        forgot_pass.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(
                    "com.app.partynearby",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException ignored) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);

        if (requestCode == GOOGLE_LOGIN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        AppLog.Log("result: ", result +"");
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //tv_username.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));

            if(acct != null) {
                fbEmail = acct.getEmail();
                fbName = acct.getDisplayName();

                if(acct.getPhotoUrl() != null) {
                    photoUrl = acct.getPhotoUrl().toString();
                }

                AppLog.Log("GoogleData: ", fbName + " ," + fbDob + " ," + fbEmail + " ," + photoUrl);
                Singleton.getInstance(mContext).userName = fbName;
                Singleton.getInstance(mContext).userEmail = fbEmail;
                Singleton.getInstance(mContext).userPhoto = photoUrl;
                nextActivity(Constant.ServiceCodeAccess.GOOGLE_LOGIN,"", 0, 1, "", "",
                        fbName,fbEmail ,"", fbDob, Singleton.getInstance(mContext).userPhoto);
            }

        } else {
            AppLog.Log("Google: ", "error");
            Singleton.getInstance(mContext).ShowToastMessage("Error in login", mContext);
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signup:

               String getEmail = input_email.getText().toString();
                String getPass = input_password.getText().toString().trim();

                if(getEmail == null || getEmail.isEmpty()) {
                    input_email.setError(getResources().getString(R.string.error_email));
                    return;
                }

                if (!Validation.isEmailValid(getEmail)) {
                    input_email.setError(getResources().getString(R.string.error_invalid_email));
                    //Singleton.getInstance(mContext).ShowToast(getResources().getString(R.string.error_email_invalid), coordinatorLayout);
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

                Singleton.getInstance(mContext).hideSoftKeyboard(this);
                authService.UserLoginRequest("","","",0,0,getEmail, getPass, true);



                break;
            case R.id.facebook:
                if (!CheckConnectivity.isConnected(mContext)) {
                    Singleton.getInstance(mContext).hideSoftKeyboard(this);
                    Singleton.getInstance(mContext).ShowToastMessage(getResources().getString(R.string.error_network_title), mContext);
                    return;
                }
                login_button_facebook.performClick();

                break;
            case R.id.btn_sign_in:
                if (!CheckConnectivity.isConnected(mContext)) {
                    Singleton.getInstance(mContext).hideSoftKeyboard(this);
                    Singleton.getInstance(mContext).ShowToastMessage(getResources().getString(R.string.error_network_title), mContext);
                    return;
                }

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE);

                break;
            case R.id.reg:
                startActivity(new Intent(getApplicationContext(), Registration.class));

                break;
            case R.id.forgot_pass:
                startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
                break;

            default:
                break;

        }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

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
    public void requestCompleted(String obj, int serviceCode ) {
        AppLog.Log("Success_Data: ", obj);

        if(serviceCode == Constant.ServiceCodeAccess.LOGIN) {
            AppLog.Log("LoginSuccess: ", obj);
            if(obj != null && !obj.isEmpty()) {

                try {
                    JSONObject jsonObject = new JSONObject(obj);
                    JSONObject jUserData = jsonObject.getJSONObject("userdata");
                    JSONObject jUser = jUserData.getJSONObject("User");

                    String user_id = jUser.getString("id");
                    String access_token = jUser.getString(Constant.KeyConstant.ACCESS_TOKEN);
                    String profile_pic = jUser.getString(Constant.KeyConstant.PROFILE_PIC);
                    String first_name = jUser.getString(Constant.KeyConstant.FIRST_NAME);
                    String last_name = jUser.getString(Constant.KeyConstant.LAST_NAME);
                    String email_id = jUser.getString(Constant.KeyConstant.EMAIL_ID);
                    String mobile_no = jUser.getString(Constant.KeyConstant.MOBILE_NUMBER);
                    String dob = jUser.getString(Constant.KeyConstant.DOB);
                    AppLog.Log("user_id", user_id);
                    String name = first_name +" "+ last_name;
                    if(user_id != null && !user_id.isEmpty()) {
                        //nextActivity(Constant.ServiceCodeAccess.LOGIN, user_id, 0, 0, access_token, mobile_no, name, email_id, "", fbDob, profile_pic);
                         sessionManager.createLoginSession(user_id, access_token, first_name, last_name, mobile_no, email_id, dob, Singleton.getInstance(mContext).userPhoto);
                            Intent main = new Intent(getApplicationContext(), MainActivity.class);
                            //main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            main.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(main);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void requestEndedWithError(VolleyError error, int errorCode) {
        AppLog.Log("LoginError: ", error.toString());

        if(errorCode == Constant.ServiceCodeAccess.FACEBOOK_PROFILE) {

        } else if(errorCode == Constant.ServiceCodeAccess.LOGIN) {
            if(error.getMessage() != null) {
                try {
                    JSONObject jsonObject = new JSONObject(error.getMessage());
                    responseMessage = jsonObject.getString(Constant.KeyConstant.MESSAGE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Singleton.getInstance(mContext).ShowToastMessage(responseMessage, mContext);
        }


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        //tv_username.setText("");
                    }
                });
    }

}