package fr.lab.bbox.bboxapirunner;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.Toast;

import fr.lab.bbox.bboxapirunner.Demo.DemoDetection;




/**
 * Created by pierr on 12/01/2017.
 */

public class MainActivity extends Activity
{

    private final static String TAG = MainActivity.class.getSimpleName();

    public Intent intent;
    private boolean btnPressed = true;
    public static int POS_STATIC = 1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        Fragment fragment = new DemoDetection();
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }

        // Launch the device bluetooth detection service
        launchDetectionService();

        // Finish the Main Activity
        //finish();

    }

    /**
     * Launch the detection of bluetooth devices around the Bbox Miami
     */
    public void launchDetectionService() {

        intent = new Intent(this, MyService.class);
        intent.putExtra("Extra","Extra");
        Log.i(TAG, "START SERVICE");
        startService(intent);
    }

    /**
     * Get the remote events and the keys pressed
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            Log.e(TAG, "Key down, code " + event.getKeyCode());
            if (event.getKeyCode() == 23) {
                if (btnPressed) {
                    btnPressed = false;
                    Toast.makeText(this, "Button OK pressed", Toast.LENGTH_SHORT).show();
                } else {
                    btnPressed = true;
                    Toast.makeText(this, "Button OK pressed", Toast.LENGTH_SHORT).show();
                }
            }else if (event.getKeyCode()== 19) POS_STATIC ++;
            //else if (event.getKeyCode()== 24) POS_STATIC ++;
            else if (event.getKeyCode()== 20 && POS_STATIC != 1) POS_STATIC --;
            //else if (event.getKeyCode()== 25 && POS_STATIC != 1) POS_STATIC --;

        }

        return true;
    }

    protected void onDestroy() {
        super.onDestroy();
        //stopService(myi);
        Log.i("TVAPP","DESTROY");

    }
}


