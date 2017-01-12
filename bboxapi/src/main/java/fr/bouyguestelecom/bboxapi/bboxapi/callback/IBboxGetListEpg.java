package fr.bouyguestelecom.bboxapi.bboxapi.callback;


import java.util.List;

import fr.bouyguestelecom.bboxapi.bboxapi.model.Epg;
import okhttp3.Request;

public interface IBboxGetListEpg {

    void onResponse(List<Epg> epgs);

    void onFailure(Request request, int errorCode);

}
