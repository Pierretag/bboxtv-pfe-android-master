package fr.bouyguestelecom.bboxapi.bboxapi;

import android.os.Build;

/**
 * Created by dinh on 28/07/16.
 */

public class MyBbox {

    private final static String LOG_TAG = MyBbox.class.getName();
    public String ip;
    public String imei;

    public MyBbox(String ip) {
        this.ip = ip;
        this.imei = Build.SERIAL.toString();
    }

    public String getIp() {
        return ip;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}