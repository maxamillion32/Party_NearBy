package com.app.adaptors;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.partynearby.Booking;
import com.app.partynearby.BookingHistory;
import com.app.partynearby.EventDetails;
import com.app.partynearby.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram on 21/05/16.
 */
public class RecycleAdaptor extends RecyclerView.Adapter<RecycleAdaptor.VersionViewHolder> {
    List<String> versionModels;
    Context context;
    OnItemClickListener clickListener;


    public RecycleAdaptor(Context context) {
        this.context = context;
    }


    public RecycleAdaptor(List<String> versionModels) {
        this.versionModels = versionModels;

    }

    @Override
    public VersionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerlist_item, viewGroup, false);
        VersionViewHolder viewHolder = new VersionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VersionViewHolder versionViewHolder, int i) {

            versionViewHolder.title.setText(versionModels.get(i));
    }

    @Override
    public int getItemCount() {
            return versionModels == null ? 0 : versionModels.size();
    }


    class VersionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardItemLayout;
        TextView title;
        TextView subTitle;
        Button book;

        public VersionViewHolder(View itemView) {
            super(itemView);

            cardItemLayout = (CardView) itemView.findViewById(R.id.cardlist_item);
            title = (TextView) itemView.findViewById(R.id.title);
            subTitle = (TextView) itemView.findViewById(R.id.address);
            book = (Button) itemView.findViewById(R.id.book);

                itemView.setOnClickListener(this);
                subTitle.setVisibility(View.VISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    /*Toast.makeText(v.getContext(),
                            "OnClick :" + newItems.getpId() + " \n " + newItems.getpName(),
                            Toast.LENGTH_SHORT).show();*/

                    //Intent orderDesc = new Intent(v.getContext(), OrderDetails.class);
                    v.getContext().startActivity(new Intent(v.getContext(), EventDetails.class));


                }
            });

            book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    v.getContext().startActivity(new Intent(v.getContext(), Booking.class));

                }
            });

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getPosition());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}

