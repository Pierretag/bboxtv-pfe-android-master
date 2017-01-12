package fr.bouyguestelecom.bboxapi.bboxapi.callback;


import java.util.List;

import fr.bouyguestelecom.bboxapi.bboxapi.model.Epg;
import okhttp3.Request;

public interface IBboxGetSpideoTv {

    void onResponse(List<Epg> epgList);

    void onFailure(Request request, int errorCode);

}
