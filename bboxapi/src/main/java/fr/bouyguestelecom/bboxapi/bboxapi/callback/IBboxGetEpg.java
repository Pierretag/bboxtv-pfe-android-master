package fr.bouyguestelecom.bboxapi.bboxapi.callback;


import fr.bouyguestelecom.bboxapi.bboxapi.model.Epg;
import okhttp3.Request;

public interface IBboxGetEpg {

    void onResponse(Epg epg);

    void onFailure(Request request, int errorCode);

}
