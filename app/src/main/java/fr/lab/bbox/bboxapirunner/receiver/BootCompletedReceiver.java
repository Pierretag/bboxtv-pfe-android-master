package fr.lab.bbox.bboxapirunner.receiver;

/**
 * Created by pierr on 26/01/2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import fr.lab.bbox.bboxapirunner.MyService;

/**
 * @author Bertrand Martel
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    private String TAG = BootCompletedReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Toast.makeText(context, "boot", Toast.LENGTH_LONG).show();
            Log.i(TAG, "Boot completed : starting services in receiver");
            Intent serviceIntent = new Intent(context, MyService.class);
            context.startService(serviceIntent);
        }
    }
}
