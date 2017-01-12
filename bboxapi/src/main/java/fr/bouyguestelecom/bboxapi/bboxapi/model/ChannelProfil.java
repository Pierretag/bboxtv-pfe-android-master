package fr.bouyguestelecom.bboxapi.bboxapi.model;

public enum ChannelProfil {

    ALL("all"),
    TNT("tnt");

    private String value;

    ChannelProfil(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
