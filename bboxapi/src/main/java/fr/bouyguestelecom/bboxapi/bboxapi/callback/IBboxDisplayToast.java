package fr.bouyguestelecom.bboxapi.bboxapi.callback;

import okhttp3.Request;

public interface IBboxDisplayToast {

    void onResponse();

    void onFailure(Request request, int errorCode);
}
