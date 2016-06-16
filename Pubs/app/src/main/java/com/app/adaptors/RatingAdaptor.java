package com.app.adaptors;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.interfaces.OnLoadMoreListener;
import com.app.pojo.ReviewItem;
import com.app.pubs.R;

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

        ((PubsDataViewHolder) holder).name.setText(String.valueOf(singleNewItem.getrName()));


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
    TextView name;
    ImageView thumbnail;
    public ReviewItem newItems;

    public PubsDataViewHolder(View v) {
        super(v);
        cardItemLayout = (CardView) v.findViewById(R.id.cardlist_item);
        name = (TextView) v.findViewById(R.id.name);
        thumbnail = (ImageView) v.findViewById(R.id.thumbnail);


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

