package com.app.adaptors;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.app.interfaces.OnLoadMoreListener;
import com.app.pojo.ReviewItem;
import com.app.partynearby.R;
import com.app.partynearby.Rating;

import java.util.List;

/**
 * Created by ram on 13/06/16.
 */
public class RatingAdaptor  extends RecyclerView.Adapter {
private final int VIEW_ITEM = 1;
private final int VIEW_PROG = 0;

private List<ReviewItem> medicineList;

// The minimum amount of items to have below your current scroll position
// before loading more.
private int visibleThreshold = 5;
private int lastVisibleItem, totalItemCount;
private boolean loading;
private OnLoadMoreListener onLoadMoreListener;

public RatingAdaptor(List<ReviewItem> medicines, RecyclerView recyclerView) {
        medicineList = medicines;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
        .getLayoutManager();


        recyclerView
        .addOnScrollListener(new RecyclerView.OnScrollListener() {
@Override
public void onScrolled(RecyclerView recyclerView,
        int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        totalItemCount = linearLayoutManager.getItemCount();
        lastVisibleItem = linearLayoutManager
        .findLastVisibleItemPosition();
        if (!loading
        && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
        // End has been reached
        // Do something
        if (onLoadMoreListener != null) {
        onLoadMoreListener.onLoadMore();
        }
        loading = true;
        }
        }
        });
        }
        }


@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return null;
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
        R.layout.review_item, parent, false);

        vh = new PubsDataViewHolder(v);
        } else {
        View v = LayoutInflater.from(parent.getContext()).inflate(
        R.layout.progressbar_item, parent, false);

        vh = new ProgressViewHolder(v);
        }
        return vh;
        }

@Override
public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof PubsDataViewHolder) {

            ReviewItem singleNewItem=  medicineList.get(position);

            String thumbnail = String.valueOf(singleNewItem.geteThumbnail());
            String name = String.valueOf(singleNewItem.getrName());
            String desc = String.valueOf(singleNewItem.getrDesc());
            String ratCount = String.valueOf(singleNewItem.getrStarCount());
            String datetime = String.valueOf(singleNewItem.getrDatetime());

            if(thumbnail != null && !thumbnail.isEmpty()) {

            }

            if(name != null && !name.isEmpty()) {
                ((PubsDataViewHolder) holder).name.setText(name);
            }

            if(desc != null && !desc.isEmpty()) {
                ((PubsDataViewHolder) holder).desc.setText(desc);
            }

            if(ratCount != null && !ratCount.isEmpty()) {

            }

            if(datetime != null && !datetime.isEmpty()) {

                ((PubsDataViewHolder) holder).datetime.setText(datetime);

            }



        }
        else {
        ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

        }

public void setLoaded() {
        loading = false;
        }

@Override
public int getItemCount() {
        return medicineList.size();

        }


@Override
public int getItemViewType(int position) {

        return medicineList.get(position) != null ? VIEW_ITEM : VIEW_PROG;

        }

public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        }

//
public static class PubsDataViewHolder extends RecyclerView.ViewHolder {
    CardView cardItemLayout;
    TextView name, desc, rating_count, datetime;
    ImageView thumbnail;
    RatingBar rating_bar;
    public ReviewItem newItems;

    public PubsDataViewHolder(View v) {
        super(v);
        cardItemLayout = (CardView) v.findViewById(R.id.cardlist_item);
        name = (TextView) v.findViewById(R.id.name);
        thumbnail = (ImageView) v.findViewById(R.id.thumbnail);

        rating_bar = (RatingBar) v.findViewById(R.id.rating_bar);
        desc = (TextView) v.findViewById(R.id.desc);
        rating_count = (TextView) v.findViewById(R.id.rating_count);
        datetime = (TextView) v.findViewById(R.id.datetime);


    }
}

public static class ProgressViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public ProgressViewHolder(View v) {
        super(v);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
    }
}
}

