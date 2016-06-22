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
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.app.adaptors.PubsDataAdaptor;
import com.app.adaptors.TomorrowDaptor;
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
public class TomorrowFragment extends Fragment implements WebServiceInterface {
    int color;

    private PubsDataAdaptor mAdapter;
    public List<EventListItem> eventItemList;
    protected Handler handler;

    public static AuthorizationWebServices auth;
    private RecyclerView recyclerView;
    private ProgressBar loader;
    private TextView empty_txt;

    public static TomorrowFragment newInstance(int page) {
        return new TomorrowFragment();
    }


    public TomorrowFragment() {
    }

    @SuppressLint("ValidFragment")
    public TomorrowFragment(int color) {
        this.color = color;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabs_framents, container, false);

        auth = new AuthorizationWebServices(getContext(), this);

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frag_bg);
        loader = (ProgressBar) view.findViewById(R.id.loader);
        empty_txt = (TextView) view.findViewById(R.id.empty_txt);
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
           auth.EventListService("tom.json"); //tom.json //lat.json
        }

        return view;
    }

    @Override
    public void requestCompleted(String obj, int serviceCode) {
        AppLog.Log("Success_Data_tomorrow: ", obj.toString() +", "+serviceCode);
        empty_txt.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        loader.setVisibility(View.GONE);
        if(serviceCode == Constant.ServiceCodeAccess.EVENT_LIST) {
            if(obj != null) {
                eventItemList.clear();
                String jsonRes = obj.toString();
                try {
                    JSONObject jsonObject = new JSONObject(jsonRes);
                    JSONArray jsonArray = jsonObject.getJSONArray("event_lists");

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


    }

    @Override
    public void requestEndedWithError(VolleyError error, int errorCode) {
        loader.setVisibility(View.GONE);
        AppLog.Log("LoginError: ", error.toString() +", "+ errorCode);

        empty_txt.setVisibility(View.VISIBLE);
        empty_txt.setText(getResources().getString(R.string.error_event));

    }
}
