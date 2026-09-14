package com.brandio.androidsample.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.brandio.ads.Controller;
import com.brandio.ads.ads.Ad;
import com.brandio.ads.ads.AdUnitType;
import com.brandio.ads.ads.supers.NativeAdInterface;
import com.brandio.ads.components.NativeMediaView;
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
import com.brandio.androidsample.R;

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

        // native ad builds its own view: no InlineContainer / Placement container involved
        if (placement.getType() == AdUnitType.NATIVE) {
            return ad instanceof NativeAdInterface
                    ? buildNativeAdView((NativeAdInterface) ad, context)
                    : null;
        }

        holder = InlineContainer.getAdView(context);
        switch (placement.getType()) {
            case BANNER: {
                BannerPlacement bannerPlacement = (BannerPlacement) placement;
                BannerContainer bannerContainer = bannerPlacement.getContainer(ad.getRequestId());
                bannerContainer.bindTo(holder);
                break;
            }
            case MEDIUMRECTANGLE: {
                MediumRectanglePlacement mediumRectanglePlacement = (MediumRectanglePlacement) placement;
                MediumRectangleContainer mediumRectangleContainer = mediumRectanglePlacement.getContainer(ad.getRequestId());
                mediumRectangleContainer.bindTo(holder);
                break;
            }
            case INFEED: {
                InfeedPlacement infeedPlacement = (InfeedPlacement) placement;
                InfeedContainer infeedContainer = infeedPlacement.getContainer(ad.getRequestId());
                infeedContainer.bindTo(holder);
                break;
            }
            case INTERSCROLLER: {
                InterscrollerPlacement interscrollerPlacement = (InterscrollerPlacement) placement;
                InterscrollerContainer interscrollerContainer = interscrollerPlacement.getContainer(ad.getRequestId());
                interscrollerContainer.bindTo(holder);
                break;
            }

        }
        return holder;
    }

    private static View buildNativeAdView(NativeAdInterface nativeAd, Context context) {
        View root = LayoutInflater.from(context).inflate(R.layout.native_ad_item, null, false);
        root.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        NativeMediaView mediaView = root.findViewById(R.id.native_media);
        NativeMediaView iconView = root.findViewById(R.id.native_icon);
        TextView headline = root.findViewById(R.id.native_headline);
        TextView advertiser = root.findViewById(R.id.native_advertiser);
        TextView body = root.findViewById(R.id.native_body);
        TextView price = root.findViewById(R.id.native_price);
        TextView privacy = root.findViewById(R.id.native_privacy);
        Button cta = root.findViewById(R.id.native_cta);

        headline.setText(textOrEmpty(nativeAd.getHeadline()));
        advertiser.setText(nativeAd.getAdvertiser() != null
                ? nativeAd.getAdvertiser() + " \u00b7 Sponsored"
                : "Sponsored");
        body.setText(textOrEmpty(nativeAd.getBody()));
        price.setText(textOrEmpty(nativeAd.getPrice()));
        cta.setText(textOrEmpty(nativeAd.getCallToAction()));

        // privacy (AdChoices) icon is handled by the app, not by the SDK
        String privacyUrl = nativeAd.getPrivacy();
        boolean privacyClickable = isHttpUrl(privacyUrl);
        privacy.setVisibility(privacyClickable ? View.VISIBLE : View.GONE);
        privacy.setOnClickListener(privacyClickable ? v -> {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl)));
            } catch (Exception ignored) {
            }
        } : null);

        nativeAd.registerViewForInteraction(root, mediaView, iconView, headline, cta);
        return root;
    }

    private static String textOrEmpty(String s) {
        return s != null ? s : "";
    }

    private static boolean isHttpUrl(String url) {
        if (url == null) {
            return false;
        }
        String scheme = Uri.parse(url).getScheme();
        return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
    }
}
