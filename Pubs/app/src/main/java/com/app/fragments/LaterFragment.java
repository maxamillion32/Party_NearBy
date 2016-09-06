package com.app.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.app.adaptors.LaterAdaptor;
import com.app.adaptors.PubsDataAdaptor;
import com.app.interfaces.WebServiceInterface;
import com.app.pojo.EventListItem;
import com.app.partynearby.MyApplication;
import com.app.partynearby.R;
import com.app.utility.AppLog;
import com.app.utility.CheckConnectivity;
import com.app.utility.Constant;
import com.app.utility.Singleton;
import com.app.utility.VolleyImageUtlil;
import com.app.webservices.AuthorizationWebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ram on 10/06/16.
 */
public class LaterFragment extends Fragment implements View.OnClickListener {
    int color;

    private PubsDataAdaptor mAdapter;
    public List<EventListItem> eventItemList;
    protected Handler handler;

   // public static AuthorizationWebServices auth;
    private RecyclerView recyclerView;
    private ProgressBar loader;
    private LinearLayout rl_network;
    private Button try_again;
    private TextView empty_txt;

    public static TomorrowFragment newInstance(int page) {
        return new TomorrowFragment();
    }


    public LaterFragment() {
    }

    @SuppressLint("ValidFragment")
    public LaterFragment(int color) {
        this.color = color;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs_framents, container, false);

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frag_bg);
        loader = (ProgressBar) view.findViewById(R.id.loader);
        rl_network = (LinearLayout) view.findViewById(R.id.rl_network);
        try_again = (Button) view.findViewById(R.id.try_again);
        empty_txt = (TextView) view.findViewById(R.id.empty_txt);
        try_again.setOnClickListener(this);
        //frameLayout.setBackgroundColor(color);
        eventItemList = new ArrayList<EventListItem>();
        handler = new Handler();

        AppLog.Log("Called", "2");
        recyclerView = (RecyclerView) view.findViewById(R.id.frag_scrollableview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        if (!CheckConnectivity.isConnected(getContext())) {
            //Singleton.getInstance(getContext()).ShowToastMessage(getResources().getString(R.string.error_network), getActivity());
            loader.setVisibility(View.GONE);
            rl_network.setVisibility(View.VISIBLE);
        }  else {
           // auth.EventListService("lat.json"); //tom.json //lat.json
            GetLaterEventService();
        }

        return view;
    }

   /* @Override
    public void requestCompleted(String obj, int serviceCode) {
        AppLog.Log("Success_Data_tomorrow: ", obj.toString() +", "+serviceCode);
        rl_network.setVisibility(View.GONE);
        if(serviceCode == Constant.ServiceCodeAccess.EVENT_LIST) {
            if(obj != null) {
               // eventItemList.clear();
                loader.setVisibility(View.GONE);
                String jsonRes = obj.toString();
                try {
                    JSONObject jsonObject = new JSONObject(jsonRes);
                    JSONArray jsonArray = jsonObject.getJSONArray("event_lists");

                    for(int i = 0; i < jsonArray.length(); i++) {
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
                        eventItemList.add(new EventListItem(date_added, discount, entry_type, event_description,
                                event_contact_no, event_time, id,event_name, event_address,
                                event_datetime, entry_cost, event_image ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //adapter = new RecycleAdaptor(list);
                mAdapter = new LaterAdaptor(eventItemList, recyclerView);
                recyclerView.setAdapter(mAdapter);
            }
        }


    }

    @Override
    public void requestEndedWithError(VolleyError error, int errorCode) {
        loader.setVisibility(View.GONE);
        rl_network.setVisibility(View.GONE);
        AppLog.Log("LoginError: ", error.toString() +", "+ errorCode);

    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.try_again:
                if (!CheckConnectivity.isConnected(getContext())) {
                    loader.setVisibility(View.GONE);
                    rl_network.setVisibility(View.VISIBLE);
                } else {
                    //auth.EventListService("tod.json"); //tom.json //lat.json
                }
                break;
            default:
                break;
        }

    }


    public void GetLaterEventService() {

        //dialog.setMessage(ctx.getResources().getString(R.string.progress_loading));
        //dialog.show();
        AppLog.Log("URL", Constant.ServiceType.EVENT_LIST +"lat.json");
        RequestQueue queue = MyApplication.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                Constant.ServiceType.EVENT_LIST +"lat.json", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        AppLog.Log("lat_response: ", response.toString());

                        rl_network.setVisibility(View.GONE);
                            if(response != null) {
                                // eventItemList.clear();
                                loader.setVisibility(View.GONE);
                                String jsonRes = response.toString();
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonRes);
                                    JSONArray jsonArray = jsonObject.getJSONArray("event_lists");

                                    for(int i = 0; i < jsonArray.length(); i++) {
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
                                                event_contact_no, event_time, id,event_name, event_address,
                                                event_datetime, entry_cost, event_image ));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //adapter = new RecycleAdaptor(list);
                                mAdapter = new PubsDataAdaptor(eventItemList, recyclerView);
                                recyclerView.setAdapter(mAdapter);
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
                            AppLog.Log("lat_error: ", error.toString());
                            loader.setVisibility(View.GONE);
                            rl_network.setVisibility(View.GONE);

                            loader.setVisibility(View.GONE);
                            rl_network.setVisibility(View.GONE);

                            empty_txt.setVisibility(View.VISIBLE);
                            empty_txt.setText(getResources().getString(R.string.error_event));
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
