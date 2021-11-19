package com.brandio.androidsample;

public class PlacementListItem {

    enum Type {
        INTERSTITIAL,
        INFEED,
        BANNER,
        MEDIUM_RECTABGLE,
        INTERSCROLLER,
        REWARDED_VIDEO,
        HEADLINEVIDEO;
    }

    String id;
    Type type;
    String name;

    public PlacementListItem(String id, Type type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }
}
