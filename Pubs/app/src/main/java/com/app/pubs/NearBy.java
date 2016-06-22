package com.app.pubs;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.app.adaptors.NearByAdaptor;
import com.app.adaptors.RatingAdaptor;
import com.app.pojo.EventListItem;
import com.app.pojo.ReviewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thinksysuser on 15-06-2016.
 */
public class NearBy extends AppCompatActivity {

    private Toolbar toolbar;
    private Context mContext;
    private RecyclerView recyclerView;
    private NearByAdaptor mAdapter;
    private List<EventListItem> eventItemList;
    protected Handler handler;

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
        eventItemList = new ArrayList<EventListItem>();
        handler = new Handler();


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);



            //eventItemList.add((new EventListItem("1","Today Pubs", "Noida", "Saturday 10:00 PM", "Free" , "")));


        //adapter = new RecycleAdaptor(list);
        mAdapter = new NearByAdaptor(eventItemList, recyclerView);
        recyclerView.setAdapter(mAdapter);
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
