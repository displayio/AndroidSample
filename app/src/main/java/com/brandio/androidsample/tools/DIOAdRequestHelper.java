package com.brandio.androidsample.tools;

import com.brandio.ads.placements.Placement;
import com.brandio.ads.request.AdRequest;
import com.brandio.ads.request.AppContentData;
import com.brandio.ads.request.CompliantState;
import com.brandio.ads.request.Gender;
import com.brandio.ads.request.MediationPlatform;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class DIOAdRequestHelper {

    /**
     * ============================================================================
     * EXAMPLE CODE - DO NOT COPY AS-IS!
     * ============================================================================
     * This method demonstrates how to create and populate AdRequest.
     * All values below are PLACEHOLDERS - replace them with your actual data.
     *
     * NOTE: All fields are OPTIONAL. Only set the values you actually have.
     * If you don't have data for a field - simply remove that line from the builder.
     * ============================================================================
     */
    public static AdRequest createAndPopulateAdRequest(Placement placement) {

        // ⚠️ REPLACE: Content Producer data with your actual values
        AppContentData.ContentProducer contentProducer = new AppContentData.ContentProducer();
        contentProducer.setId("<YOUR_PRODUCER_ID>");
        contentProducer.setName("<YOUR_PRODUCER_NAME>");
        contentProducer.setDomain("<YOUR_PRODUCER_DOMAIN>");
        contentProducer.setCat(new ArrayList<>(Arrays.asList("<YOUR_PRODUCER_CAT_1>", "<YOUR_PRODUCER_CAT_2>")));
        contentProducer.setExt(new JSONObject());

        // ⚠️ REPLACE: Segment data with your actual values
        AppContentData.Segment segment = new AppContentData.Segment(
                "<YOUR_SEGMENT_ID>",
                "<YOUR_SEGMENT_NAME>",
                "<YOUR_SEGMENT_VALUE>",
                null);

        // ⚠️ REPLACE: App Content data with your actual values
        AppContentData.Data appContentData = new AppContentData.Data();
        appContentData.setId("<YOUR_DATA_ID>");
        appContentData.setName("<YOUR_DATA_NAME>");
        appContentData.setSegment(new ArrayList<>(Arrays.asList(segment)));
        appContentData.setExt(new JSONObject());

        // ⚠️ REPLACE ALL VALUES BELOW with your actual data
        AdRequest adRequest = placement.newAdRequestBuilder()
                // User identification
                .setUserId("<YOUR_USER_ID>")
                .setBuyerUId("<YOUR_BUYER_USER_ID>")
                .setYob(0) // Year of birth, e.g. 1990
                .setGender(Gender.O) // Gender.M, Gender.F, or Gender.O
                .setKeywords("<YOUR_KEYWORDS>") // e.g. "sports, news, tech"

                // Block lists (categories/advertisers/apps to block)
                .setBcat(new ArrayList<>(Arrays.asList("<BLOCKED_CAT_1>", "<BLOCKED_CAT_2>"))) // IAB categories
                .setBadv(new ArrayList<>(Arrays.asList("<BLOCKED_DOMAIN_1>", "<BLOCKED_DOMAIN_2>")))
                .setBapp(new ArrayList<>(Arrays.asList("<BLOCKED_APP_1>", "<BLOCKED_APP_2>")))

                // Content categories
                .setCat(new ArrayList<>(Arrays.asList("<YOUR_CAT_1>", "<YOUR_CAT_2>"))) // IAB categories
                .setSectionCats(new ArrayList<>(Arrays.asList("<YOUR_SECTION_CAT_1>", "<YOUR_SECTION_CAT_2>")))
                .setPageCat(new ArrayList<>(Arrays.asList("<YOUR_PAGE_CAT_1>", "<YOUR_PAGE_CAT_2>")))
                .setPublisherCats(new ArrayList<>(Arrays.asList("<YOUR_PUB_CAT_1>", "<YOUR_PUB_CAT_2>")))

                // App information
                .setAppVersion("<YOUR_APP_VERSION>") // e.g. "1.0.0"
                .setPrivacyPolicy(0) // 1 if app has privacy policy, 0 otherwise
                .setPaid(0) // 1 if paid app, 0 if free
                .setStoreUrl("<YOUR_STORE_URL>") // Google Play or App Store URL
                .setDomain("<YOUR_APP_DOMAIN>")

                // Bid configuration
                .setBidFloor(0) // Minimum bid price
                .setTmax(0) // Maximum response time in ms

                // Compliance
                .setMediationPlatform(MediationPlatform.NONE)
                .setChildCompliant(CompliantState.YES) // COPPA compliance

                // Content metadata
                .setContentId("<YOUR_CONTENT_ID>")
                .setContentEpisode(0)
                .setContentTitle("<YOUR_CONTENT_TITLE>")
                .setContentSeries("<YOUR_CONTENT_SERIES>")
                .setContentSeason("<YOUR_CONTENT_SEASON>")
                .setContentArtist("<YOUR_CONTENT_ARTIST>")
                .setContentGenre("<YOUR_CONTENT_GENRE>")
                .setContentAlbum("<YOUR_CONTENT_ALBUM>")
                .setContentIsrc("<YOUR_CONTENT_ISRC>")
                .setContentProducer(contentProducer)
                .setContentUrl("<YOUR_CONTENT_URL>")
                .setContentCat(new ArrayList<>(Arrays.asList("<YOUR_CONTENT_CAT_1>", "<YOUR_CONTENT_CAT_2>")))
                .setContentProdq(1) // Production quality: 1=Professional, 2=Prosumer, 3=UGC
                .setContentContext(1) // Content context type
                .setContentRating("<YOUR_CONTENT_RATING>")
                .setContentUserrating("<YOUR_USER_RATING>")
                .setContentQagmediarating(1) // QAG media rating
                .setContentKeywords("<YOUR_CONTENT_KEYWORDS>")
                .setContentLivestream(0) // 1 if livestream, 0 otherwise
                .setContentSourcerelationship(0) // 0=indirect, 1=direct
                .setContentLen(0) // Content length in seconds
                .setContentLanguage("<YOUR_CONTENT_LANGUAGE>") // ISO 639-1 code, e.g. "en"
                .setContentEmbeddable(1) // 1 if embeddable, 0 otherwise
                .setContentData(new ArrayList<>(Arrays.asList(appContentData)))
                .build();

        return adRequest;
    }
}
