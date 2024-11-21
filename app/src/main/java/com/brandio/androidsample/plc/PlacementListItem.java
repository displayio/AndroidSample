package com.brandio.androidsample.plc;

import com.brandio.ads.ads.AdUnitType;

public class PlacementListItem {
    public String id;
    public AdUnitType type;
    public String name;

    public PlacementListItem(String id, AdUnitType type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }
}
