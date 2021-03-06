package com.app.adaptors;

import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app.fragments.TodayFragment;
import com.app.interfaces.OnLoadMoreListener;
import com.app.pojo.EventListItem;
import com.app.partynearby.Booking;
import com.app.partynearby.EventDetails;
import com.app.partynearby.Login;
import com.app.partynearby.MainActivity;
import com.app.partynearby.R;
import com.app.utility.AppLog;
import com.app.utility.Constant;
import com.app.utility.SessionManager;
import com.app.utility.Singleton;
import com.app.utility.VolleyImageUtlil;

import java.util.List;

public class PubsDataAdaptor extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Context mContext;

    private List<EventListItem> medicineList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private SessionManager sessionManager;


    public ImageLoader mImageLoader;


    public PubsDataAdaptor(Context _mContext, List<EventListItem> medicines, RecyclerView recyclerView) {
        medicineList = medicines;
        mContext = _mContext;
        mImageLoader = VolleyImageUtlil.getInstance(mContext)
                .getImageLoader();
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
        final EventListItem singleNewItem=  medicineList.get(position);

        if (holder instanceof PubsDataViewHolder) {

            String ev_name = String.valueOf(singleNewItem.getEventName());
            String ev_discount = String.valueOf(singleNewItem.getDiscount());
            String ev_price = String.valueOf(singleNewItem.getPrice());
            String ev_img = String.valueOf(singleNewItem.getThumbnail());
            String ev_address = String.valueOf(singleNewItem.getAddress());
            String ev_time = String.valueOf(singleNewItem.getTime());
            String ev_dateTime = String.valueOf(singleNewItem.getDateTime());
            final String cost = String.valueOf(singleNewItem.getPrice());
            final String entryType = String.valueOf(singleNewItem.getEntryType());

            if(ev_name != null && !ev_name.isEmpty()) {
                ev_name = Singleton.capitalize(ev_name);
                ((PubsDataViewHolder) holder).title.setText(ev_name);
            }

            if(ev_discount != null && !ev_discount.isEmpty() && !ev_discount.equalsIgnoreCase("0")) {
                ((PubsDataViewHolder) holder).discount.setVisibility(View.VISIBLE);
                ((PubsDataViewHolder) holder).discount.setText(ev_discount +" "+ "off");
                ((PubsDataViewHolder) holder).discount.setPaintFlags(((PubsDataViewHolder) holder).discount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            }

            if(ev_price != null && !ev_price.isEmpty()) {
                ((PubsDataViewHolder) holder).price.setText(ev_price);
            }

            if(ev_address != null && !ev_address.isEmpty()) {
                ((PubsDataViewHolder) holder).address.setText(Singleton.capitalize(ev_address));
            }

            if(ev_img != null && !ev_img.isEmpty()) {
                // Instantiate the RequestQueue.
                //Image URL - This can point to any image file supported by Android
                final String url = Constant.ServiceType.IMAGE_BASE_URL+ev_img;
                mImageLoader.get(url, ImageLoader.getImageListener(((PubsDataViewHolder) holder).thumbnail,
                        R.drawable.ib_loading, android.R.drawable
                                .ic_dialog_alert));
                ((PubsDataViewHolder) holder).thumbnail.setImageUrl(url, mImageLoader);
            }

            if(ev_time != null && ev_dateTime != null && !ev_time.isEmpty()) {
                String dateTime = ev_time +", "+ev_dateTime;
                ((PubsDataViewHolder) holder).time.setText(dateTime);
            }


            ((PubsDataViewHolder) holder).thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppLog.Log("id: ", String.valueOf(singleNewItem.getEventId()));
                    Intent i = new Intent(v.getContext(), EventDetails.class);
                    v.getContext().startActivity(i);

                    Singleton.getInstance(v.getContext()).ev_id = String.valueOf(singleNewItem.getEventId());
                    Singleton.getInstance(v.getContext()).ev_thumbnail = String.valueOf(singleNewItem.getThumbnail());
                    Singleton.getInstance(v.getContext()).ev_cost = cost;
                    Singleton.getInstance(v.getContext()).ev_entrytype = entryType;
                }
            });

            ((PubsDataViewHolder) holder).book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    sessionManager = new SessionManager(v.getContext());
                    if(sessionManager.isLoggedIn()) {
                        Singleton.getInstance(v.getContext()).ev_id = String.valueOf(singleNewItem.getEventId());
                        Singleton.getInstance(v.getContext()).ev_thumbnail = String.valueOf(singleNewItem.getThumbnail());
                        Singleton.getInstance(v.getContext()).ev_cost = cost;
                        Singleton.getInstance(v.getContext()).ev_entrytype = entryType;
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
        TextView address, time;
        Button book;
        NetworkImageView thumbnail;
        public EventListItem newItems;

        public PubsDataViewHolder(View v) {
            super(v);
            cardItemLayout = (CardView) v.findViewById(R.id.cardlist_item);
            title = (TextView) v.findViewById(R.id.title);
            address = (TextView) v.findViewById(R.id.address);
            price = (TextView) v.findViewById(R.id.price);
            discount = (TextView) v.findViewById(R.id.discount);
            book = (Button) v.findViewById(R.id.book);
            thumbnail = (NetworkImageView) v.findViewById(R.id.thumbnail);
            time = (TextView) v.findViewById(R.id.time);

            discount.setVisibility(View.GONE);

          /*  title.setVisibility(View.GONE);
            address.setVisibility(View.GONE);
            price.setVisibility(View.GONE);

            time.setVisibility(View.GONE);*/

            v.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    /*Intent i = new Intent(v.getContext(), EventDetails.class);
                    v.getContext().startActivity(i);

                    Singleton.getInstance(v.getContext()).ev_id = String.valueOf(newItems.getEventId());
                    Singleton.getInstance(v.getContext()).ev_thumbnail = String.valueOf(newItems.getThumbnail());*/

                   /* Toast.makeText(v.getContext(),
                            "OnClick :" + newItems.getEventId() + " \n " + newItems.getEventId(),
                            Toast.LENGTH_SHORT).show();*/

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
