package com.brandio.androidsample;

import com.brandio.ads.ads.AdUnitType;

public class PlacementListItem {



    String id;
    AdUnitType type;
    String name;

    public PlacementListItem(String id, AdUnitType type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }
}
