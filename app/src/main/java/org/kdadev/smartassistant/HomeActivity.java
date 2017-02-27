package org.kdadev.smartassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences prefs;
    private static long time=0;
    public static Toast transitionToast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences prefs = this.getSharedPreferences("general_settings", this.MODE_PRIVATE);
        String name_sett = prefs.getString("name", null);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home Condition");
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_chat));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_weather));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_map));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final HomeFragmentPagerAdapter adapter = new HomeFragmentPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setOffscreenPageLimit(tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
                if(tab.getPosition()==0){
                    mActionBarToolbar.setTitle("Home Condition");
                } else if(tab.getPosition()==1){
                    mActionBarToolbar.setTitle("Chat");
                } else if(tab.getPosition()==2){
                    mActionBarToolbar.setTitle("Weather");
                } else{
                    mActionBarToolbar.setTitle("Maps");
                }
                setSupportActionBar(mActionBarToolbar);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        HomeActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.setDrawerListener(toggle);
                toggle.syncState();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        TextView name = (TextView) hView.findViewById(R.id.name_nav);
        TextView email = (TextView) hView.findViewById(R.id.email_nav);
        ImageView profile_pic = (ImageView) hView.findViewById(R.id.picture_nav);
        if(name_sett!=null){
            name.setText(name_sett);
        }
        else{
            name.setText(currentUser.getDisplayName());
        }

        email.setText(currentUser.getEmail());
        if(currentUser.getPhotoUrl()!=null){
            Picasso.with(this).load(currentUser.getPhotoUrl()).into(profile_pic);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (time +2000>System.currentTimeMillis()){
                moveTaskToBack(true);
            }
            else{
                Toast.makeText(HomeActivity.this, "Press once again to exit", Toast.LENGTH_SHORT).show();
            }
            time = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent (HomeActivity.this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void alertSignOut(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        // set title
        alertDialogBuilder.setTitle("Sign out");
        alertDialogBuilder
                .setMessage("Are you sure you want to sign out?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        transitionToast = new Toast(HomeActivity.this);
                        transitionToast.makeText(HomeActivity.this, "Signing out ...", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        startActivityForResult(new Intent(HomeActivity.this, MainActivity.class), 0, null);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.cancel();
                    }
                })
                .setIcon(R.drawable.ic_menu_logout);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_compass) {
            startActivity(new Intent(HomeActivity.this,CompassActivity.class));

            // Handle the camera action
        } else if (id == R.id.nav_pedometer) {
            startActivity(new Intent(HomeActivity.this,PedometerActivity.class));
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_signout) {
            alertSignOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        TextView name = (TextView) hView.findViewById(R.id.name_nav);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String name_sett = prefs.getString("name", null);
        if(name!=null){
            name.setText(name_sett);
        }

    }
}
