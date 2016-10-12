package com.app.partynearby;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.adaptors.PubsDataAdaptor;
import com.app.pojo.EventListItem;
import com.app.utility.AppLog;
import com.app.utility.CheckConnectivity;
import com.app.utility.Constant;
import com.app.utility.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ram on 15/06/16.
 */
public class SearchResultsActivity extends AppCompatActivity {

    private TextView txtQuery;
    private Toolbar toolbar;
    private Context mContext;
    private ProgressBar loader;
    private RecyclerView frag_scrollableview;
    private PubsDataAdaptor mAdapter;
    public List<EventListItem> eventItemList  = new ArrayList<EventListItem>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        toolbar = (Toolbar) findViewById(R.id.toolbar_elevated);
        mContext = SearchResultsActivity.this;
        loader = (ProgressBar) findViewById(R.id.loader);
        frag_scrollableview = (RecyclerView) findViewById(R.id.frag_scrollableview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        frag_scrollableview.setLayoutManager(linearLayoutManager);
        frag_scrollableview.setHasFixedSize(true);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.lb_search_result));
        }

        txtQuery = (TextView) findViewById(R.id.txtQuery);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            /**
             * Use this query to display search results like
             * 1. Getting the data from SQLite and showing in listview
             * 2. Making webrequest and displaying the data
             * For now we just display the query only
             */
            if (!CheckConnectivity.isConnected(mContext)) {
                Singleton.getInstance(mContext).hideSoftKeyboard(this);
                Singleton.getInstance(mContext).ShowToastMessage(getResources().getString(R.string.error_network_title), mContext);
                return;
            }
            //txtQuery.setText("Record not found: " + query);
            GetSearchEventService(query);

        }

    }


    private void GetSearchEventService(String query) {
        loader.setVisibility(View.VISIBLE);
        if(query == null) {
            Singleton.getInstance(mContext).ShowToastMessage("Error, please try again.", mContext);
            return;
        }

        //dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        //dialog.show();
        AppLog.Log("URL", Constant.ServiceType.SEARCH_EVENT + query +".json");
        RequestQueue queue = MyApplication.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constant.ServiceType.SEARCH_EVENT + query +".json", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                            AppLog.Log("Search_response: ", response.toString());
                            loader.setVisibility(View.GONE);
                            if (response != null) {
                                eventItemList.clear();

                                String jsonRes = response.toString();
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonRes);
                                    JSONArray jsonArray = jsonObject.getJSONArray("eventData");

                                    if (jsonArray != null) {

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
                                            if (event_image != null && !event_image.isEmpty()) {
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

                                if (eventItemList.isEmpty()) {
                                    txtQuery.setVisibility(View.VISIBLE);
                                    txtQuery.setText(getResources().getString(R.string.error_event));
                                } else {
                                    mAdapter = new PubsDataAdaptor(mContext, eventItemList, frag_scrollableview);
                                    frag_scrollableview.setAdapter(mAdapter);
                                    frag_scrollableview.setVisibility(View.VISIBLE);
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
                                AppLog.Log("Search_error: ", error.toString());
                                loader.setVisibility(View.GONE);
                                txtQuery.setVisibility(View.VISIBLE);
                                txtQuery.setText(getResources().getString(R.string.error_event));
                            }
                        }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
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
