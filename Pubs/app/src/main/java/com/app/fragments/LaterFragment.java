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
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.app.adaptors.LaterAdaptor;
import com.app.adaptors.PubsDataAdaptor;
import com.app.interfaces.WebServiceInterface;
import com.app.pojo.EventListItem;
import com.app.pubs.R;
import com.app.utility.AppLog;
import com.app.utility.CheckConnectivity;
import com.app.utility.Constant;
import com.app.utility.Singleton;
import com.app.webservices.AuthorizationWebServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram on 10/06/16.
 */
public class LaterFragment extends Fragment implements WebServiceInterface {
    int color;

    private PubsDataAdaptor mAdapter;
    public List<EventListItem> eventItemList;
    protected Handler handler;

    public static AuthorizationWebServices auth;
    private RecyclerView recyclerView;
    private ProgressBar loader;

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

        auth = new AuthorizationWebServices(getContext(), this);

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frag_bg);
        loader = (ProgressBar) view.findViewById(R.id.loader);
        //frameLayout.setBackgroundColor(color);
        eventItemList = new ArrayList<EventListItem>();
        handler = new Handler();

        AppLog.Log("Called", "2");
        recyclerView = (RecyclerView) view.findViewById(R.id.frag_scrollableview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        if (!CheckConnectivity.isConnected(getContext())) {
            Singleton.getInstance(getContext()).ShowToastMessage(getResources().getString(R.string.error_network), getActivity());
        } else {
            auth.EventListService("lat.json"); //tom.json //lat.json
        }

        return view;
    }

    @Override
    public void requestCompleted(String obj, int serviceCode) {
        AppLog.Log("Success_Data_tomorrow: ", obj.toString() +", "+serviceCode);
        if(serviceCode == Constant.ServiceCodeAccess.EVENT_LIST) {
            if(obj != null) {
                eventItemList.clear();
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
                mAdapter = new PubsDataAdaptor(eventItemList, recyclerView);
                recyclerView.setAdapter(mAdapter);
            }
        }


    }

    @Override
    public void requestEndedWithError(VolleyError error, int errorCode) {
        loader.setVisibility(View.GONE);
        AppLog.Log("LoginError: ", error.toString() +", "+ errorCode);

    }
}
