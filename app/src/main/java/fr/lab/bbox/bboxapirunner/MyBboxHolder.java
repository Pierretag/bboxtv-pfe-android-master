package fr.lab.bbox.bboxapirunner;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.TextView;
import android.widget.Toast;

import fr.bouyguestelecom.bboxapi.bboxapi.MyBbox;
import fr.bouyguestelecom.bboxapi.bboxapi.MyBboxManager;

public class MyBboxHolder {

    private static final String TAG = MyBboxHolder.class.getCanonicalName();

    public static MyBboxHolder mInstance = new MyBboxHolder();
    private MyBbox mBbox;
    private MyBboxManager bboxManager = new MyBboxManager();
    private Handler handler = new Handler();

    /**
     * Singleton: private constructor. Instance must be retrieved with getInstance method
     */
    private MyBboxHolder() {}

    public MyBboxManager getBboxManager() {
        return bboxManager;
    }

    public void bboxSearch(final Context context, final Activity activity){

        bboxManager.startLookingForBbox(context, new MyBboxManager.CallbackBboxFound() {
            @Override
            public void onResult(final MyBbox bboxFound) {

                // When we find our Bbox, we stopped looking for other Bbox.
                bboxManager.stopLookingForBbox();

                // We save our Bbox.
                mBbox = bboxFound;

                //Log.i(TAG, "Bbox found: " + mBbox.getIp());

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("bboxip", mBbox.getIp());
                editor.commit();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Bbox IP address saved", Toast.LENGTH_LONG).show();

                        TextView currentip = (TextView) activity.findViewById(R.id.currentip);
                        currentip.setText(mBbox.getIp());
                    }
                });
            }
        });

    }

    /**
     * set the current bbox
     *
     * @param ip bbox ip
     */
    public void setCustomBbox(String ip) {
        mBbox = new MyBbox(ip);
    }

    /**
     * Return the current bbox. null if not correctly initialized !
     *
     * @return the bbox.
     */
    public MyBbox getBbox() throws MyBboxNotFoundException {
        if (mBbox == null) {
            throw new MyBboxNotFoundException();
        }
        return mBbox;
    }

    public static MyBboxHolder getInstance() {
        return mInstance;
    }

    public interface IBboxSearchCallback {
        public void onBboxFound();
    }

}
