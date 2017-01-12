package fr.bouyguestelecom.bboxapi.bboxapi.callback;


import okhttp3.Request;

public interface IBboxRegisterApp {

    void onResponse(String appId);

    void onFailure(Request request, int errorCode);

}
