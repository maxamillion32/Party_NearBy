package com.app.partynearby;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app.interfaces.WebServiceInterface;
import com.app.utility.AppLog;
import com.app.utility.CheckConnectivity;
import com.app.utility.Constant;
import com.app.utility.SessionManager;
import com.app.utility.Singleton;
import com.app.utility.VolleyImageUtlil;
import com.app.webservices.AuthorizationWebServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ram on 21/05/16.
 */
public class EventDetails extends AppCompatActivity implements WebServiceInterface{

    boolean showFAB = true;
    private Toolbar toolbar;
    private LinearLayout rating_lay;
    private SessionManager sessionManager;
    private Context mContext;
    private ImageLoader mImageLoader;

    private NetworkImageView eventImg;
    private TextView ev_desc, ev_name, ev_address, ev_contact, ev_datetime, ev_rating_count,
            entry_type, ev_cost;
    private RatingBar ev_ratingBar;
    private ProgressBar loader;
    private LinearLayout data_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        sessionManager = new SessionManager(getApplicationContext());
        mContext = EventDetails.this;
        mImageLoader = VolleyImageUtlil.getInstance(getApplicationContext())
                .getImageLoader();

        toolbar = (Toolbar) findViewById(R.id.gmail_toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.event_details));
        }

        rating_lay = (LinearLayout) findViewById(R.id.rating_lay);
        eventImg = (NetworkImageView) findViewById(R.id.eventImg);
        ev_ratingBar = (RatingBar) findViewById(R.id.ev_ratingBar);

        ev_desc = (TextView) findViewById(R.id.ev_desc);
        ev_name = (TextView) findViewById(R.id.ev_name);
        ev_address = (TextView) findViewById(R.id.ev_address);
        ev_contact = (TextView) findViewById(R.id.ev_contact);
        ev_datetime = (TextView) findViewById(R.id.ev_datetime);
        ev_rating_count = (TextView) findViewById(R.id.ev_rating_count);
        ev_cost = (TextView) findViewById(R.id.ev_cost);
        entry_type = (TextView) findViewById(R.id.entry_type);

        loader = (ProgressBar) findViewById(R.id.loader);
        data_rl = (LinearLayout) findViewById(R.id.data_rl);




        updateEventImageUI();

        rating_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Rating.class));
            }
        });

        String eventId = Singleton.getInstance(mContext).ev_id;

        AppLog.Log("eventId: ", eventId +"");

        if (!CheckConnectivity.isConnected(mContext)) {
            Singleton.getInstance(mContext).hideSoftKeyboard(this);
            Singleton.getInstance(mContext).ShowToastMessage(getResources().getString(R.string.error_network_title), mContext);
            return;
        }

        AuthorizationWebServices authorizationWebServices = new AuthorizationWebServices(mContext);
        authorizationWebServices.EventDetailsService(Integer.valueOf(eventId));




        /**
         * Bottom Sheet
         */

        // To handle FAB animation upon entrance and exit
        final Animation growAnimation = AnimationUtils.loadAnimation(this, R.anim.simple_grow);
        final Animation shrinkAnimation = AnimationUtils.loadAnimation(this, R.anim.simple_shrink);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.gmail_fab);

        fab.setVisibility(View.VISIBLE);
        fab.startAnimation(growAnimation);


        shrinkAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.gmail_coordinator);
        View bottomSheet = coordinatorLayout.findViewById(R.id.gmail_bottom_sheet);

        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                switch (newState) {

                    case BottomSheetBehavior.STATE_DRAGGING:
                        if (showFAB)
                            fab.startAnimation(shrinkAnimation);
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        showFAB = true;
                        fab.setVisibility(View.VISIBLE);
                        fab.startAnimation(growAnimation);
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:
                        showFAB = false;
                        break;


                }

            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sessionManager.isLoggedIn()) {
                    startActivity(new Intent(getApplicationContext(), Booking.class));
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            v.getContext(),
                            R.style.AlertDialogCustom_Destructive)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Delete Action
                                    startActivity(new Intent(getApplicationContext(), Login.class));
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


            }
        });

    }



    private void updateEventImageUI() {
        final String url = Constant.ServiceType.IMAGE_BASE_URL+Singleton.getInstance(mContext).ev_thumbnail;
        mImageLoader.get(url, ImageLoader.getImageListener(eventImg,
                R.mipmap.ic_launcher, android.R.drawable
                        .ic_dialog_alert));
        eventImg.setImageUrl(url, mImageLoader);

        entry_type.setText(Singleton.getInstance(mContext).ev_entrytype);
        ev_cost.setText(Singleton.getInstance(mContext).ev_cost);
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
                break;
           /* case R.id.action_settings:

                // Intent i = new Intent(getApplicationContext(), SelectChemist.class);
                //startActivity(i);

                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void requestCompleted(String obj, int serviceCode) {

        if(obj != null) {
            loader.setVisibility(View.GONE);
            data_rl.setVisibility(View.VISIBLE);
            String responseData = obj.toString();

            AppLog.Log("EVENT_DETAILS: ", responseData);

            try {
                JSONObject jsonObject = new JSONObject(responseData);

                JSONObject new_Json = jsonObject.getJSONObject("eventDetails");

                String event_name = new_Json.getString("event_name");
                String rating_avg = new_Json.getString("rating_avg");
                String total_review_count = new_Json.getString("total_review_count");
                String event_address = new_Json.getString("event_address");
                String entry_time = new_Json.getString("entry_time");
                String event_datetime = new_Json.getString("event_datetime");
                String event_contact_no = new_Json.getString("event_contact_no");
                String event_description = new_Json.getString("event_description");


                if(event_name != null && !event_name.isEmpty()) {
                    ev_name.setText(event_name);
                }

                if(event_address != null && !event_address.isEmpty()) {
                    ev_address.setText(event_address);
                }

                if(entry_time != null && !entry_time.isEmpty() && event_datetime != null) {
                    ev_datetime.setText(entry_time +" " +event_datetime);
                }

                if(event_contact_no != null && !event_contact_no.isEmpty()) {
                    ev_contact.setText(event_contact_no);
                }

                if(event_description != null && !event_description.isEmpty()) {
                    ev_desc.setText(event_description);
                }

                if(rating_avg != null && !rating_avg.isEmpty() && !rating_avg.equalsIgnoreCase("null")) {

                    ev_ratingBar.setRating(Float.valueOf(rating_avg));
                    ev_rating_count.setText(rating_avg +"(" + total_review_count + ")");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void requestEndedWithError(VolleyError error, int errorCode) {

        AppLog.Log("Error_Data: ", error.toString());
        loader.setVisibility(View.GONE);

    }
}
