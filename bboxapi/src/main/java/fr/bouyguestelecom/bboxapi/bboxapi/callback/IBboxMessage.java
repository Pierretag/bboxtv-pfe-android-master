package fr.bouyguestelecom.bboxapi.bboxapi.callback;


import fr.bouyguestelecom.bboxapi.bboxapi.model.MessageResource;

public interface IBboxMessage {

    void onNewMessage(MessageResource message);
}
