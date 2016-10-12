package com.app.partynearby;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.app.webservices.AuthorizationWebServices;

import java.util.HashMap;

/**
 * Created by ram on 21/05/16.
 */
public class Booking extends AppCompatActivity implements View.OnClickListener, WebServiceInterface {

    private Button book_now;
    private Toolbar toolbar;
    private Context mContext;
    private TextView counter_girl, counter_male, total_amount, couple_entry_value, female_entry_value, male_entry_value;
    private TextView couple_entry_counter, female_entry_counter, male_entry_counter;
    private FloatingActionButton female_remove, male_remove, female_add, male_add;

    int minBoysCountValue = 0;
    int minGirlsCountValue = 0;
    int totalPersonCount = 0;
    private final static int COUPLE_ENTRY = 1000;
    private final static int BOYS_ENTRY = 1000;
    private final static int GIRL_ENTRY = 0;

    int filteredCouple = 0;
    int filteredBoys = 0;
    int filteredGirls = 0;
    int totalCouplePerson = 0;


    private SessionManager userSession;
    private HashMap<String, String> justPharmaUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking);
        mContext = Booking.this;


        toolbar = (Toolbar) findViewById(R.id.toolbar_elevated);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(getResources().getString(R.string.guest_list));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        userSession = new SessionManager(getApplicationContext());
        justPharmaUser = userSession.getUserDetails();

        book_now = (Button) findViewById(R.id.book_now);

        female_remove = (FloatingActionButton) findViewById(R.id.female_remove);
        male_remove = (FloatingActionButton) findViewById(R.id.male_remove);
        female_add = (FloatingActionButton) findViewById(R.id.female_add);
        male_add = (FloatingActionButton) findViewById(R.id.male_add);

        counter_girl = (TextView) findViewById(R.id.counter_girl);
        counter_male = (TextView) findViewById(R.id.counter_male);
        total_amount = (TextView) findViewById(R.id.total_amount);

        couple_entry_value = (TextView) findViewById(R.id.couple_entry_value);
        female_entry_value = (TextView) findViewById(R.id.female_entry_value);
        male_entry_value = (TextView) findViewById(R.id.male_entry_value);

        couple_entry_counter = (TextView) findViewById(R.id.couple_entry_counter);
        female_entry_counter = (TextView) findViewById(R.id.female_entry_counter);
        male_entry_counter = (TextView) findViewById(R.id.male_entry_counter);

        book_now.setOnClickListener(this);

        female_remove.setOnClickListener(this);
        male_remove.setOnClickListener(this);
        female_add.setOnClickListener(this);
        male_add.setOnClickListener(this);
        initWidget();
    }


    void initWidget() {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.book_now:

               /* String getEmail = input_email.getText().toString();
                String getPass = input_password.getText().toString().trim();

                if(getEmail == null || getEmail.isEmpty()) {
                    input_email.setError(getResources().getString(R.string.error_email));
                    return;
                }

*/
                String getTotalAmount = total_amount.getText().toString();
                if (!CheckConnectivity.isConnected(mContext)) {
                    Singleton.getInstance(mContext).hideSoftKeyboard(this);
                    Singleton.getInstance(mContext).ShowToastMessage(getResources().getString(R.string.error_network_title), mContext);
                    return;
                }

                if(getTotalAmount != null && getTotalAmount.equalsIgnoreCase("0")) {
                    Singleton.getInstance(mContext).ShowToastMessage("Please select member", mContext);
                    return;
                }
              /*  if(filteredCouple == 0 || filteredGirls == 0 || filteredBoys == 0) {
                    Singleton.getInstance(mContext).ShowToastMessage("Please select member", mContext);
                    return;
                }*/

                String uId  = justPharmaUser.get(SessionManager.KEY_ID);
                String evId = Singleton.getInstance(mContext).ev_id;
                AuthorizationWebServices auth = new AuthorizationWebServices(this);
                auth.BookEventService(uId, evId, String.valueOf(filteredCouple), String.valueOf(filteredGirls), String.valueOf(filteredBoys));


                break;

            case R.id.female_remove:

                if(minGirlsCountValue > 0) {
                    minGirlsCountValue--;
                    totalPersonCount = minBoysCountValue + minGirlsCountValue;
                }

                AppLog.Log("minGirlsCountValue(-):", minGirlsCountValue+"");
                counter_girl.setText(""+minGirlsCountValue);

                if(minBoysCountValue > minGirlsCountValue) {
                    filteredCouple = minGirlsCountValue;
                    totalCouplePerson = (filteredCouple * 2);
                    filteredGirls = (minGirlsCountValue - filteredCouple);
                    filteredBoys = (totalPersonCount - totalCouplePerson);

                } else if(minGirlsCountValue > minBoysCountValue) {
                    filteredCouple = minBoysCountValue;
                    totalCouplePerson = (filteredCouple * 2);
                    filteredGirls = (totalPersonCount - totalCouplePerson);
                    filteredBoys = (minBoysCountValue - filteredCouple);

                }
                else if(minGirlsCountValue == minBoysCountValue) {
                    filteredCouple = minBoysCountValue;
                    totalCouplePerson = (filteredCouple * 2);
                    filteredGirls = (totalPersonCount - totalCouplePerson);
                    filteredBoys = (minBoysCountValue - filteredCouple);

                }

                AppLog.Log("Data: ", "Couple: "+ filteredCouple +"Couple_Person: "+ totalCouplePerson
                        +"Boys: "+ filteredBoys
                        +"Girls: "+filteredGirls);
                couple_entry_counter.setText(filteredCouple + "X " + Constant.COUPLE_ENTRY);
                female_entry_counter.setText(filteredGirls + "X " + Constant.FEMALE_ENTRY);
                male_entry_counter.setText(filteredBoys + "X " + Constant.MALE_ENTRY);

                couple_entry_value.setText("" + filteredCouple * Constant.COUPLE_ENTRY);
                female_entry_value.setText("" + filteredGirls * Constant.FEMALE_ENTRY);
                male_entry_value.setText("" + filteredBoys * Constant.MALE_ENTRY);




                total_amount.setText(""+ (Integer.valueOf(couple_entry_value.getText().toString())
                        + Integer.valueOf(female_entry_value.getText().toString())
                        + Integer.valueOf(male_entry_value.getText().toString())));

                break;
            case R.id.male_remove:

                if(minBoysCountValue > 0) {
                    minBoysCountValue--;
                    totalPersonCount = minBoysCountValue + minGirlsCountValue;
                }
                AppLog.Log("minBoysCountValue:(-)", minBoysCountValue+"");
                counter_male.setText(""+minBoysCountValue);

                if(minBoysCountValue > minGirlsCountValue) {
                    filteredCouple = minGirlsCountValue;
                    totalCouplePerson = (filteredCouple * 2);
                    filteredGirls = (minGirlsCountValue - filteredCouple);
                    filteredBoys = (totalPersonCount - totalCouplePerson);

                } else if(minGirlsCountValue > minBoysCountValue) {
                    filteredCouple = minBoysCountValue;
                    totalCouplePerson = (filteredCouple * 2);
                    filteredGirls = (totalPersonCount - totalCouplePerson);
                    filteredBoys = (minBoysCountValue - filteredCouple);

                }
                else if(minGirlsCountValue == minBoysCountValue) {
                    filteredCouple = minBoysCountValue;
                    totalCouplePerson = (filteredCouple * 2);
                    filteredGirls = (totalPersonCount - totalCouplePerson);
                    filteredBoys = (minBoysCountValue - filteredCouple);

                }

                AppLog.Log("Data: ", "Couple: "+ filteredCouple +"Couple_Person: "+ totalCouplePerson
                        +"Boys: "+ filteredBoys
                        +"Girls: "+filteredGirls);
                couple_entry_counter.setText(filteredCouple + "X " + Constant.COUPLE_ENTRY);
                female_entry_counter.setText(filteredGirls + "X " + Constant.FEMALE_ENTRY);
                male_entry_counter.setText(filteredBoys + "X " + Constant.MALE_ENTRY);

                couple_entry_value.setText("" + filteredCouple * Constant.COUPLE_ENTRY);
                female_entry_value.setText("" + filteredGirls * Constant.FEMALE_ENTRY);
                male_entry_value.setText("" + filteredBoys * Constant.MALE_ENTRY);

                total_amount.setText(""+ (Integer.valueOf(couple_entry_value.getText().toString())
                        + Integer.valueOf(female_entry_value.getText().toString())
                        + Integer.valueOf(male_entry_value.getText().toString())));

                break;
            case R.id.female_add:
                minGirlsCountValue++;
                totalPersonCount = minBoysCountValue + minGirlsCountValue;
                AppLog.Log("minGirlsCountValue(+):", minGirlsCountValue+"");
                counter_girl.setText(""+minGirlsCountValue);


                if(minBoysCountValue > minGirlsCountValue) {
                    filteredCouple = minGirlsCountValue;
                    totalCouplePerson = (filteredCouple * 2);
                    filteredGirls = (minGirlsCountValue - filteredCouple);
                    filteredBoys = (totalPersonCount - totalCouplePerson);

                } else if(minGirlsCountValue > minBoysCountValue) {
                    filteredCouple = minBoysCountValue;
                    totalCouplePerson = (filteredCouple * 2);
                    filteredGirls = (totalPersonCount - totalCouplePerson);
                    filteredBoys = (minBoysCountValue - filteredCouple);

                }
                else if(minGirlsCountValue == minBoysCountValue) {
                    filteredCouple = minBoysCountValue;
                    totalCouplePerson = (filteredCouple * 2);
                    filteredGirls = (totalPersonCount - totalCouplePerson);
                    filteredBoys = (minBoysCountValue - filteredCouple);

                }

                AppLog.Log("Data: ", "Couple: "+ filteredCouple +"Couple_Person: "+ totalCouplePerson
                        +"Boys: "+ filteredBoys
                        +"Girls: "+filteredGirls);
                couple_entry_counter.setText(filteredCouple + "X " + Constant.COUPLE_ENTRY);
                female_entry_counter.setText(filteredGirls + "X " + Constant.FEMALE_ENTRY);
                male_entry_counter.setText(filteredBoys + "X " + Constant.MALE_ENTRY);

                couple_entry_value.setText("" + filteredCouple * Constant.COUPLE_ENTRY);
                female_entry_value.setText("" + filteredGirls * Constant.FEMALE_ENTRY);
                male_entry_value.setText("" + filteredBoys * Constant.MALE_ENTRY);

                total_amount.setText(""+ (Integer.valueOf(couple_entry_value.getText().toString())
                        + Integer.valueOf(female_entry_value.getText().toString())
                        + Integer.valueOf(male_entry_value.getText().toString())));

                break;
            case R.id.male_add:
                minBoysCountValue++;
                totalPersonCount = minBoysCountValue + minGirlsCountValue;
                AppLog.Log("minBoysCountValue(+):", minBoysCountValue+"");
                counter_male.setText(""+minBoysCountValue);

                if(minBoysCountValue > minGirlsCountValue) {
                    filteredCouple = minGirlsCountValue;
                    totalCouplePerson = (filteredCouple * 2);
                    filteredGirls = (minGirlsCountValue - filteredCouple);
                    filteredBoys = (totalPersonCount - totalCouplePerson);

                } else if(minGirlsCountValue > minBoysCountValue) {
                    filteredCouple = minBoysCountValue;
                    totalCouplePerson = (filteredCouple * 2);
                    filteredGirls = (totalPersonCount - totalCouplePerson);
                    filteredBoys = (minBoysCountValue - filteredCouple);

                }

                else if(minGirlsCountValue == minBoysCountValue) {
                    filteredCouple = minBoysCountValue;
                    totalCouplePerson = (filteredCouple * 2);
                    filteredGirls = (totalPersonCount - totalCouplePerson);
                    filteredBoys = (minBoysCountValue - filteredCouple);

                }

                AppLog.Log("Data: ", "Couple: "+ filteredCouple +"Couple_Person: "+ totalCouplePerson
                       +"Boys: "+ filteredBoys
                        +"Girls: "+filteredGirls);
                couple_entry_counter.setText(filteredCouple + "X " + Constant.COUPLE_ENTRY);
                female_entry_counter.setText(filteredGirls + "X " + Constant.FEMALE_ENTRY);
                male_entry_counter.setText(filteredBoys + "X " + Constant.MALE_ENTRY);

                couple_entry_value.setText("" + filteredCouple * Constant.COUPLE_ENTRY);
                female_entry_value.setText("" + filteredGirls * Constant.FEMALE_ENTRY);
                male_entry_value.setText("" + filteredBoys * Constant.MALE_ENTRY);

                total_amount.setText(""+ (Integer.valueOf(couple_entry_value.getText().toString())
                        + Integer.valueOf(female_entry_value.getText().toString())
                        + Integer.valueOf(male_entry_value.getText().toString())));

                break;

            default:
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        MenuItem item3;
        item3 = menu.findItem(R.id.write_review);
        item3.setVisible(false);
        return true;
    }

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
        AppLog.Log("Success_Data: ", obj);

        Intent main = new Intent(getApplicationContext(), BookingHistory.class);
        //main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        main.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(main);
    }

    @Override
    public void requestEndedWithError(VolleyError error, int errorCode) {
        AppLog.Log("Booking_error: ", error.toString());
    }
}

