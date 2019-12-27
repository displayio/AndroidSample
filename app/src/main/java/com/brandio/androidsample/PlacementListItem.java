package com.brandio.androidsample;

public class PlacementListItem {

    enum Type {
        INTERSTITIAL,
        INFEED,
        BANNER,
        MEDIUM_RECTABGLE,
        FEED_INTERSTITIAL,
        REWARDED_VIDEO
    }

    String id;
    Type type;

    public PlacementListItem(String id, Type type) {
        this.id = id;
        this.type = type;
    }
}
