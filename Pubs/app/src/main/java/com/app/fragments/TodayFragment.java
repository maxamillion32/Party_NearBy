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

import com.android.volley.VolleyError;
import com.app.adaptors.PubsDataAdaptor;
import com.app.interfaces.WebServiceInterface;
import com.app.pojo.EventListItem;
import com.app.pubs.R;
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
import java.util.List;

/**
 * Created by ram on 10/06/16.
 */
public class TodayFragment extends Fragment implements WebServiceInterface {
    int color;
    //RecycleAdaptor adapter;
    private PubsDataAdaptor mAdapter;
    public static List<EventListItem> eventItemList;
    protected Handler handler;

    AuthorizationWebServices auth;

    public TodayFragment() {
    }

    @SuppressLint("ValidFragment")
    public TodayFragment(int color) {
        this.color = color;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs_framents, container, false);

        auth = new AuthorizationWebServices(getContext(), this);

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frag_bg);
        //frameLayout.setBackgroundColor(color);
        eventItemList = new ArrayList<EventListItem>();
        handler = new Handler();


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.frag_scrollableview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

      /*  List<String> list = new ArrayList<String>();
        for (int i = 0; i < VersionModel.data.length; i++) {
            list.add(VersionModel.data[i]);
        }*/
        if (!CheckConnectivity.isConnected(getContext())) {
            Singleton.getInstance(getContext()).ShowToastMessage(getResources().getString(R.string.error_network), getActivity());
        } else {
            auth.EventListService();
        }

        /*for (int i = 0; i < 10; i++) {
            eventItemList.add(new EventListItem(1,"Today Pubs", "Hauz Khas", "Sunday 10:00 PM", "Free" ));

        }


        //adapter = new RecycleAdaptor(list);
        mAdapter = new PubsDataAdaptor(eventItemList, recyclerView);
        recyclerView.setAdapter(mAdapter);*/

        return view;
    }

    @Override
    public void requestCompleted(String obj, int serviceCode) {
        AppLog.Log("Success_Data: ", obj.toString() +", "+serviceCode);
        if(serviceCode == Constant.ServiceCodeAccess.EVENT_LIST) {
            if(obj != null) {
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
                        eventItemList.add(new EventListItem(id,event_name, event_address, event_datetime, entry_cost, event_image ));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void requestEndedWithError(VolleyError error, int errorCode) {

        AppLog.Log("LoginError: ", error.toString() +", "+ errorCode);

    }
}
