package com.app.partynearby;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.toolbox.ImageLoader;
import com.app.adaptors.ViewPagerAdapter;
import com.app.fragments.FragmentDrawer;
import com.app.utility.AppLog;
import com.app.utility.SessionManager;
import com.app.utility.Singleton;
import com.app.utility.VolleyImageUtlil;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, TabLayout.OnTabSelectedListener {
    private Toolbar toolbar;
    private Context mContext;
    private FragmentDrawer drawerFragment;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout htab_appbar;
    private SessionManager sessionManager;


    private ViewPager viewPager;
    private ViewPagerAdapter TabAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        sessionManager = new SessionManager(getApplicationContext());


        toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setTitle("Pubs Nearby");
            //getSupportActionBar().setIcon(R.drawable.app_icon);
            //toolbar.setNavigationIcon(R.drawable.logo);
            //mToolBar.setNavigationIcon(R.drawable.ic_back_shadow);

            final ActionBar ab = getSupportActionBar();
            if(ab != null) {
                //ab.setHomeAsUpIndicator(R.drawable.app_icon); // set a custom icon for
                ab.setIcon(R.drawable.app_icon);
                // the default home button
                ab.setDisplayShowHomeEnabled(true); // show or hide the default home
                // button
                ab.setDisplayHomeAsUpEnabled(true);
                ab.setDisplayShowCustomEnabled(false); // enable overriding the default
                // toolbar layout
                ab.setDisplayShowTitleEnabled(false);
            }
        }


        inItWidget();
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
        htab_appbar = (AppBarLayout) findViewById(R.id.htab_appbar);

        // display the first navigation drawer view on app launch
        displayView(0);
    }


    void inItWidget() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.header);

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressWarnings("ResourceType")
            @Override
            public void onGenerated(Palette palette) {

                int vibrantColor = palette.getVibrantColor(R.color.primary_500);
                int vibrantDarkColor = palette.getDarkVibrantColor(R.color.primary_700);
                collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
            }
        });



        // TODO TABS
        viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.htab_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("TODAY"));
        tabLayout.addTab(tabLayout.newTab().setText("TOMORROW"));
        tabLayout.addTab(tabLayout.newTab().setText("LATER"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        TabAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());


        viewPager.setAdapter(TabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        Intent intent = null;
        AppLog.Log("position: ", position + "");
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                //fragment = new HomeFragment();
                //title = getString(R.string.title_home);
                break;
            case 1:
                //fragment = new FriendsFragment();
                //title = getString(R.string.title_friends);
               // Singleton.getInstance(mContext).ShowToastMessage("Available soon", mContext);
                intent = new Intent(getApplicationContext(), NearBy.class);
                break;
            case 2:
                //fragment = new MessagesFragment();
                //title = getString(R.string.title_messages);
                if(sessionManager.isLoggedIn()) {
                    intent = new Intent(getApplicationContext(), BookingHistory.class);
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            mContext,
                            R.style.AlertDialogCustom_Destructive)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Delete Action
                                    startActivity(new Intent(mContext, Login.class));
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
                break;

            case 3:
                //fragment = new SettingsFragment();
                //title = getString(R.string.settings);
                intent = new Intent(getApplicationContext(), ShareApp.class);
                break;
            case 4:
                //fragment = new SettingsFragment();
                //title = getString(R.string.settings);
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                break;
            default:
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }

      /*if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStack();
                return true;

            case R.id.action_search:
                // search action
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_settings);
        MenuItem item2 = menu.findItem(R.id.action_search);
        item.setVisible(false);
        item2.setVisible(true);

        MenuItem item3;
        item3 = menu.findItem(R.id.write_review);
        item3.setVisible(false);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                AppLog.Log("onQueryTextSubmit", s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                AppLog.Log("onQueryTextChange", s);
                return false;
            }
        });
        return true;



        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

 /*   @Override
    public boolean onQueryTextSubmit(String query) {

        ((PubsDataAdaptor) TodayFragment.eventItemList.getAdapter()).setFilter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }*/

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }


}
