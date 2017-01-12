package fr.bouyguestelecom.bboxapi.bboxapi.callback;


import java.util.List;

import fr.bouyguestelecom.bboxapi.bboxapi.model.ChannelLight;
import okhttp3.Request;

public interface IBboxGetChannelListOnBox {

    void onResponse(List<ChannelLight> channels);

    void onFailure(Request request, int errorCode);

}
