package fr.bouyguestelecom.bboxapi.bboxapi.callback;


public interface IBboxGetLogoApplication {

    void onResponse(String sessionId, String url);

    void onFailure(Exception e);
}
