package com.app.pubs;

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

import com.app.utility.SessionManager;

/**
 * Created by ram on 21/05/16.
 */
public class EventDetails extends AppCompatActivity {

    boolean showFAB = true;
    private Toolbar toolbar;
    private LinearLayout rating_lay;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        sessionManager = new SessionManager(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.gmail_toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.event_details));
        }

        rating_lay = (LinearLayout) findViewById(R.id.rating_lay);

        rating_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Rating.class));
            }
        });




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
}
