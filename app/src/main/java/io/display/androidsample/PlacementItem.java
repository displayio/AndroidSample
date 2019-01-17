package io.display.androidsample;

public class PlacementItem {

    enum Type {
        INTERSTITIAL,
        BANNER
    }

    String id;
    Type type;

    public PlacementItem(String id, Type type) {
        this.id = id;
        this.type = type;
    }
}
