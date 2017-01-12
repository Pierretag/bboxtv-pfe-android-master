package fr.lab.bbox.bboxapirunner;

import android.support.v7.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ibm.iotf.client.device.DeviceClient;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;


/**
 * Created by Victor on 09/01/2017.
 */
public class Client extends AppCompatActivity {

    Properties prop;
    DeviceClient client;

    public Client(){
        prop = new Properties();
        client = null;
    }

    public void SendToServer(ArrayList<BluetoothObject> listDevices){

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
        bbox.addProperty("serialNumber", "unknown");
        bbox.addProperty("MAC", "unknown");
        bbox.addProperty("IP", "176.135.254.139");
        message.add("bbox", bbox);


        //Timestemp
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        message.addProperty("timestamp", timestamp.toString());
        message.addProperty("startTimestamp", timestamp.toString());

        //TV
        JsonObject tv = new JsonObject();
        tv.addProperty("positionId", 1);
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




        try {
            client = new DeviceClient(prop);
            client.connect();
            client.publishEvent("send", dElement); 


            client.disconnect();

        } catch(Exception e ){
            e.printStackTrace();
        }

    }
}
