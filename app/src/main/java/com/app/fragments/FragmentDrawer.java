package com.app.fragments;

/**
 * Created by Ravi on 29/07/15.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.app.adaptors.NavigationDrawerAdapter;
import com.app.pojo.NavDrawerItem;
import com.app.partynearby.R;
import com.app.utility.AppLog;
import com.app.utility.SessionManager;
import com.app.utility.Singleton;
import com.app.utility.VolleyImageUtlil;
import com.facebook.login.LoginManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FragmentDrawer extends Fragment {

    private static String TAG = FragmentDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private NetworkImageView user_pic;
    private TextView user_name;
    private ImageLoader mImageLoader;
    private SessionManager userSession;
    private HashMap<String, String> justPharmaUser;

    private static TypedArray navMenuIcons = null;
    private FragmentDrawerListener drawerListener;
    private TextView logout;
    private RelativeLayout logout_lay;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();


        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            //navItem.setImageIcon(titlesIcon[i]);
            navItem.setImageIcon(navMenuIcons.getResourceId(i, -1));
            data.add(navItem);
        }
        return data;
    }


    @Override
    public void onStart() {
        super.onStart();
        // Instantiate the RequestQueue.

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
        navMenuIcons = getActivity().getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        userSession = new SessionManager(getActivity().getApplicationContext());
        justPharmaUser = userSession.getUserDetails();

        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        user_pic = (NetworkImageView) layout.findViewById(R.id.user_pic);
        user_name = (TextView) layout.findViewById(R.id.user_name);
        logout = (TextView) layout.findViewById(R.id.logout);
        logout_lay = (RelativeLayout) layout.findViewById(R.id.logout_lay);
        logout_lay.setVisibility(View.GONE);

        String fname  = justPharmaUser.get(SessionManager.KEY_FNAME);
        String lname  = justPharmaUser.get(SessionManager.KEY_LNAME);
        Singleton.getInstance(getActivity().getApplicationContext()).userPhoto = justPharmaUser.get(SessionManager.KEY_PROFILE_PIC);
        if(fname != null && lname != null) {
            Singleton.getInstance(getActivity().getApplicationContext()).userName = fname +" "+ lname;

        } else {
            Singleton.getInstance(getActivity().getApplicationContext()).userName = fname;
        }

        if(Singleton.getInstance(getActivity().getApplicationContext()).userName != null
                && !Singleton.getInstance(getActivity().getApplicationContext()).userName.isEmpty()) {
            user_name.setText(Singleton.getInstance(getActivity().getApplicationContext()).userName);
            logout_lay.setVisibility(View.VISIBLE);
        }

        mImageLoader = VolleyImageUtlil.getInstance(getActivity().getApplicationContext())
                .getImageLoader();
        //Image URL - This can point to any image file supported by Android

        final String url = Singleton.getInstance(getActivity().getApplicationContext()).userPhoto;
        AppLog.Log("url: ", url);
        if(url != null && !url.isEmpty() && !url.equalsIgnoreCase("null")) {
            mImageLoader.get(url, ImageLoader.getImageListener(user_pic,
                    R.drawable.default_user, R.drawable.default_user));
            user_pic.setImageUrl(url, mImageLoader);
            logout_lay.setVisibility(View.VISIBLE);


            logout_lay.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    AlertDialog alertDialog = new AlertDialog.Builder(
                            getContext(),
                            R.style.AlertDialogCustom_Destructive)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Delete Action
                                    userSession.logoutUser();
                                    if(LoginManager.getInstance() != null) {
                                        LoginManager.getInstance().logOut();
                                    }

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
                    alertDialog.setMessage("Are you sure do you want to logout ?");
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                    return false;
                }


            });


        }


        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return layout;
    }

    public void logoutApp(View view) {

    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);
    }
}
