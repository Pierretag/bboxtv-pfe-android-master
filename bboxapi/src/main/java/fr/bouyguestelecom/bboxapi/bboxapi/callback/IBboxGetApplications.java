package fr.bouyguestelecom.bboxapi.bboxapi.callback;


import java.util.List;

import fr.bouyguestelecom.bboxapi.bboxapi.model.Application;
import okhttp3.Request;

public interface IBboxGetApplications {

    void onResponse(List<Application> applications);

    void onFailure(Request request, int errorCode);

}
