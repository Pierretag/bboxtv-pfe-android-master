package fr.bouyguestelecom.bboxapi.bboxapi.callback;


import fr.bouyguestelecom.bboxapi.bboxapi.model.Channel;
import okhttp3.Request;

public interface IBboxGetChannel {

    void onResponse(Channel channel);

    void onFailure(Request request, int errorCode);
}
