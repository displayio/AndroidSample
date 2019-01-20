package io.display.androidsample;

public class PlacementListItem {

    enum Type {
        INTERSTITIAL,
        BANNER
    }

    String id;
    Type type;

    public PlacementListItem(String id, Type type) {
        this.id = id;
        this.type = type;
    }
}
