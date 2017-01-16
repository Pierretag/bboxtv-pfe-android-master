package fr.lab.bbox.bboxapirunner;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.ListIterator;

import fr.bouyguestelecom.bboxapi.bboxapi.Bbox;
import fr.bouyguestelecom.bboxapi.bboxapi.MyBbox;
import fr.bouyguestelecom.bboxapi.bboxapi.MyBboxManager;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetCurrentChannel;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetSessionId;
import fr.bouyguestelecom.bboxapi.bboxapi.model.Channel;
import okhttp3.Request;

/**
 * Created by pierr on 12/01/2017.
 */

public class MyService extends Service {


    public final static boolean SEND_TO_CLIENT = true;

    private MyBboxManager bboxManager;
    public MyBbox mBbox;
    final static String TAG = MyService.class.getCanonicalName();
    public Context context;
    public String SessionId;
    Client mClient;
    Channel mChannel = new Channel();
    Channel mChannelMinusOne = new Channel();
    public int posIdT, posIdTMinusOne;
    public boolean wait = false;


    public int mPosId = 0;


    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothObject> btFoundT, btFoundTMinusOne;
    private boolean presence;
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                // Get the bluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // Get the "RSSI" to get the signal strength as integer,
                // but should be displayed in "dBm" units
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

                // Create the device object and add it to the arrayList of devices
                BluetoothObject bluetoothObject = new BluetoothObject();
                bluetoothObject.setBluetooth_name(device.getName());
                bluetoothObject.setBluetooth_address(device.getAddress());
                bluetoothObject.setBluetooth_state(device.getBondState());
                bluetoothObject.setBluetooth_type(device.getType());    // requires API 18 or higher
                bluetoothObject.setBluetooth_uuids(device.getUuids());
                bluetoothObject.setBluetooth_rssi(rssi);
                bluetoothObject.setStart();
                btFoundT = macAddrFilter(bluetoothObject, btFoundT);
                Log.i(TAG, "onReceive: " + bluetoothObject.getBluetooth_address());

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //int posID = MyService.this.GetTvId();

                MyService.this.GetTvId();


                if (MyService.this.mBluetoothAdapter.isDiscovering()) {
                    MyService.this.mBluetoothAdapter.cancelDiscovery();
                }

                if (btFoundTMinusOne.isEmpty()) {
                    btFoundTMinusOne = btFoundT;
                    btFoundT = new ArrayList<BluetoothObject>();
                } else {
                    ArrayList<BluetoothObject> diff = tabBuffer(btFoundTMinusOne, btFoundT);
                    if (SEND_TO_CLIENT == true && !diff.isEmpty())
                        mClient.SendToServer(diff, posIdT);

                }

                while(wait == false){}
                wait = false;
                if (mChannel.getPositionId() != mChannelMinusOne.getPositionId()) {
                    Log.i(TAG, "onReceive: POSIDT = " + mChannel.getPositionId() + " POSIDTMinusOne = "+mChannelMinusOne.getPositionId());
                    mClient.SendToServer(btFoundTMinusOne, mChannelMinusOne.getPositionId());
                    mChannelMinusOne = mChannel;


                    btFoundTMinusOne.clear();
                }

                // Clear list of devices then restart the discovery
                Log.i(TAG, "RESTART");

                btFoundT.clear();
                MyService.this.mBluetoothAdapter.startDiscovery();

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
                Log.i(TAG, "FINISH");
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
                Log.i(TAG, "STARTED");
        }
    };




    @Nullable


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {

    }

    private ArrayList<BluetoothObject> tabBuffer(ArrayList<BluetoothObject> before, ArrayList<BluetoothObject> after) {

        ArrayList<BluetoothObject> diff = new ArrayList<BluetoothObject>();
        for (BluetoothObject bt : after) {
            if (!before.contains(bt)) {
                before.add(bt);
            }
        }
        ListIterator<BluetoothObject> btIterator = before.listIterator();
        while (btIterator.hasNext()) {
            BluetoothObject bt = btIterator.next();
            if (!after.contains(bt)) {
                diff.add(bt);
                btIterator.remove();
            }
        }

        return diff;
    }


    private ArrayList<BluetoothObject> macAddrFilter(BluetoothObject bluetoothObject, ArrayList<BluetoothObject> arrayOfFoundBTDevices) {

        Log.i(TAG, "size = " + arrayOfFoundBTDevices.size());

        if (arrayOfFoundBTDevices.size() == 0) {
            arrayOfFoundBTDevices.add(bluetoothObject);
            Log.i(TAG, bluetoothObject.getBluetooth_address() + " (" + bluetoothObject.getBluetooth_name() + ") " + " ajouté à la table");
        }

        presence = true;

        if (arrayOfFoundBTDevices.contains(bluetoothObject)) presence = false;

        /*for (int i = 0; i < btFoundT.size(); i++) {
            if (btFoundT.get(i).getBluetooth_address().contains(bluetoothObject.getBluetooth_address())) {
                presence = false;
            }
         }
            */
        if (presence) {
            arrayOfFoundBTDevices.add(bluetoothObject);
            Log.i(TAG, bluetoothObject.getBluetooth_address() + " (" + bluetoothObject.getBluetooth_name() + ") " + " ajouté à la table");
        } else {
            Log.i(TAG, bluetoothObject.getBluetooth_address() + " (" + bluetoothObject.getBluetooth_name() + ") " + " existe deja dans la table");
        }

        return arrayOfFoundBTDevices;
    }


    public int InitBluetooth() {

        btFoundT = new ArrayList<BluetoothObject>();
        btFoundTMinusOne = new ArrayList<BluetoothObject>();
        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(BluetoothDevice.ACTION_FOUND);
        theFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        Log.i(TAG, "InitBluetooth: ");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mClient = new Client();
        mClient.tryGetIp();

        registerReceiver(mReceiver, theFilter);
        Log.i(TAG, "InitBluetooth: " + mBluetoothAdapter.enable());

        mBluetoothAdapter.startDiscovery();
        if (!mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.startDiscovery();
            Log.i(TAG, "DISCOVERY");
        } else {
            mBluetoothAdapter.cancelDiscovery();
            Log.i(TAG, "Already");
            mBluetoothAdapter.startDiscovery();
        }


        return 0;
    }


    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: " + " start");

        bboxManager = new MyBboxManager();
        context = this.getApplicationContext();

        Log.i(TAG, "onStartCommand: " + " myBboxHolder");
        MyBboxHolder.getInstance().getBboxManager().startLookingForBbox(this.getBaseContext(), new MyBboxManager.CallbackBboxFound() {
            @Override
            public void onResult(MyBbox myBbox) {
                bboxManager.stopLookingForBbox();
                Log.i(TAG, "onResult: OK BBox Found");
                // We save our Bbox.
                mBbox = myBbox;
                wait = true;
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("bboxip", mBbox.getIp());

                editor.commit();

            }
        });

        while (wait == false) {
        }

        wait = false;

        InitBluetooth();


        Log.d(TAG, "onStartCommand: " + "setsessionId");
        SetSessionId();


        Log.d(TAG, "onStartCommand: " + " TVID");

        /*
        Bbox.getInstance().addListener(mBbox.getIp(),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                new IBboxMedia() {
                    @Override
                    public void onNewMedia(MediaResource media) {
                        mPosId = media.getPositionId();

                        Log.i(TAG +"TV", "onNewMedia: " + mPosId);
                    }
                });

        Bbox.getInstance().addListener(mBbox.getIp(),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                new IBboxMessage() {
                    @Override
                    public void onNewMessage(MessageResource message) {
                        Log.i(TAG, "DEB onNewMessage: " + message.getMessage());
                    }
                });
        */


        GetTvId();


        Log.d(TAG, "onStartCommand: " + " FINISH");
        return Service.START_NOT_STICKY;
    }


    public void GetTvId() {
        Bbox.getInstance().getCurrentChannel(mBbox.getIp(),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                new IBboxGetCurrentChannel() {
                    @Override
                    public void onResponse(Channel channel) {
                        wait = true;
                        mChannel = channel;
                        //posIdT = channel.getPositionId();
                        wait = true;
                        Log.i(TAG, "onResponse: " + channel.getPositionId() + " POS ID  bbox" + channel.getPositionIdBbox());
                    }

                    @Override
                    public void onFailure(Request request, int errorCode) {
                        Log.d(TAG, "onFailure: " + errorCode);
                    }
                });


    }


    public void SetSessionId() {
        Bbox.getInstance().getSessionId(mBbox.getIp(),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                new IBboxGetSessionId() {
                    @Override
                    public void onResponse(String sessionId) {
                        SessionId = sessionId;
                        wait = true;
                        Log.i(TAG, "onResponse: " + SessionId);
                    }

                    @Override
                    public void onFailure(Request request, int errorCode) {
                        Log.e(TAG, "onFailure: " + errorCode);
                    }
                });
    }

}
