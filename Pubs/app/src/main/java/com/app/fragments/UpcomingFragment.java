package com.app.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.app.adaptors.HistoryAdaptor;
import com.app.adaptors.PubsDataAdaptor;
import com.app.pojo.BookingItem;
import com.app.pojo.EventListItem;
import com.app.pojo.HistoryItem;
import com.app.partynearby.MyApplication;
import com.app.partynearby.R;
import com.app.utility.AppLog;
import com.app.utility.CheckConnectivity;
import com.app.utility.Constant;
import com.app.utility.SessionManager;
import com.app.utility.VolleyImageUtlil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ram on 28/05/16.
 */
public class UpcomingFragment extends Fragment implements View.OnClickListener{
    int color;
    //RecycleAdaptor adapter;
    private HistoryAdaptor mAdapter;
    private List<HistoryItem> eventItemList;
    protected Handler handler;
    private RecyclerView recyclerView;
    private TextView empty_txt;
    private ProgressBar loader;
    private LinearLayout rl_network;
    private Button try_again;

    private SessionManager userSession;
    private HashMap<String, String> justPharmaUser;
    public static ImageLoader mImageLoader;

    public UpcomingFragment() {
    }

    @SuppressLint("ValidFragment")
    public UpcomingFragment(int color) {
        this.color = color;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs_framents, container, false);

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frag_bg);
        //frameLayout.setBackgroundColor(color);
        eventItemList = new ArrayList<>();
        handler = new Handler();
        mImageLoader = VolleyImageUtlil.getInstance(getActivity().getApplicationContext())
                .getImageLoader();


        userSession = new SessionManager(getContext());
        justPharmaUser = userSession.getUserDetails();


        recyclerView = (RecyclerView) view.findViewById(R.id.frag_scrollableview);
        empty_txt = (TextView) view.findViewById(R.id.empty_txt);
        loader = (ProgressBar) view.findViewById(R.id.loader);

        try_again = (Button) view.findViewById(R.id.try_again);
        try_again.setOnClickListener(this);
        rl_network = (LinearLayout) view.findViewById(R.id.rl_network);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        if (!CheckConnectivity.isConnected(getContext())) {
            //Singleton.getInstance(getContext()).ShowToastMessage(getResources().getString(R.string.error_network), getActivity());
            loader.setVisibility(View.GONE);
            rl_network.setVisibility(View.VISIBLE);
        } else {
            //auth.EventListService("tod.json"); //tom.json //lat.json

            String uId  = justPharmaUser.get(SessionManager.KEY_ID);
            EventHistoryService(uId);
        }

        return view;
    }


    public void EventHistoryService(String uId) {

        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put(Constant.KeyConstant.USER_ID, uId);

            AppLog.Log("EventHistory_Req: ", reqObj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppLog.Log("URL", Constant.ServiceType.BOOKING_HISTORY);
        RequestQueue queue = MyApplication.getInstance().getRequestQueue();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Constant.ServiceType.BOOKING_HISTORY , reqObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Activity activity = getActivity();
                        if (activity != null && isAdded()) {
                            AppLog.Log("history_response: ", response.toString());

                            empty_txt.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            loader.setVisibility(View.GONE);
                            if (response != null) {
                                eventItemList.clear();

                                String jsonRes = response.toString();
                                try {
                                    JSONObject jsonObject = new JSONObject(jsonRes);
                                    JSONArray jsonArray = jsonObject.getJSONArray("bookingInfo");

                                    if (jsonArray != null) {

                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jObj = jsonArray.getJSONObject(i);
                                            // String id = jObj.getString("id");
                                            String event_image = jObj.getString("event_image");
                                            String event_name = jObj.getString("event_name");
                                            String event_address = jObj.getString("event_address");
                                            String event_time = jObj.getString("entry_time");
                                            String event_datetime = jObj.getString("event_datetime");
                                            String event_contact_no = jObj.getString("event_contact_no");
                                            //String event_description = jObj.getString("event_description");
                                            //String entry_cost = jObj.getString("entry_cost");
                                            String entry_type = jObj.getString("entry_type");
                                            String booking_id = jObj.getString("booking_id");
                                            //String date_added = jObj.getString("date_added");

                                            if (event_image != null && !event_image.isEmpty()) {
                                                event_image = event_image.replaceAll(" ", "%20");
                                            }
                                            eventItemList.add(new HistoryItem(event_name, event_address, event_datetime,
                                                    event_time, booking_id, "",
                                                    entry_type, event_image, ""));
                                        }
                                    } else {

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (eventItemList.isEmpty()) {
                                    loader.setVisibility(View.GONE);
                                    empty_txt.setVisibility(View.VISIBLE);
                                    empty_txt.setText(getResources().getString(R.string.error_event));
                                } else {
                                    mAdapter = new HistoryAdaptor(eventItemList, recyclerView);
                                    recyclerView.setAdapter(mAdapter);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }

                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Activity activity = getActivity();
                        if (activity != null && isAdded()) {
                            if (error != null) {
                                if (error.networkResponse != null && error.networkResponse.data != null) {
                                    VolleyError newerror = new VolleyError(new String(error.networkResponse.data));
                                    error = newerror;

                                }
                                AppLog.Log("History_error: ", error.toString());
                                loader.setVisibility(View.GONE);

                                empty_txt.setVisibility(View.VISIBLE);
                                empty_txt.setText(getResources().getString(R.string.error_event));
                            }
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.try_again:
                if (!CheckConnectivity.isConnected(getContext())) {
                    loader.setVisibility(View.GONE);
                    rl_network.setVisibility(View.VISIBLE);
                } else {
                    String uId  = justPharmaUser.get(SessionManager.KEY_ID);
                    EventHistoryService(uId);
                }
                break;
            default:
                break;
        }
    }

}
