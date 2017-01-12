package fr.bouyguestelecom.bboxapi.bboxapi.callback;


import fr.bouyguestelecom.bboxapi.bboxapi.model.ApplicationResource;

public interface IBboxApplication {

    void onNewApplication(ApplicationResource application);
}
