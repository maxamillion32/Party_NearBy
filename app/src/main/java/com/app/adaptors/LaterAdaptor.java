package com.app.adaptors;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.interfaces.OnLoadMoreListener;
import com.app.pojo.EventListItem;
import com.app.partynearby.Booking;
import com.app.partynearby.EventDetails;
import com.app.partynearby.Login;
import com.app.partynearby.R;
import com.app.utility.SessionManager;
import com.app.utility.Singleton;

import java.util.List;

/**
 * Created by thinksysuser on 17-06-2016.
 */
public class LaterAdaptor   extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<EventListItem> medicineList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private static SessionManager sessionManager;

    public LaterAdaptor(List<EventListItem> medicines, RecyclerView recyclerView) {
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
                    R.layout.recyclerlist_item, parent, false);

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

            EventListItem singleNewItem=  medicineList.get(position);

            String ev_name = String.valueOf(singleNewItem.getEventName());
            String ev_discount = String.valueOf(singleNewItem.getDiscount());
            String ev_price = String.valueOf(singleNewItem.getPrice());
            String ev_img = String.valueOf(singleNewItem.getThumbnail());
            String ev_address = String.valueOf(singleNewItem.getAddress());

            if(ev_name != null) {
                ev_name = Singleton.capitalize(ev_name);
                ((PubsDataViewHolder) holder).title.setText(ev_name);
            }

            if(ev_discount != null) {
                ((PubsDataViewHolder) holder).discount.setText(ev_discount);
                ((PubsDataViewHolder) holder).discount.setPaintFlags(((PubsDataViewHolder) holder).discount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            }

            if(ev_price != null) {
                ((PubsDataViewHolder) holder).price.setText(ev_price);
            }

            if(ev_address != null) {
                ((PubsDataViewHolder) holder).address.setText(ev_address);
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
        TextView title, price, discount;
        TextView address;
        Button book;
        ImageView thumbnail;
        public EventListItem newItems;

        public PubsDataViewHolder(View v) {
            super(v);
            cardItemLayout = (CardView) v.findViewById(R.id.cardlist_item);
            title = (TextView) v.findViewById(R.id.title);
            address = (TextView) v.findViewById(R.id.address);
            price = (TextView) v.findViewById(R.id.price);
            discount = (TextView) v.findViewById(R.id.discount);
            book = (Button) v.findViewById(R.id.book);
            thumbnail = (ImageView) v.findViewById(R.id.thumbnail);

            address.setVisibility(View.VISIBLE);

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(new Intent(v.getContext(), EventDetails.class));

                }
            });

            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    sessionManager = new SessionManager(v.getContext());
                    if(sessionManager.isLoggedIn()) {
                        v.getContext().startActivity(new Intent(v.getContext(), Booking.class));
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                v.getContext(),
                                R.style.AlertDialogCustom_Destructive)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Delete Action
                                        v.getContext().startActivity(new Intent(v.getContext(), Login.class));
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // Cancel Action
                                    }
                                })
                                .setTitle("Alert !")
                                .create();
                        alertDialog.setMessage("Login required");
                        alertDialog.setCancelable(false);
                        alertDialog.show();
                    }

                }
            });
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
