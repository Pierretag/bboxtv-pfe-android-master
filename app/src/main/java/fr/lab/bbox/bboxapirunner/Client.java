package fr.lab.bbox.bboxapirunner;

import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.iotf.client.device.DeviceClient;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by Victor on 09/01/2017.
 */
public class Client extends AppCompatActivity {

    Properties prop;
    DeviceClient client;
    Timestamp timestamp;
    public final static String TAG = Client.class.getCanonicalName();

    public String json;

    OkHttpClient web;
    Request request ;
    public Client(){

        prop = new Properties();
        client = null;
    }

    public void tryGetIp(){
        web = new OkHttpClient();
        request = new Request.Builder().url("http://ip-api.com/json/").build();
        Log.i(TAG, "****try");

        web.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "****onFailure: NON ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                json = response.body().string();
                Log.i(TAG, "****onResponse: " + json);

                try {
                    JSONObject jo = new JSONObject(json);
                    ConnexionConstants.city = jo.optString("city");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Log.i(TAG, "********onResponse: " + ConnexionConstants.city);

            }

        });

    }


    public void SendToServer(ArrayList<BluetoothObject> listDevices, int posID){

        JsonObject messageToSend = new JsonObject();

        //Connexion property
        prop.put("org", ConnexionConstants.Org);
        prop.put("type", ConnexionConstants.Type);
        prop.put("id", ConnexionConstants.Id);
        prop.put("auth-method", ConnexionConstants.AuthMethod);
        prop.put("auth-token", ConnexionConstants.AuthToken);


        JsonObject dElement = new JsonObject();
        JsonObject message = new JsonObject();


        //BBOX
        JsonObject bbox = new JsonObject();
        bbox.addProperty("serialNumber", Build.SERIAL);
        bbox.addProperty("MAC","");
        bbox.addProperty("IP", "176.135.254.139");
        bbox.addProperty("location",ConnexionConstants.city);
        message.add("bbox", bbox);



        //Timestemp
        timestamp = new Timestamp(System.currentTimeMillis());
        CharSequence format = DateFormat.format("yyyy-MM-dd'T'HH:mm:ssZ", timestamp);

        message.addProperty("timestamp", format.toString());
        timestamp = new Timestamp(System.currentTimeMillis()-5400000);

        //format = DateFormat.format("yyyy-MM-dd'T'HH:mm:ssZ", timestamp);
//        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
  //      String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
  //      Log.i("******IP", ip);
        //message.addProperty("startTimestamp", format.toString());
        Log.i(TAG, "SendToServer: " + format.toString());
        //TV
        JsonObject tv = new JsonObject();
        tv.addProperty("positionId",posID);
        message.add("tv", tv);


        //APP
        JsonObject app = new JsonObject();
        app.addProperty("appId", 0);
        message.add("app", app);

        //Device
        JsonArray arrayDevices = new JsonArray();
        //Generate JSON for One device to send to server
        if(!listDevices.isEmpty()){
            for(int counter=0; counter<listDevices.size();  counter++){
                BluetoothObject tempDevice = listDevices.get(counter);
                arrayDevices.add(tempDevice.toJson()); 
            }

        }


        message.add("devices", arrayDevices);

        dElement.add("d", message);

        Log.i(TAG, "SendToServer: " + format.toString());



        try {
            client = new DeviceClient(prop);
            client.connect();

          //  System.out.println(dElement.toString());

            client.publishEvent("send", dElement); 


            client.disconnect();

        } catch(Exception e ){
            e.printStackTrace();
        }

    }
}
