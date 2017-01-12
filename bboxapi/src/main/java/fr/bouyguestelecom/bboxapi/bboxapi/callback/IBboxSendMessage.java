package fr.bouyguestelecom.bboxapi.bboxapi.callback;

import okhttp3.Request;

public interface IBboxSendMessage {

    void onResponse();

    void onFailure(Request request, int errorCode);
}
