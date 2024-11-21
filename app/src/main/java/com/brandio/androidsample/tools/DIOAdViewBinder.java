package com.brandio.androidsample.tools;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.brandio.ads.Controller;
import com.brandio.ads.ads.Ad;
import com.brandio.ads.containers.BannerContainer;
import com.brandio.ads.containers.InfeedContainer;
import com.brandio.ads.containers.InlineContainer;
import com.brandio.ads.containers.InterscrollerContainer;
import com.brandio.ads.containers.MediumRectangleContainer;
import com.brandio.ads.exceptions.DioSdkException;
import com.brandio.ads.placements.BannerPlacement;
import com.brandio.ads.placements.InfeedPlacement;
import com.brandio.ads.placements.InterscrollerPlacement;
import com.brandio.ads.placements.MediumRectanglePlacement;
import com.brandio.ads.placements.Placement;

public class DIOAdViewBinder {
    private DIOAdViewBinder() {
    }

    public static @Nullable View getAdView(@Nullable Ad ad, Context context) {
        if (ad == null) {
            return null;
        }
        ViewGroup holder = null;
        Placement placement;
        try {
            placement = Controller.getInstance().getPlacement(ad.getPlacementId());
        } catch (DioSdkException e) {
            return holder;
        }

        holder = InlineContainer.getAdView(context);
        switch (placement.getType()) {
            case BANNER: {
                BannerPlacement bannerPlacement = (BannerPlacement) placement;
                BannerContainer bannerContainer = bannerPlacement.getContainer(context, ad.getRequestId());
                bannerContainer.bindTo(holder);
                break;
            }
            case MEDIUMRECTANGLE: {
                MediumRectanglePlacement mediumRectanglePlacement = (MediumRectanglePlacement) placement;
                MediumRectangleContainer mediumRectangleContainer = mediumRectanglePlacement.getContainer(context, ad.getRequestId());
                mediumRectangleContainer.bindTo(holder);
                break;
            }
            case INFEED: {
                InfeedPlacement infeedPlacement = (InfeedPlacement) placement;
                InfeedContainer infeedContainer = infeedPlacement.getContainer(context, ad.getRequestId());
                infeedContainer.bindTo(holder);
                break;
            }
            case INTERSCROLLER: {
                InterscrollerPlacement interscrollerPlacement = (InterscrollerPlacement) placement;
                InterscrollerContainer interscrollerContainer = interscrollerPlacement.getContainer(context, ad.getRequestId());
                interscrollerContainer.bindTo(holder);
                break;
            }

        }
        return holder;
    }
}
