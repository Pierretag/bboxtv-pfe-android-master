package fr.bouyguestelecom.bboxapi.bboxapi.callback;


import android.graphics.Bitmap;
import android.media.Image;

import java.util.List;

import fr.bouyguestelecom.bboxapi.bboxapi.model.Application;
import okhttp3.Request;

public interface IBboxGetApplicationIcon {

    void onResponse(Bitmap img);

    void onFailure(Request request, int errorCode);

}
