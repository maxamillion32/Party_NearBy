package com.app.partynearby;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.adaptors.NearByAdaptor;
import com.app.adaptors.PubsDataAdaptor;
import com.app.adaptors.RatingAdaptor;
import com.app.pojo.EventListItem;
import com.app.pojo.ReviewItem;
import com.app.utility.AppLog;
import com.app.utility.Constant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by thinksysuser on 15-06-2016.
 */
public class NearBy extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    private Toolbar toolbar;
    private Context mContext;
    private RecyclerView recyclerView;
    private PubsDataAdaptor mAdapter;
    private List<EventListItem> eventItemList;
    protected Handler handler;
    private ProgressBar loader;
    private TextView empty_txt;

    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private GoogleApiClient googleApiClient;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);

        mContext = NearBy.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar_elevated);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.lb_nearby));
        }

        recyclerView = (RecyclerView) findViewById(R.id.frag_scrollableview);
        loader = (ProgressBar) findViewById(R.id.loader);
        empty_txt = (TextView) findViewById(R.id.empty_txt);
        eventItemList = new ArrayList<EventListItem>();
        handler = new Handler();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();

        //GetNearByEventService(78.3910265, 23.5355161);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
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

    private void GetNearByEventService(double lat, double lon) {

        //dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        //dialog.show();
        JSONObject reqObj = new JSONObject();

        try {
            reqObj.put(Constant.KeyConstant.LONGITUDE, lat);
            reqObj.put(Constant.KeyConstant.LATITUDE, lon);

            AppLog.Log("Near_Req: ", reqObj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppLog.Log("URL", Constant.ServiceType.NEAR_BY);
        RequestQueue queue = MyApplication.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constant.ServiceType.NEAR_BY , reqObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AppLog.Log("tod_response: ", response.toString());

                        empty_txt.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        loader.setVisibility(View.GONE);
                        if(response != null) {
                            eventItemList.clear();

                            String jsonRes = response.toString();
                            try {
                                JSONObject jsonObject = new JSONObject(jsonRes);
                                JSONArray jsonArray = jsonObject.getJSONArray("allEvents");

                                if(jsonArray != null) {

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jObj = jsonArray.getJSONObject(i).getJSONObject("EventList");
                                        String id = jObj.getString("id");
                                        String event_image = jObj.getString("event_image");
                                        String event_name = jObj.getString("event_name");
                                        String event_address = jObj.getString("event_address");
                                        String event_time = jObj.getString("event_time");
                                        String event_datetime = jObj.getString("event_datetime");
                                        String event_contact_no = jObj.getString("event_contact_no");
                                        String event_description = jObj.getString("event_description");
                                        String entry_cost = jObj.getString("entry_cost");
                                        String entry_type = jObj.getString("entry_type");
                                        String discount = jObj.getString("discount");
                                        String date_added = jObj.getString("date_added");
                                        if(event_image != null && !event_image.isEmpty()) {
                                            event_image = event_image.replaceAll(" ", "%20");
                                        }

                                        eventItemList.add(new EventListItem(date_added, discount, entry_type, event_description,
                                                event_contact_no, event_time, id, event_name, event_address,
                                                event_datetime, entry_cost, event_image));
                                    }
                                } else {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if(eventItemList.isEmpty()) {
                                loader.setVisibility(View.GONE);
                                empty_txt.setVisibility(View.VISIBLE);
                                empty_txt.setText(getResources().getString(R.string.error_event));
                            } else {
                                mAdapter = new PubsDataAdaptor(eventItemList, recyclerView);
                                recyclerView.setAdapter(mAdapter);
                                recyclerView.setVisibility(View.VISIBLE);
                            }

                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                VolleyError newerror = new VolleyError(new String(error.networkResponse.data));
                                error = newerror;

                            }
                            AppLog.Log("tod_error: ", error.toString());
                            loader.setVisibility(View.GONE);

                            empty_txt.setVisibility(View.VISIBLE);
                            empty_txt.setText(getResources().getString(R.string.error_event));
                        }
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
               // headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjectRequest);

    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        //Setting Dialog Title
        alertDialog.setTitle(R.string.GPSAlertDialogTitle);
        alertDialog.setCancelable(false);

        //Setting Dialog Message
        alertDialog.setMessage(R.string.GPSAlertDialogMessage);

        //On Pressing Setting button
        alertDialog.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        //On pressing cancel button
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                if(mContext != null) {
                    NearBy.this.finish();
                }
            }
        });

        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        AppLog.Log(MainActivity.class.getSimpleName(), "Connected to Google Play Services!");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            if(lastLocation != null) {

                double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();

                GetNearByEventService(lat, lon);
            } else {
                showSettingsAlert();
            }

        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }

        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        AppLog.Log(MainActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
    }
}
