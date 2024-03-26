package com.brandio.androidsample.utils;

import com.brandio.ads.consent.CompliantState;
import com.brandio.ads.placements.Placement;
import com.brandio.ads.request.AdRequest;
import com.brandio.ads.request.AppContentData;
import com.brandio.ads.request.Gender;
import com.brandio.ads.request.MediationPlatform;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class DIOAdrequestHelper {

    public static AdRequest createAndPopulateAdRequest(Placement placement) {
        AppContentData.ContentProducer contentProducer = new AppContentData.ContentProducer();
        contentProducer.setId("producer_666");
        contentProducer.setName("producer_name");
        contentProducer.setDomain("www.mmmm.com");
        contentProducer.setCat(new ArrayList<>(Arrays.asList("producer_cat_1", "producer_cat_66")));
        contentProducer.setExt(new JSONObject());

        AppContentData.Segment segment = new AppContentData.Segment(
                "seg_id",
                "seg_name",
                "seg_value",
                null);

        AppContentData.Data appContentData = new AppContentData.Data();
        appContentData.setId("data_id44");
        appContentData.setName("data_name_ttt");
        appContentData.setSegment(new ArrayList<>(Arrays.asList(segment, segment)));
        appContentData.setExt(new JSONObject());

        AdRequest adRequest = placement.newAdRequestBuilder()
                .setUserId("user_404")
                .setBcat(new ArrayList<>(Arrays.asList("IAB444-555", "IAB5")))
                .setBadv(new ArrayList<>(Arrays.asList("xxx.yyy", "zzz.com")))
                .setBapp(new ArrayList<>(Arrays.asList("ddd.net", "tpt.ua")))
                .setCat(new ArrayList<>(Arrays.asList("IAB1-1", "IAB666")))
                .setSectionCats(new ArrayList<>(Arrays.asList("IAB1-144", "IAB23")))
                .setPageCat(new ArrayList<>(Arrays.asList("IAB12", "IAB4-55")))
                .setAppVersion("1.1.10001")
                .setPrivacyPolicy(0)
                .setPaid(1)
                .setStoreUrl("http://g.play.com/id111555999")
                .setDomain("www.awesome.app.com")
                .setPublisherCats(new ArrayList<>(Arrays.asList("IAB0001", "IAB99-99")))
                .setCur("BTC")
                .setBuyerUId("id_12-45:55432")
                .setYob(2001)
                .setGender(Gender.O)
                .setKeywords("car, web, social")
                .setBidFloor(15)
                .setTmax(999)
                .setMediationPlatform(MediationPlatform.NONE)
                .setChildCompliant(CompliantState.YES)
                .setContentId("cont ID")
                .setContentEpisode(555)
                .setContentTitle("title  # 12")
                .setContentSeries("series 1-2-3")
                .setContentSeason("season_start")
                .setContentArtist("famous")
                .setContentGenre("techno")
                .setContentAlbum("single")
                .setContentIsrc("bla-bla")
                .setContentProducer(contentProducer)
                .setContentUrl("www.url.com")
                .setContentCat(new ArrayList<>(Arrays.asList("cat1111", "tcat656")))
                .setContentProdq(1)
                .setContentContext(1)
                .setContentRating("low")
                .setContentUserrating("high")
                .setContentQagmediarating(1)
                .setContentKeywords("apps, web")
                .setContentLivestream(1)
                .setContentSourcerelationship(0)
                .setContentLen(25)
                .setContentLanguage("UA")
                .setContentEmbeddable(1)
                .setContentData(new ArrayList<>(Arrays.asList(appContentData)))
                .build();

        return adRequest;
    }
}
