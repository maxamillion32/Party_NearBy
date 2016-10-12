package com.app.partynearby;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.adaptors.HistoryAdaptor;
import com.app.adaptors.RatingAdaptor;
import com.app.interfaces.WebServiceInterface;
import com.app.pojo.HistoryItem;
import com.app.pojo.ReviewItem;
import com.app.utility.AppLog;
import com.app.utility.CheckConnectivity;
import com.app.utility.Constant;
import com.app.utility.SessionManager;
import com.app.utility.Singleton;
import com.app.webservices.AuthorizationWebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ram on 13/06/16.
 */
public class Rating extends AppCompatActivity implements WebServiceInterface{

    private Toolbar toolbar;
    private Context mContext;
    private RecyclerView recyclerView;
    private RatingAdaptor mAdapter;
    private List<ReviewItem> eventItemList;
    protected Handler handler;
    private TextView empty_txt;
    private ProgressBar loader;
    private ProgressDialog dialog;

    private SessionManager userSession;
    private HashMap<String, String> justPharmaUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);

        mContext = Rating.this;
        userSession = new SessionManager(getApplicationContext());
        justPharmaUser = userSession.getUserDetails();

        toolbar = (Toolbar) findViewById(R.id.toolbar_elevated);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.lb_reviews));
        }

        String eventId = Singleton.getInstance(mContext).ev_id;

        AppLog.Log("eventId: ", eventId +"");

        if (!CheckConnectivity.isConnected(mContext)) {
            Singleton.getInstance(mContext).ShowToastMessage(getResources().getString(R.string.error_network_title), mContext);
            return;
        }
        AuthorizationWebServices authorizationWebServices = new AuthorizationWebServices(this);
        authorizationWebServices.EventRatingServie(Integer.valueOf(eventId));

        empty_txt = (TextView) findViewById(R.id.empty_txt);
        loader = (ProgressBar) findViewById(R.id.loader);

        recyclerView = (RecyclerView) findViewById(R.id.frag_scrollableview);
        eventItemList = new ArrayList<ReviewItem>();
        handler = new Handler();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        //adapter = new RecycleAdaptor(list);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuItem item;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        item = menu.findItem(R.id.action_settings);
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
            case R.id.write_review:

                final Dialog dialog = new Dialog(Rating.this);
                // Include dialog.xml file
                dialog.setContentView(R.layout.dialog_view);
                // Set dialog title
                dialog.setTitle("Write Review");

                // set values for custom dialog components - text, image and button
                final EditText text = (EditText) dialog.findViewById(R.id.textDialog);
                final EditText textTitle = (EditText) dialog.findViewById(R.id.textTitle);

                dialog.show();
                //dialog.setCancelable(false);

                Button declineButton = (Button) dialog.findViewById(R.id.declineButton);

                final RatingBar ev_ratingBar = (RatingBar) dialog.findViewById(R.id.ev_ratingBar);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        String inputText = text.getText().toString();
                        String title = textTitle.getText().toString();
                        String uId  = justPharmaUser.get(SessionManager.KEY_ID);

                        if(uId == null || uId.isEmpty()) {
                            AlertDialog alertDialog = new AlertDialog.Builder(
                                    mContext,
                                    R.style.AlertDialogCustom_Destructive)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // Delete Action
                                            startActivity(new Intent(mContext, Login.class));
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // Cancel Action
                                        }
                                    })
                                    .setTitle("Alert !")
                                    .create();
                            alertDialog.setMessage("Login required");
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }
                        else if(title.length() == 0 || title.isEmpty()) {
                            Singleton.getInstance(mContext).ShowToastMessage("Please enter review title", mContext);
                        }

                        else if(inputText.length() == 0 && inputText.isEmpty()) {
                            Singleton.getInstance(mContext).ShowToastMessage("Please enter review", mContext);

                        }

                        else if(ev_ratingBar.getRating() == 0) {

                            Singleton.getInstance(mContext).ShowToastMessage("Please rate", mContext);
                        }

                        else if (!CheckConnectivity.isConnected(mContext)) {
                            Singleton.getInstance(mContext).ShowToastMessage(getResources().getString(R.string.error_network_title), mContext);
                        }

                        else {

                            WriteReviewService(uId, title, inputText, String.valueOf(ev_ratingBar.getRating()), Singleton.getInstance(mContext).ev_id);
                            dialog.dismiss();
                        }
                    }
                });

                ev_ratingBar.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            float touchPositionX = event.getX();
                            float width = ev_ratingBar.getWidth();
                            float starsf = (touchPositionX / width) * 5.0f;
                            int stars = (int)starsf + 1;
                            ev_ratingBar.setRating(stars);

                            //Toast.makeText(MainActivity.this, String.valueOf("test"), Toast.LENGTH_SHORT).show();
                            v.setPressed(false);
                        }
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            v.setPressed(true);
                        }

                        if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                            v.setPressed(false);
                        }
                        return true;
                    }});


                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void requestCompleted(String obj, int serviceCode) {
        if(obj != null) {

            AppLog.Log("EVENT_DETAILS: ", obj);
            loader.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(obj);

                JSONArray new_Json = jsonObject.getJSONArray("ratingList");

                for(int i = 0; i < new_Json.length(); i++) {
                    JSONObject jobj = new_Json.getJSONObject(i);

                    JSONObject jobRating = jobj.getJSONObject("Rating");

                    JSONObject jobProfile = jobj.getJSONObject("User");

                    String rat_id = jobRating.getString("id");
                    String rat_title = jobRating.getString("title");
                    String rating_count = jobRating.getString("rating");
                    String rat_review = jobRating.getString("review");
                    String date_added = jobRating.getString("date_added");
                    String profile_pic = jobProfile.getString("profile_pic");

                    eventItemList.add(new ReviewItem(rat_id, rat_title, rat_review, date_added, rating_count, profile_pic));
                }



                if(eventItemList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    empty_txt.setVisibility(View.VISIBLE);
                    empty_txt.setText("Review not found");
                } else {
                    mAdapter = new RatingAdaptor(eventItemList, recyclerView);
                    recyclerView.setAdapter(mAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void requestEndedWithError(VolleyError error, int errorCode) {

        loader.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        if(error != null) {
            empty_txt.setText(error.getMessage());
        }

    }




    private void WriteReviewService(String uId, String title, String desc, String revCount, String evId) {
        dialog = new ProgressDialog(Rating.this);
        dialog.setMessage(getResources().getString(R.string.progress_loading));
        dialog.show();
        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put(Constant.KeyConstant.USER_ID, uId);
            reqObj.put("title", title);
            reqObj.put("description", desc);
            reqObj.put("rating_count", revCount);
            reqObj.put("event_id", evId);


            AppLog.Log("WriteRev_Req: ", reqObj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppLog.Log("URL", Constant.ServiceType.ADD_RATING);
        RequestQueue queue = MyApplication.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constant.ServiceType.ADD_RATING , reqObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AppLog.Log("Rat_response: ", response.toString());
                        if(dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if(response != null) {
                            try {
                                JSONObject jsonObject  = new JSONObject(response.toString());
                                String msg = jsonObject.getString("message");
                                if(msg != null && msg.contains("Saved")) {
                                    Singleton.getInstance(mContext).ShowToastMessage("Review has been sent. It will be visible once admin has approved", mContext);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        if (error != null) {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                VolleyError newerror = new VolleyError(new String(error.networkResponse.data));
                                error = newerror;

                            }
                            AppLog.Log("History_error: ", error.toString());
                            Singleton.getInstance(mContext).ShowToastMessage("Review has not been sent. Please try again", mContext);
                        }
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);

    }
}
