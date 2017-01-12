package fr.bouyguestelecom.bboxapi.bboxapi.callback;


import java.util.List;

import fr.bouyguestelecom.bboxapi.bboxapi.model.EpgSimple;
import okhttp3.Request;

public interface IBboxGetEpgSimple {

    void onResponse(List<EpgSimple> epgSimple);

    void onFailure(Request request, int errorCode);

}
