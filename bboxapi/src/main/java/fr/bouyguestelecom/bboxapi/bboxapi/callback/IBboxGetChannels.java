package fr.bouyguestelecom.bboxapi.bboxapi.callback;


import java.util.List;

import fr.bouyguestelecom.bboxapi.bboxapi.model.Channel;
import okhttp3.Request;

public interface IBboxGetChannels {

    void onResponse(List<Channel> channels);

    void onFailure(Request request, int errorCode);

}
