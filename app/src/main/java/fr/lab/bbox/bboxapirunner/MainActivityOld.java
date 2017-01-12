package fr.lab.bbox.bboxapirunner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.lab.bbox.bboxapirunner.Application.ApplicationFragment;
import fr.lab.bbox.bboxapirunner.Media.MediaFragment;
import fr.lab.bbox.bboxapirunner.Notification.NotificationFragment;
import fr.lab.bbox.bboxapirunner.Security.SecurityFragment;
import fr.lab.bbox.bboxapirunner.UserInterface.UserInterfaceFragment;

public class MainActivityOld extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private final static String TAG = MainActivityOld.class.getSimpleName();

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                InputMethodManager imm = (InputMethodManager)MainActivityOld.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
        };;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.replace(R.id.container, new BboxFragment()).commit();

        BboxIpAddrDefault();

        View header = navigationView.getHeaderView(0);
        TextView id = (TextView) header.findViewById(R.id.app_id);
        id.setText("app_id : " + getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID));
        TextView secret = (TextView) header.findViewById(R.id.app_secret);
        secret.setText("app_secret : " + getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET));
    }

    @Override
    public void onBackPressed() {

        finish();

        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        */
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_security) {
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.replace(R.id.container, new SecurityFragment()).commit();
        } else if (id == R.id.nav_application) {
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.replace(R.id.container, new ApplicationFragment()).commit();
        } else if (id == R.id.nav_media) {
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.replace(R.id.container, new MediaFragment()).commit();
        } else if (id == R.id.nav_userinterface) {
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.replace(R.id.container, new UserInterfaceFragment()).commit();
        } else if (id == R.id.nav_notification) {
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.replace(R.id.container, new NotificationFragment()).commit();
        } else if (id == R.id.nav_bbox) {
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            mFragmentTransaction.addToBackStack(null);
            mFragmentTransaction.replace(R.id.container, new BboxFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void BboxIpAddrDefault()
    {
        String bboxip = "127.0.0.1";
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("bboxip", bboxip);
        editor.commit();
    }

    public void BboxIpAddrSave(View v)
    {
        EditText bboxipedit = (EditText) findViewById(R.id.bboxip);
        String bboxip = bboxipedit.getText().toString();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("bboxip", bboxip);
        editor.commit();

        TextView currentip = (TextView) findViewById(R.id.currentip);
        currentip.setText(bboxip);

        Log.i(TAG, "ip address : " + bboxip);

        Toast.makeText(this, "Bbox IP address saved", Toast.LENGTH_LONG).show();
    }

    public void AutoSearch(View v) throws MyBboxNotFoundException {
        MyBboxHolder.getInstance().bboxSearch(getBaseContext(), MainActivityOld.this);
    }


}
