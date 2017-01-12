package fr.bouyguestelecom.bboxapi.bboxapi.callback;


import fr.bouyguestelecom.bboxapi.bboxapi.model.MediaResource;

public interface IBboxMedia {

    void onNewMedia(MediaResource media);
}
