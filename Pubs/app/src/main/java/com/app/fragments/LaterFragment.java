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

import com.app.adaptors.PubsDataAdaptor;
import com.app.pojo.EventListItem;
import com.app.pubs.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram on 10/06/16.
 */
public class LaterFragment extends Fragment {
    int color;
    //RecycleAdaptor adapter;
    private PubsDataAdaptor mAdapter;
    private List<EventListItem> eventItemList;
    protected Handler handler;

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
        for (int i = 0; i < 10; i++) {
            eventItemList.add(new EventListItem(1,"Later Pubs", "Hauz Khas", "Sunday 10:00 PM", "Free" ));

        }


        //adapter = new RecycleAdaptor(list);
        mAdapter = new PubsDataAdaptor(eventItemList, recyclerView);
        recyclerView.setAdapter(mAdapter);

        return view;
    }
}

