package com.app.pubs;

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

import com.app.utility.AppLog;
import com.app.utility.Constant;

/**
 * Created by ram on 21/05/16.
 */
public class Booking extends AppCompatActivity implements View.OnClickListener {

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

                if (!CheckConnectivity.isConnected(mContext)) {
                    Singleton.getInstance(mContext).hideSoftKeyboard(this);
                    Singleton.getInstance(mContext).ShowToast(getResources().getString(R.string.error_network), coordinatorLayout);
                    return;
                }
*/
                startActivity(new Intent(getApplicationContext(), MainActivity.class));


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
}

