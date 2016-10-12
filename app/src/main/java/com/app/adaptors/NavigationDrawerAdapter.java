package com.app.adaptors;

/**
 * Created by Ravi on 29/07/15.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.pojo.NavDrawerItem;
import com.app.partynearby.BookingHistory;
import com.app.partynearby.MainActivity;
import com.app.partynearby.R;
import com.app.partynearby.SettingsActivity;
import com.app.utility.AppLog;

import java.util.Collections;
import java.util.List;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(String.valueOf(current.getTitle()));

        AppLog.Log("img", current.getImageIcon() +"");

        holder.img.setImageResource(data.get(position).getImageIcon());

        //holder.img.setImageResource(current.getImageIcon());
        //int id = context.getResources().getIdentifier("com.app.partynearby:drawable/" + current.getImageIcon(), null, null);
        //holder.img.setImageResource(id);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);

            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
