package fr.lab.bbox.bboxapirunner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import fr.lab.bbox.bboxapirunner.Application.ApplicationFragment;
import fr.lab.bbox.bboxapirunner.Media.MediaFragment;
import fr.lab.bbox.bboxapirunner.Notification.NotificationFragment;
import fr.lab.bbox.bboxapirunner.Security.SecurityFragment;
import fr.lab.bbox.bboxapirunner.UserInterface.UserInterfaceFragment;


/**
 * Created by pierr on 12/01/2017.
 */

public class MainActivity extends Activity
{


    private final static String TAG = MainActivity.class.getSimpleName();
    public Intent intent ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = new Intent(this,MyService.class);
        intent.putExtra("Extra","Extra");
        Log.i(TAG, "onCreate:  Start Service");
        startService(intent);
        Log.i(TAG ,"OnCreate: FINISH");

        finish();


    }

    protected void onDestroy() {
        super.onDestroy();
        //stopService(myi);

        Log.i("TVAPP","DESTROY");

    }


}


