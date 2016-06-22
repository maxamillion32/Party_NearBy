package com.app.pubs;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.app.adaptors.PubsDataAdaptor;
import com.app.adaptors.ViewPagerAdapter;
import com.app.fragments.FragmentDrawer;
import com.app.fragments.LaterFragment;
import com.app.fragments.TodayFragment;
import com.app.fragments.TomorrowFragment;
import com.app.utility.AppLog;
import com.app.utility.SessionManager;
import com.app.utility.Singleton;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {
    private Toolbar toolbar;
    private Context mContext;
    private FragmentDrawer drawerFragment;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout htab_appbar;
    private SessionManager sessionManager;
    private  TabLayout tabLayout;
    private TodayFragment todayFragment;
    private TomorrowFragment tomorrowFragment;
    private LaterFragment laterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        sessionManager = new SessionManager(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Pubs Nearby");
            //getSupportActionBar().setIcon(R.drawable.logo);
            //toolbar.setNavigationIcon(R.drawable.logo);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
       final ViewPager viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        setupViewPager(viewPager);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);

       // viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                AppLog.Log("tab_getPosition: ", tab.getPosition() +"");
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0) {
                    //TodayFragment.auth.EventListService("tod.json");

                } else if(tab.getPosition() == 1) {
                   //TomorrowFragment.auth.EventListService("tom.json");

                } else if(tab.getPosition() == 2) {
                    //LaterFragment.auth.EventListService("lat.json");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.header_1);

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


       /* tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        //Singleton.getInstance(mContext).ShowToastMessage("One", mContext);
                        break;
                    case 1:
                        //Singleton.getInstance(mContext).ShowToastMessage("2", mContext);

                        break;
                    case 2:
                        //Singleton.getInstance(mContext).ShowToastMessage("3", mContext);

                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/



    }
/*
    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0 :
                replaceFragment(todayFragment);
                break;
            case 1 :
                replaceFragment(tomorrowFragment);
                break;
            case 2 :
                replaceFragment(laterFragment);
                break;
        }
    }*/

/*    private void setupTabLayout() {
        todayFragment = new TodayFragment();
        tomorrowFragment = new TomorrowFragment();
        laterFragment = new LaterFragment();
        tabLayout.addTab(tabLayout.newTab().setText("TODAY"));
        tabLayout.addTab(tabLayout.newTab().setText("TOMORROW"));
        tabLayout.addTab(tabLayout.newTab().setText("LATER"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }*/

   private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TodayFragment(getResources().getColor(R.color.white)), "TODAY"); //accent_material_light
        adapter.addFrag(new TomorrowFragment(getResources().getColor(R.color.white)), "TOMORROW"); //ripple_material_light
        adapter.addFrag(new LaterFragment(getResources().getColor(R.color.white)), "LATER"); //button_material_dark
        viewPager.setAdapter(adapter);
    }


/*    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }*/


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
                intent = new Intent(getApplicationContext(), NearBy.class);
                break;
            case 2:
                //fragment = new MessagesFragment();
                //title = getString(R.string.title_messages);
                intent = new Intent(getApplicationContext(), BookingHistory.class);
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

 /*   @Override
    public boolean onQueryTextSubmit(String query) {

        ((PubsDataAdaptor) TodayFragment.eventItemList.getAdapter()).setFilter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }*/
}
