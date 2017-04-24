package paztechnologies.com.meribus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import paztechnologies.com.meribus.navigation.ExpandableListadapter;
import paztechnologies.com.meribus.navigation.ExpandedMenuModel;

public class Home extends AppCompatActivity {
    ExpandableListAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<ExpandedMenuModel> listDataHeader;
    HashMap<ExpandedMenuModel, List<String>> listDataChild;
    Toolbar toolbar;
    ImageView navigation;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //   final android.support.v7.app.ActionBar ab = getSupportActionBar();
        /* to set the menu icon image*/
        navigation = (ImageView) findViewById(R.id.navigaion);
        toolbar=(Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        // ab.setHomeAsUpIndicator(R.mipmap.toggel);
        //  ab.setDisplayHomeAsUpEnabled(true);
        // ab.setBackgroundDrawable(null);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        navigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.END);
            }
        });
        prepareListData();
        mMenuAdapter = new ExpandableListadapter(this, listDataHeader, listDataChild, expandableList);

        // setting list adapter
        expandableList.setAdapter(mMenuAdapter);

        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if((listDataChild.get(listDataHeader.get(i)).get(i1).equals("Book Monthly Seat"))){

                    mDrawerLayout.closeDrawers();

                }
                if((listDataChild.get(listDataHeader.get(i)).get(i1).equals("Book Per Day Seat"))){

                    mDrawerLayout.closeDrawers();

                }
                if((listDataChild.get(listDataHeader.get(i)).get(i1).equalsIgnoreCase("My Bookings"))){
                    My_Booking book= new My_Booking();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container,book);
                    ft.commit();
                    mDrawerLayout.closeDrawers();
                }

                return false;
            }
        });
        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                //Log.d("DEBUG", "heading clicked");

                if(listDataHeader.get(i).getIconName().equals("My Account")){
                    Profile book= new Profile();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container,book);
                    ft.commit();
                    mDrawerLayout.closeDrawers();

                }
                if(listDataHeader.get(i).getIconName().equals("Careers")){

                    mDrawerLayout.closeDrawers();

                } else if (listDataHeader.get(i).getIconName().equals("New Booking")) {
                    My_Booking book = new My_Booking();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, book);
                    ft.commit();
                    mDrawerLayout.closeDrawers();
                } else if (listDataHeader.get(i).getIconName().equals("Track My Bus/Car")) {
                    Track_Bus book = new Track_Bus();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, book);
                    ft.commit();
                    mDrawerLayout.closeDrawers();
                } else if (listDataHeader.get(i).getIconName().equals("Log Out")) {
                    SharedPreferences sharedPreferences = getSharedPreferences("app", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    mDrawerLayout.closeDrawers();
                    Intent in = new Intent(Home.this, Login.class);
                    startActivity(in);
                    finish();
                }
                return false;
            }
        });
        expandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
               //s Toast.makeText(getApplicationContext(),""+listDataHeader.get(groupPosition).getIconName(),3).show();

            }
        });
        android.support.v7.app.ActionBarDrawerToggle actionBarDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }


        };


        //   actionBarDrawerToggle.setDrawerIndicatorEnabled(false);

//        Drawable drawable = ResourcesCompat.getDrawable(getResources(),   R.mipmap.toggel, getTheme());
//
//        actionBarDrawerToggle.setHomeAsUpIndicator(drawable);
//        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mDrawerLayout.isDrawerVisible(Gravity.RIGHT)) {
//                    mDrawerLayout.closeDrawer(Gravity.RIGHT);
//                } else {
//                    mDrawerLayout.openDrawer(Gravity.RIGHT);
//                }
//            }
//        });
        //Setting the actionbarToggle to drawer layout
        //  mDrawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        // actionBarDrawerToggle.syncState();
        Booking_Tab main_page= new Booking_Tab();
        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,main_page);
        fragmentTransaction.commit();
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<ExpandedMenuModel>();
        listDataChild = new HashMap<ExpandedMenuModel, List<String>>();



        ExpandedMenuModel item2 = new ExpandedMenuModel();
        item2.setIconName("New Booking");
        item2.setIconImg(R.drawable.booking);
        listDataHeader.add(item2);
        ExpandedMenuModel item1 = new ExpandedMenuModel();
        item1.setIconName("My Account");
        item1.setIconImg(R.drawable.profile);
        // Adding data header
        listDataHeader.add(item1);
        ExpandedMenuModel item3 = new ExpandedMenuModel();
        item3.setIconName("Track My Bus/Car");
        item3.setIconImg(R.drawable.location);
        listDataHeader.add(item3);

        ExpandedMenuModel item4 = new ExpandedMenuModel();
        item4.setIconName("Privacy Policy");
        item4.setIconImg(R.drawable.privatepolicy);
        listDataHeader.add(item4);

        ExpandedMenuModel item5 = new ExpandedMenuModel();
        item5.setIconName("Partners/Clients");
        item5.setIconImg(R.drawable.partners);
        listDataHeader.add(item5);

        ExpandedMenuModel item6 = new ExpandedMenuModel();
        item6.setIconName("Careers");
        item6.setIconImg(R.drawable.career);
        listDataHeader.add(item6);

        ExpandedMenuModel item7= new ExpandedMenuModel();
        item7.setIconName("Log Out");
        item7.setIconImg(R.drawable.logout);
        listDataHeader.add(item7);
        // Adding child data
        List<String> heading1 = new ArrayList<String>();

        heading1.add("Book Monthly Seat");
        heading1.add("Book Per Day Seat");
        heading1.add("My Bookings");
        List<String> heading2 = new ArrayList<String>();
        heading2.add("Our Policy");
        heading2.add("Terms Of Use");
        heading2.add("Cancellations & Refunds");
        heading2.add("FAQS");

//        listDataChild.put(listDataHeader.get(1), heading1);// Header, Child data
//        listDataChild.put(listDataHeader.get(3), heading2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    private void setupDrawerContent(NavigationView navigationView) {
        //revision: this don't works, use setOnChildClickListener() and setOnGroupClickListener() above instead
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

}


