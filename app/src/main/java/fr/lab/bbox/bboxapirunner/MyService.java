package fr.lab.bbox.bboxapirunner;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import fr.bouyguestelecom.bboxapi.bboxapi.Bbox;
import fr.bouyguestelecom.bboxapi.bboxapi.MyBbox;
import fr.bouyguestelecom.bboxapi.bboxapi.MyBboxManager;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetCurrentChannel;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetSessionId;
import fr.bouyguestelecom.bboxapi.bboxapi.model.Channel;
import fr.lab.bbox.bboxapirunner.Demo.DemoConstants;
import okhttp3.Request;

/**
 * Created by pierr on 12/01/2017.
 */

public class MyService extends Service {

    private final static String TAG = MyService.class.getCanonicalName();

    public final static boolean SEND_TO_CLIENT = true;
    public final static boolean DEMO = false;
    Timer t;

    private MyBboxManager bboxManager;
    public MyBbox mBbox;

    public Context context;
    public String SessionId;

    Client mClient;
    Channel mChannel = new Channel();
//    Channel mChannelMinusOne = new Channel();

    public int posIdT, posIdTMinusOne;
    public int myPreviousPosId = 0;
    private boolean presence;
    private int smoothingConst = 0;
    private int nbScan = 4;
    private int RSSILimit = -75;
    public boolean isDisco = false;
    public boolean isAlive = true;
    private BluetoothAdapter mBluetoothAdapter;
    private ArrayList<BluetoothObject> btFoundT, tempArray;
    public ArrayList<BluetoothObject> btFoundTMinusOne;


    /**
     * BroadcastReceiver that process bluetooth new devices
     */
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

                // Get all the bluetooth devices in a given field
                tempArray = RSSIFilter(bluetoothObject, tempArray);

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                // Clear list of devices then restart the discovery
                Log.i(TAG, "DISCOVERY_FINISHED : RESTART");

                //int posID = MyService.this.GetTvId();

                if(!DEMO && mBbox != null)MyService.this.GetTvId();

                if (MyService.this.mBluetoothAdapter.isDiscovering()) {
                    MyService.this.mBluetoothAdapter.cancelDiscovery();
                }

                // Make a condensed array with the 'nbScan' last scans of bluetooth devices
               if(!DEMO) smoothArrayOfDevices(btFoundT, tempArray);

                btFoundT = new ArrayList<BluetoothObject>(tempArray);
                // Clear the arrays of devices
                tempArray.clear();
                displayArray(btFoundT,"RESULT");

                // Restart Discovery
                //MyService.this.mBluetoothAdapter.startDiscovery();
                isDisco = false;

            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
                Log.i(TAG, "STARTED");
        }
    };









    @Nullable

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(context,"MAKING CON",Toast.LENGTH_LONG);
        return binder;
    }

    public void onCreate() {

    }

    /**
     * Get the differences between the array of devices at T and the array of devices at T-1
     *
     * @param before
     * @param after
     * @return
     */
    private ArrayList<BluetoothObject> tabBuffer(ArrayList<BluetoothObject> before, ArrayList<BluetoothObject> after) {

        ArrayList<BluetoothObject> diff = new ArrayList<BluetoothObject>();
        // on ajoute toutes les devices qui ne sont pas dans Before mais dans After
        for (BluetoothObject bt : after) {
            if (!before.contains(bt)) {
                before.add(bt);
            }
        }

        // on retire toutes les devices qui sont dans Before mais pas dans After
        // et elles seront envoyé
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

    /**
     * Avoid to have 2 times the same bluetooth device in the array - filtering by MAC address
     *
     * @param bluetoothObject
     * @param arrayOfFoundBTDevices
     * @return
     */
    private ArrayList<BluetoothObject> macAddrFilter(BluetoothObject bluetoothObject, ArrayList<BluetoothObject> arrayOfFoundBTDevices) {

        if (arrayOfFoundBTDevices.size() == 0) {
            arrayOfFoundBTDevices.add(bluetoothObject);
            Log.i(TAG, bluetoothObject.getBluetooth_address() +
                    " (" + bluetoothObject.getBluetooth_name() + ") "
                    + "rssi :" + bluetoothObject.getBluetooth_rssi()
                    + " ajouté à  la table");
            //Log.i(TAG, "size = " + arrayOfFoundBTDevices.size());
        }

        presence = true;
        if (arrayOfFoundBTDevices.contains(bluetoothObject)) {
            presence = false;
        }

        if (presence) {
            arrayOfFoundBTDevices.add(bluetoothObject);
            Log.i(TAG, bluetoothObject.getBluetooth_address() +
                    " (" + bluetoothObject.getBluetooth_name() + ") "
                    + "rssi :" + bluetoothObject.getBluetooth_rssi()
                    + " ajouté à  la table");
            //Log.i(TAG, "size = " + arrayOfFoundBTDevices.size());
        } else {
            //Log.i(TAG, bluetoothObject.getBluetooth_address() + " (" + bluetoothObject.getBluetooth_name() + ") " + " existe deja dans la table");
        }

        return arrayOfFoundBTDevices;
    }

    /**
     * Filter the found devices by their RSSI (higher than RSSILimit)
     *
     * @param bluetoothObject
     * @param arrayOfFoundBTDevices
     * @return
     */
    private ArrayList<BluetoothObject> RSSIFilter(BluetoothObject bluetoothObject, ArrayList<BluetoothObject> arrayOfFoundBTDevices) {

        if (bluetoothObject.getBluetooth_rssi() > RSSILimit) {
            arrayOfFoundBTDevices = macAddrFilter(bluetoothObject, arrayOfFoundBTDevices);
            //Log.i(TAG, "RSSI : " + bluetoothObject.getBluetooth_rssi() + " (" + bluetoothObject.getBluetooth_name() + ") dans le champ");
        } else {
            //Log.i(TAG, "RSSI : " + bluetoothObject.getBluetooth_rssi() + " (" + bluetoothObject.getBluetooth_name() + ") trop loin");
        }

        return arrayOfFoundBTDevices;
    }

    /**
     * Create a more complete array made with the last 'nbScan' bluetooth scans
     *
     * @param tempArray
     * @param btFoundT
     * @return
     */
    private void smoothArrayOfDevices(ArrayList<BluetoothObject> btFoundT, ArrayList<BluetoothObject> tempArray) {


        // Add the first array of found devices
        if (btFoundT.size() == 0) {
            btFoundT.addAll(tempArray);
            smoothingConst++;
        } else { // Compare 'tempArray' with 'btFoundT' and add non existing elements
            for (int i = 0; i < tempArray.size(); i++) {
                // The bluetooth object already exists in btFoundT
                if (btFoundT.contains(tempArray.get(i))) {
                    //Log.i(TAG, "Existe dÃ©jÃ  dans 'btFoundT");
                } else { // The bluetooth doesn't exist in btFoundT
                    btFoundT.add(tempArray.get(i));
                }
            }
            smoothingConst++;
        }

        if (smoothingConst > nbScan) {
            Log.i(TAG, "Tableau condensé de devices créé");

            // Display the 'array'
            displayArray(btFoundT, "btFoundT");

            //while(wait == false){}
            //wait = false;

            if (btFoundTMinusOne.isEmpty()) {
                btFoundTMinusOne.addAll(btFoundT);

                //btFoundT = new ArrayList<BluetoothObject>();
            } else {
                ArrayList<BluetoothObject> diff = tabBuffer(btFoundTMinusOne, btFoundT);

                if(btFoundTMinusOne.size()==0 && btFoundT.size() == 0 ) isAlive = false;
                else isAlive = true;


                if (SEND_TO_CLIENT == true && !diff.isEmpty()) {
                    mClient.SendToServer(diff, posIdT);
                    displayArray(diff, "diff");
                    Log.i(TAG, "smoothArrayOfDevices:" + "Number of devices =  " + diff.size() + "posID " +posIdT);
                }
                if (posIdT != myPreviousPosId && !DEMO) {
                    Log.i(TAG, "onReceive: POSIDT = " + posIdT + " POSIDTMinusOne = " + myPreviousPosId);
                    mClient.SendToServer(btFoundTMinusOne, myPreviousPosId);
                    myPreviousPosId = posIdT;

                    btFoundTMinusOne.clear();
                }

                // Clear the array of devices
                btFoundT.clear();

                // Reset the constant at 0
                smoothingConst = 0;

            }
        }

        // Envoie immédiat après changement de chaine

        if (posIdT!= myPreviousPosId && DEMO) {
            //Log.i(TAG, "onReceive: POSIDT = " + mChannel.getPositionId() + " POSIDTMinusOne = " + myPreviousPosId);
            Log.i(TAG, "onReceive: POSIDT = " +posIdT + " POSIDTMinusOne = " + myPreviousPosId);

            mClient.SendToServer(btFoundTMinusOne, myPreviousPosId);
            //myPreviousPosId = mChannel.getPositionId();
            myPreviousPosId = posIdT;
            btFoundTMinusOne.clear();


            // Clear the array of devices
            btFoundT.clear();

            // Reset the constant at 0
            smoothingConst = 0;
        }

    }

    /**
     * print into log the given array
     * @param mArray
     * @param nameArray
     */
    public void displayArray(ArrayList<BluetoothObject> mArray, String nameArray ){
        for (int j = 0; j < mArray.size(); j++) {
            Log.i(TAG , "{ArrayList}" + nameArray + " : (" + mArray.get(j).getBluetooth_name() + ") " + mArray.get(j).getBluetooth_address()
                    + " Start = " + mArray.get(j).getStart());
        }
    }

    /**
     * Initialization of the bluetooth device
     * first create the implicit intent for the broadcast recevier
     * initialize the bluetooth adapter and the web client
     * Fetch the localisation of the bbox
     * Register the broadcast
     *
     * @return
     */
    public int initBluetooth() {
        btFoundT = new ArrayList<BluetoothObject>();
        btFoundTMinusOne = new ArrayList<BluetoothObject>();
        tempArray = new ArrayList<BluetoothObject>();
        t = new Timer();
        DemoConstants.actualDevices = new ArrayList<BluetoothObject>();

        final IntentFilter theFilter = new IntentFilter();
        theFilter.addAction(BluetoothDevice.ACTION_FOUND);
        theFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        //Log.i(TAG, "initBluetooth: ");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mClient = new Client();
        mClient.tryGetIp();

        registerReceiver(mReceiver, theFilter);
        Log.i(TAG, "initBluetooth: " + mBluetoothAdapter.enable());


        // Deamon who start and stop the discovery of the bluetooth
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(isDisco == true){
                    mBluetoothAdapter.cancelDiscovery();
                    Log.i(TAG, "run: FORCE STOP");
                    isDisco = false;

                }
                else if(isDisco == false) {
                    mBluetoothAdapter.startDiscovery();
                    isDisco = true;
                    Log.i(TAG, "run: FORCE RUN");
                }


            }
        },0,30*1000);


        //mBluetoothAdapter.startDiscovery();
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

    /**
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: " + " start");
        DemoConstants.actualDevices = new ArrayList<BluetoothObject>();
        DemoConstants.actualDevices = btFoundTMinusOne;
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
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("bboxip", mBbox.getIp());

                editor.commit();

                Log.d(TAG, "onStartCommand: " + "setsessionId");
                SetSessionId();

                Log.d(TAG, "onStartCommand: " + " TVID");
                if(DEMO){
                    myPreviousPosId = 166;
                }
            }


        });

        initBluetooth();

        Log.d(TAG, "onStartCommand: " + " FINISH");

        return Service.START_NOT_STICKY;
    }


    public int GetTvId() {

        Bbox.getInstance().getCurrentChannel(mBbox.getIp(),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                new IBboxGetCurrentChannel() {
                    @Override
                    public void onResponse(Channel channel) {
                        mChannel = channel;
                        posIdT = channel.getPositionId();
                        //MainActivity.POS_STATIC = channel.getPositionId();
                        Log.i(TAG, "onResponse: " + channel.getPositionId() + " POS ID  bbox" + channel.getPositionIdBbox());

                    }

                    @Override
                    public void onFailure(Request request, int errorCode) {
                        Log.d(TAG, "onFailure: bb " + errorCode);
                    }
                });
        return mChannel.getPositionId();
    }


    public void SetSessionId() {
        Bbox.getInstance().getSessionId(mBbox.getIp(),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                new IBboxGetSessionId() {
                    @Override
                    public void onResponse(String sessionId) {
                        SessionId = sessionId;
                        Log.i(TAG, "onResponse: " + SessionId);
                    }

                    @Override
                    public void onFailure(Request request, int errorCode) {
                        Log.e(TAG, "onFailure: " + errorCode);
                    }
                });
    }








    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy: " +" DESTROY");
        mBluetoothAdapter.cancelDiscovery();
        unregisterReceiver(mReceiver);
        t.cancel();
        super.onDestroy();


    }

    /**
     * Implmemen
     */
    public final IService.Stub binder = new IService.Stub(){


        @Override
        public boolean getDevice(String Number) throws RemoteException {
            return btFoundT.contains(new BluetoothObject(Number));
        }

        @Override
        public int getNumberOfDevice() throws RemoteException {
            return btFoundT.size();
        }

        @Override
        public boolean isStillWorking() throws RemoteException {
            return !btFoundT.isEmpty();
        }

        @Override
        public List<String> getDevicesList() throws RemoteException {
            ArrayList<String> mArrayList = new ArrayList<String>();
            List<String> mList;
            for (BluetoothObject b:btFoundT) {
                mArrayList.add(b.getBluetooth_name()+"  " + b.getBluetooth_address());
            }

            mList = (List<String>) mArrayList;
            return mList;
        }
    };
}
