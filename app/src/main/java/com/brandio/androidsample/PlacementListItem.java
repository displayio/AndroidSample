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

    public PlacementListItem(String id, Type type) {
        this.id = id;
        this.type = type;
    }
}
