package com.brandio.androidsample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.brandio.ads.AdProvider;
import com.brandio.ads.AdRequest;
import com.brandio.ads.Controller;
import com.brandio.ads.InterscrollerPlacement;
import com.brandio.ads.Placement;
import com.brandio.ads.ads.Ad;
import com.brandio.ads.exceptions.DIOError;
import com.brandio.ads.exceptions.DioSdkException;
import com.brandio.ads.listeners.AdEventListener;
import com.brandio.ads.listeners.AdLoadListener;
import com.brandio.ads.listeners.AdRequestListener;


public class LoadInfeedActivity extends AppCompatActivity {

    public final static int AD_POSITION = 12;
    private static String TAG = "LoadInfeedActivity";

    private Button loadButton;
    private Button showButton;

    private String placementId;
    private String requestId;
    private String adUnitType;
    private boolean isViewPager;
    private boolean isORTB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infeed);

        placementId = getIntent().getStringExtra(MainActivity.PLACEMENT_ID);
        adUnitType = getIntent().getStringExtra(MainActivity.AD_UNIT_TYPE);
        isViewPager = getIntent().getStringExtra(MainActivity.NAME).contains("ViewPager");
        isORTB = getIntent().getStringExtra(MainActivity.NAME).contains("ORTB");

        loadButton = findViewById(R.id.button_load_infeed);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isORTB) {
                    loadAdFromORTB();
                } else {
                    loadAd();
                }
            }
        });

        showButton = findViewById(R.id.button_show_infeed);
        showButton.setEnabled(false);

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showButton.setEnabled(false);
                Intent intent = isViewPager ?
                        new Intent(LoadInfeedActivity.this, ViewPagerActivity.class) :
                        new Intent(LoadInfeedActivity.this, ShowListWithInfeedActivity.class);
                intent.putExtra(MainActivity.PLACEMENT_ID, placementId);
                intent.putExtra(MainActivity.REQUEST_ID, requestId);
                intent.putExtra(MainActivity.AD_UNIT_TYPE, adUnitType);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadButton.setEnabled(true);
    }

    private void loadAd() {
        loadButton.setEnabled(false);

        Placement placement;
        try {
            placement = Controller.getInstance().getPlacement(placementId);
        } catch (DioSdkException e) {
            Log.e(TAG, e.getLocalizedMessage());
            return;
        }

        // customise IS here
        if (isViewPager) {
            placement.setShowSoundControl(false);  //true by default
            placement.setDefaultMute(true);  //true by default
            ((InterscrollerPlacement) placement).setReveal(false);   //true by default
            ((InterscrollerPlacement) placement).setShowHeader(false);   //true by default
        }

        final AdRequest adRequest = placement.newAdRequest();
        adRequest.setAdRequestListener(new AdRequestListener() {
            @Override
            public void onAdReceived(AdProvider adProvider) {

                adProvider.setAdLoadListener(new AdLoadListener() {
                    @Override
                    public void onLoaded(Ad ad) {
                        requestId = adRequest.getId();
                        showButton.setEnabled(true);

                        ad.setEventListener(new AdEventListener() {
                            @Override
                            public void onShown(Ad ad) {
                                Log.e(TAG, "onShown");
                            }

                            @Override
                            public void onFailedToShow(Ad ad) {
                                Log.e(TAG, "onFailedToShow");
                            }

                            @Override
                            public void onClicked(Ad ad) {
                                Log.e(TAG, "onClicked");
                            }

                            @Override
                            public void onClosed(Ad ad) {
                                Log.e(TAG, "onClosed");
                            }

                            @Override
                            public void onAdCompleted(Ad ad) {
                                Log.e(TAG, "onAdCompleted");
                            }
                        });
                    }

                    @Override
                    public void onFailedToLoad(DIOError error) {
                        Toast.makeText(LoadInfeedActivity.this, "Ad for placement " + placementId + " failed to load", Toast.LENGTH_LONG).show();
                    }
                });

                try {
                    adProvider.loadAd();
                } catch (DioSdkException e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }

            @Override
            public void onNoAds(DIOError error) {
                Toast.makeText(LoadInfeedActivity.this, "No Ads placement " + placementId, Toast.LENGTH_LONG).show();
            }
        });

        adRequest.requestAd();
    }

    private void loadAdFromORTB() {
        loadButton.setEnabled(false);

        InterscrollerPlacement placement;
        try {
            placement = (InterscrollerPlacement) Controller.getInstance().getPlacement(placementId);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
            return;
        }

        // customise IS here
        if (isViewPager) {
            placement.setShowSoundControl(false);  //true by default
            placement.setDefaultMute(true);  //true by default
            placement.setReveal(false);   //true by default
            placement.setShowHeader(false);   //true by default
        }

        placement.loadInterscrollerFromORTB(getSampleOrtbResponse1(), new Placement.OnORTBLoadListener() {
            @Override
            public void onLoaded(Ad ad) {
                requestId = ad.getRequestId();
                Log.e(TAG, "RequestId = " + requestId);
                showButton.setEnabled(true);
            }

            @Override
            public void onFailToLoad(DIOError e) {
                Log.e(TAG, "Failed to load ad");
            }
        });
    }

    //just a mock bid response to demonstrate how to render oRTB ads on DIO SDK
    private String getSampleOrtbResponse1() {
        return  "{\"id\":\"93509158363773756147606\",\"seatbid\":[{\"bid\":[{\"id\":\"12345\",\"impid\":\"6447a3517100c\",\"price\":3.912,\"nurl\":\"http:\\/\\/example.com\\/winnoticeurl&price=${AUCTION_PRICE}&id=${AUCTION_ID}&impid=${AUCTION_IMP_ID}&bidid=${AUCTION_BID_ID}&seatid=${AUCTION_SEAT_ID}&curr=${AUCTION_CURRENCY}\",\"adm\":\"<?xml version=\\\"1.0\\\"?>\\n<VAST version=\\\"2.0\\\">\\n    <Ad id=\\\"188ni7\\\">\\n        <InLine>\\n            <AdSystem>Innovid Ads<\\/AdSystem>\\n            <AdTitle><![CDATA[MUL_MY21_PCC_Show_LVStory_Olympic_Non-New_ENG_30_16x9Video_TYSB0890000H_unslated]]><\\/AdTitle>\\n            <Error><![CDATA[https:\\/\\/s.innovid.com\\/1x1.gif?project_hash=1h565h&client_id=81&video_id=732781&channel_id=2388992&publisher_id=2891&placement_tag_id=0&project_state=2&r=1682416465454&placement_hash=188ni7&device_id=0539b6f8-71c9-4638-8344-e2e8db128b77&action=error&event_id=vast_error&event_value=[ERRORCODE]&ivc_exdata=dipn%3Ddeviceid%26deviceid%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_deviceid_raw%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_appkeylm%3D3e4dcb4e6c%26dipn%3Ddeviceid%26deviceid%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_deviceid_raw%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26iv_geo_dma%3D%26iv_geo_country%3DUS%26iv_geo_city%3D%26iv_geo_state%3D%26iv_geo_zip%3D%26iv_geo_lat%3D37.751%26iv_geo_lon%3D-97.822]]><\\/Error>\\n            <Impression><![CDATA[https:\\/\\/s.innovid.com\\/1x1.gif?project_hash=1h565h&client_id=81&video_id=732781&channel_id=2388992&publisher_id=2891&placement_tag_id=0&project_state=2&r=1682416465454&placement_hash=188ni7&device_id=0539b6f8-71c9-4638-8344-e2e8db128b77&action=init&ivc_exdata=dipn%3Ddeviceid%26deviceid%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_deviceid_raw%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_appkeylm%3D3e4dcb4e6c%26dipn%3Ddeviceid%26deviceid%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_deviceid_raw%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26iv_geo_dma%3D%26iv_geo_country%3DUS%26iv_geo_city%3D%26iv_geo_state%3D%26iv_geo_zip%3D%26iv_geo_lat%3D37.751%26iv_geo_lon%3D-97.822]]><\\/Impression>\\n            <Impression><![CDATA[https:\\/\\/dts.innovid.com\\/placement\\/188ni7\\/uuid?cb=9cd855a5-aabe-34f0-975b-eaf248bacaa2]]><\\/Impression>\\n            <Creatives>\\n                <Creative id=\\\"1h2rqa\\\">\\n                    <Linear>\\n                        <Duration>00:00:30<\\/Duration>\\n                        <TrackingEvents>\\n                            <Tracking event=\\\"start\\\"><![CDATA[https:\\/\\/s.innovid.com\\/1x1.gif?project_hash=1h565h&client_id=81&video_id=732781&channel_id=2388992&publisher_id=2891&placement_tag_id=0&project_state=2&r=1682416465454&placement_hash=188ni7&device_id=0539b6f8-71c9-4638-8344-e2e8db128b77&action=play&ivc_exdata=dipn%3Ddeviceid%26deviceid%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_deviceid_raw%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_appkeylm%3D3e4dcb4e6c%26dipn%3Ddeviceid%26deviceid%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_deviceid_raw%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26iv_geo_dma%3D%26iv_geo_country%3DUS%26iv_geo_city%3D%26iv_geo_state%3D%26iv_geo_zip%3D%26iv_geo_lat%3D37.751%26iv_geo_lon%3D-97.822]]><\\/Tracking>\\n<Tracking event=\\\"start\\\"><![CDATA[https:\\/\\/secure.insightexpressai.com\\/adServer\\/adServerESI.aspx?script=false&bannerID=8780093&siteID=2891&creativeID=732781&placementID=2401064&DID=0539b6f8-71c9-4638-8344-e2e8db128b77&rnd=1682416465454&redir=https:\\/\\/secure.insightexpressai.com\\/adserver\\/1pixel.gif]]><\\/Tracking>\\n<Tracking event=\\\"start\\\"><![CDATA[https:\\/\\/ad.doubleclick.net\\/ddm\\/trackimp\\/N2724.1938701LOOPME\\/B25913659.307026229;dc_trk_aid=499851202;dc_trk_cid=152903406;ord=1682416465454;dc_lat=;dc_rdid=;tag_for_child_directed_treatment=;tfua=;ltd=?]]><\\/Tracking>\\n<Tracking event=\\\"start\\\"><![CDATA[https:\\/\\/secure-gl.imrworldwide.com\\/cgi-bin\\/m?ca=nlsn284922&cr=732781&ce=2891&pc=2401064&ci=nlsnci2103&am=52&at=view&rt=banner&st=image&r=1682416465454]]><\\/Tracking>\\n<Tracking event=\\\"start\\\"><![CDATA[https:\\/\\/unified.adsafeprotected.com\\/vevent\\/impression\\/741998\\/55507410?ias_xadur=00:00:30:080&ias_xmtp=v&omidPartner=[OMIDPARTNER]&ias_xappb=[APPBUNDLE]&xsId=c7af8c11bf446f6476b58816007acab4e5daf9a5]]><\\/Tracking>\\n<Tracking event=\\\"firstQuartile\\\"><![CDATA[https:\\/\\/s.innovid.com\\/1x1.gif?project_hash=1h565h&client_id=81&video_id=732781&channel_id=2388992&publisher_id=2891&placement_tag_id=0&project_state=2&r=1682416465454&placement_hash=188ni7&device_id=0539b6f8-71c9-4638-8344-e2e8db128b77&action=vpoint&event_id=percent&event_value=25&ivc_exdata=dipn%3Ddeviceid%26deviceid%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_deviceid_raw%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_appkeylm%3D3e4dcb4e6c%26dipn%3Ddeviceid%26deviceid%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_deviceid_raw%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26iv_geo_dma%3D%26iv_geo_country%3DUS%26iv_geo_city%3D%26iv_geo_state%3D%26iv_geo_zip%3D%26iv_geo_lat%3D37.751%26iv_geo_lon%3D-97.822]]><\\/Tracking>\\n<Tracking event=\\\"firstQuartile\\\"><![CDATA[https:\\/\\/unified.adsafeprotected.com\\/vevent\\/firstQuartile\\/741998\\/55507410?xsId=c7af8c11bf446f6476b58816007acab4e5daf9a5]]><\\/Tracking>\\n<Tracking event=\\\"midpoint\\\"><![CDATA[https:\\/\\/s.innovid.com\\/1x1.gif?project_hash=1h565h&client_id=81&video_id=732781&channel_id=2388992&publisher_id=2891&placement_tag_id=0&project_state=2&r=1682416465454&placement_hash=188ni7&device_id=0539b6f8-71c9-4638-8344-e2e8db128b77&action=vpoint&event_id=percent&event_value=50&ivc_exdata=dipn%3Ddeviceid%26deviceid%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_deviceid_raw%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_appkeylm%3D3e4dcb4e6c%26dipn%3Ddeviceid%26deviceid%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_deviceid_raw%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26iv_geo_dma%3D%26iv_geo_country%3DUS%26iv_geo_city%3D%26iv_geo_state%3D%26iv_geo_zip%3D%26iv_geo_lat%3D37.751%26iv_geo_lon%3D-97.822]]><\\/Tracking>\\n<Tracking event=\\\"midpoint\\\"><![CDATA[https:\\/\\/unified.adsafeprotected.com\\/vevent\\/midpoint\\/741998\\/55507410?xsId=c7af8c11bf446f6476b58816007acab4e5daf9a5]]><\\/Tracking>\\n<Tracking event=\\\"thirdQuartile\\\"><![CDATA[https:\\/\\/s.innovid.com\\/1x1.gif?project_hash=1h565h&client_id=81&video_id=732781&channel_id=2388992&publisher_id=2891&placement_tag_id=0&project_state=2&r=1682416465454&placement_hash=188ni7&device_id=0539b6f8-71c9-4638-8344-e2e8db128b77&action=vpoint&event_id=percent&event_value=75&ivc_exdata=dipn%3Ddeviceid%26deviceid%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_deviceid_raw%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_appkeylm%3D3e4dcb4e6c%26dipn%3Ddeviceid%26deviceid%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_deviceid_raw%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26iv_geo_dma%3D%26iv_geo_country%3DUS%26iv_geo_city%3D%26iv_geo_state%3D%26iv_geo_zip%3D%26iv_geo_lat%3D37.751%26iv_geo_lon%3D-97.822]]><\\/Tracking>\\n<Tracking event=\\\"thirdQuartile\\\"><![CDATA[https:\\/\\/unified.adsafeprotected.com\\/vevent\\/thirdQuartile\\/741998\\/55507410?xsId=c7af8c11bf446f6476b58816007acab4e5daf9a5]]><\\/Tracking>\\n<Tracking event=\\\"complete\\\"><![CDATA[https:\\/\\/s.innovid.com\\/1x1.gif?project_hash=1h565h&client_id=81&video_id=732781&channel_id=2388992&publisher_id=2891&placement_tag_id=0&project_state=2&r=1682416465454&placement_hash=188ni7&device_id=0539b6f8-71c9-4638-8344-e2e8db128b77&action=vpoint&event_id=percent&event_value=100&ivc_exdata=dipn%3Ddeviceid%26deviceid%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_deviceid_raw%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_appkeylm%3D3e4dcb4e6c%26dipn%3Ddeviceid%26deviceid%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26ivc_deviceid_raw%3D0539b6f8-71c9-4638-8344-e2e8db128b77%26iv_geo_dma%3D%26iv_geo_country%3DUS%26iv_geo_city%3D%26iv_geo_state%3D%26iv_geo_zip%3D%26iv_geo_lat%3D37.751%26iv_geo_lon%3D-97.822]]><\\/Tracking>\\n<Tracking event=\\\"complete\\\"><![CDATA[https:\\/\\/unified.adsafeprotected.com\\/vevent\\/complete\\/741998\\/55507410?xsId=c7af8c11bf446f6476b58816007acab4e5daf9a5]]><\\/Tracking>\\n\\n                        <\\/TrackingEvents>\\n                        <VideoClicks>\\n                            <ClickThrough><![CDATA[https:\\/\\/dts.innovid.com\\/clktru\\/action\\/vclk?project_hash=1h565h&client_id=81&video_id=732781&channel_id=2388992&publisher_id=2891&placement_tag_id=0&project_state=2&r=1682416465454&placement_hash=188ni7&device_id=0539b6f8-71c9-4638-8344-e2e8db128b77&action=clktru&click=https%3A%2F%2Fad.doubleclick.net%2Fddm%2Ftrackclk%2FN2724.1938701LOOPME%2FB25913659.307026229%3Bdc_trk_aid%3D499851202%3Bdc_trk_cid%3D152903406%3Bdc_lat%3D%3Bdc_rdid%3D%3Btag_for_child_directed_treatment%3D%3Btfua%3D%3Bltd%3D]]><\\/ClickThrough>\\n                            \\n                        <ClickTracking><![CDATA[https:\\/\\/appsrv.display.io\\/click?msessId=93509158363773756147606&tls=21296502_7_0&p=8148&app=8970]]><\\/ClickTracking><\\/VideoClicks>\\n                        <MediaFiles>\\n                            <MediaFile delivery=\\\"progressive\\\" width=\\\"16\\\" height=\\\"9\\\" type=\\\"video\\/mp4\\\" bitrate=\\\"800\\\"><![CDATA[https:\\/\\/s-static.innovid.com\\/media\\/encoded\\/06_21\\/435607\\/2_source_122866_537977.mp4]]><\\/MediaFile><MediaFile delivery=\\\"progressive\\\" width=\\\"16\\\" height=\\\"9\\\" type=\\\"video\\/mp4\\\" bitrate=\\\"600\\\"><![CDATA[https:\\/\\/s-static.innovid.com\\/media\\/encoded\\/06_21\\/435607\\/23_source_122866_537977.mp4]]><\\/MediaFile>\\n                        <\\/MediaFiles>\\n                        \\n                    <\\/Linear>\\n                    \\n                <\\/Creative>\\n                \\n            <\\/Creatives>\\n            <Extensions>\\n<Extension>\\n      <AVID>\\n         <AdVerifications>\\n            <Verification>\\n               <JavaScriptResource>\\n               <![CDATA[https:\\/\\/fw.adsafeprotected.com\\/rjss\\/st\\/741998\\/55507410\\/skeleton.js?ias_xmtp=v&omidPartner=[OMIDPARTNER]&ias_xappb=[APPBUNDLE]&xsId=c7af8c11bf446f6476b58816007acab4e5daf9a5]]>\\n               <\\/JavaScriptResource>\\n            <\\/Verification>\\n         <\\/AdVerifications>\\n      <\\/AVID>\\n   <\\/Extension>\\n   <Extension type=\\\"AdVerifications\\\">\\n      <AdVerifications>\\n         <Verification vendor=\\\"integralads.com\\\">\\n            <JavaScriptResource apiFramework=\\\"omid\\\" browserOptional=\\\"true\\\">\\n            <![CDATA[https:\\/\\/fw.adsafeprotected.com\\/rjss\\/st\\/741998\\/55507410\\/skeleton.js?ias_xmtp=v&omidPartner=[OMIDPARTNER]&ias_xappb=[APPBUNDLE]&xsId=c7af8c11bf446f6476b58816007acab4e5daf9a5]]>\\n            <\\/JavaScriptResource>\\n         <\\/Verification>\\n      <\\/AdVerifications>\\n   <\\/Extension>\\n<\\/Extensions>\\n        <Impression><![CDATA[https:\\/\\/appsrv.display.io\\/imp?msessId=93509158363773756147606&tls=21296502_7_0&p=8148&app=8970]]><\\/Impression><Impression><![CDATA[http:\\/\\/example.com\\/winnoticeurl&price=4.89&id=6447a35152c0f&impid=1&bidid=12345&seatid=&curr=USD]]><\\/Impression><\\/InLine>\\n    <\\/Ad>\\n<\\/VAST>\\n\",\"ext\":{\"dio\":{\"impMethod\":2,\"cptr\":0,\"key\":\"m2ba8dMhH1hMfRFtysjbAjbWeDWw5+WiRn0U0M37d7s=\",\"advertiserName\":\"\"}}}]}]}";

    }
    private String getSampleOrtbResponse() {
        //Randomise id. Having unique id is essential to avoid collisions between different ads
        String rnd = String.valueOf((int) (Math.random() * 10000));

        return "{\n" +
                "    \"id\": \"7d79e65e-26c5-4153-93e1-cb59faaecefc" + rnd + "\",\n" +
                "    \"seatbid\": [\n" +
                "        {\n" +
                "            \"bid\": [\n" +
                "                {\n" +
                "                    \"id\": \"12345\",\n" +
                "                    \"impid\": \"6452af6d894ce\",\n" +
                "                    \"price\": 4.034800000000001,\n" +
                "                    \"nurl\": \"http:\\/\\/example.com\\/winnoticeurl&price=${AUCTION_PRICE}&id=${AUCTION_ID}&impid=${AUCTION_IMP_ID}&bidid=${AUCTION_BID_ID}&seatid=${AUCTION_SEAT_ID}&curr=${AUCTION_CURRENCY}\",\n" +
                "                    \"adm\": \"<?xml version=\\\"1.0\\\"?>\\n<VAST version=\\\"3.0\\\"> <Ad id=\\\"2799063\\\"> <InLine> <AdSystem version=\\\"1.0\\\"> <![CDATA[AdServer]]> <\\/AdSystem> <AdTitle> <![CDATA[Video Ad]]> <\\/AdTitle> <Impression> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/hyperad\\/rtb\\/111285\\/impression\\/17-H4sIAAAAAAAAAI2SPWgUQRTHN4_zOI8zATmChZhoo4WzzOfOzhXmEnKYYBElIYXNMft1Wdi9O2_34ndhoY1oFWzSWFlYpE5n0EorLcROSWEVED8glYhzq0IECwemeDP_997__Xiw8WAKLMuCY3E3DwdZHuexTkz8fBtgfpihUGc5ojbBOM1aNiZYegSrSPue7weCQFMnnd4gztdS1NFp3O0gP9fI72sUd7NcJ8l_KL5_-PGiBpXUy6_Ck0ebY7A3cXhiov5lBp6OrVN00NnBvFvUllK50mWyhcTZfxc3IiKFcojk0qHccbnRwrdnH-8debN_bjS6OZ9nAIhlLgCUoLz17i1YpfkW1If9JI7y9sjk7o361vndrzNQWTdIoETUKBMmiesaOtJ2bGxBdbbfT0KoxRfXet0QynEvgxrhtpFYUM9SPcj7xQ_AGT2StuMibq-HA8LbLhwP4qyf6Ot23Jtenr8AVWYLW4H126hpuBrRBmMixCQizKc8lCIwCFQoqKe0wkQEfhAGKuC-KPLgKMOCccYxh8f7rypQXZydM-6h4jLuGif1lWHqJYN2vLQMTamUI33CsMscrCPHdR1fR77gLom0CgIYp5QpIqkZsts2Mzx8uXH_tOljCn3aPASvN04BcBPuvL9ThR2zRX8Q1wgh1BVGN04dilaXCEzO6WB6oZd6w0Hn76AMmGLKEOaIqBXiNDBrYGorLi4vaBVxabCjIPQ9REgYIFczD3EV4cDhkgdEwdTdvUvbJxabwIpzsgmlhRaUDY6qQ5jAVoEUKqMuVuG6oDs9WsJGHpqlv4I8s0Ojx9LN279IXvsJ3XT5Zi8DAAA\\/1\\/a9f47188-decb-11ed-8a3b-49f0d6474d19\\/0.200010\\/notify?cid=279848]]> <\\/Impression> <Creatives> <Creative id=\\\"1\\\" sequence=\\\"1\\\"> <Linear skipoffset=\\\"00:00:05\\\"> <Duration>00:00:27<\\/Duration> <TrackingEvents> <Tracking event=\\\"start\\\"> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/hyperad\\/tracking\\/action\\/start?click_id=66936789946c8b4ced0ac07e9330d097e59bf0a7c7db4afa766f9075e45445473e7582a2411a73b12d36a7e675db25ea8c32f8d1ea636d91814db548dff280610e966f156a78c35d767b203d6719f30975be78cd33486a3222e6ece5459e2527fe5e420e906930e79e037845d55f9833c3f4d554cd60422399044fa9336cfa4fc994230edfc82cc746d9a29a12fb0c4829341c45aeda13c2d3d05f513dc64afd3b9fcd667c7f6eea3844a6a687c65b72a7affd5e0b451e14fdcaa69141efcf0b&crid=2799063]]> <\\/Tracking> <Tracking event=\\\"firstQuartile\\\"> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/hyperad\\/tracking\\/action\\/firstQuartile?click_id=66936789946c8b4ced0ac07e9330d097e59bf0a7c7db4afa766f9075e45445473e7582a2411a73b12d36a7e675db25ea8c32f8d1ea636d91814db548dff280610e966f156a78c35d767b203d6719f30975be78cd33486a3222e6ece5459e2527fe5e420e906930e79e037845d55f9833c3f4d554cd60422399044fa9336cfa4fc994230edfc82cc746d9a29a12fb0c4829341c45aeda13c2d3d05f513dc64afd3b9fcd667c7f6eea3844a6a687c65b72a7affd5e0b451e14fdcaa69141efcf0b&crid=2799063]]> <\\/Tracking> <Tracking event=\\\"midpoint\\\"> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/hyperad\\/tracking\\/action\\/midpoint?click_id=66936789946c8b4ced0ac07e9330d097e59bf0a7c7db4afa766f9075e45445473e7582a2411a73b12d36a7e675db25ea8c32f8d1ea636d91814db548dff280610e966f156a78c35d767b203d6719f30975be78cd33486a3222e6ece5459e2527fe5e420e906930e79e037845d55f9833c3f4d554cd60422399044fa9336cfa4fc994230edfc82cc746d9a29a12fb0c4829341c45aeda13c2d3d05f513dc64afd3b9fcd667c7f6eea3844a6a687c65b72a7affd5e0b451e14fdcaa69141efcf0b&crid=2799063]]> <\\/Tracking> <Tracking event=\\\"thirdQuartile\\\"> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/hyperad\\/tracking\\/action\\/thirdQuartile?click_id=66936789946c8b4ced0ac07e9330d097e59bf0a7c7db4afa766f9075e45445473e7582a2411a73b12d36a7e675db25ea8c32f8d1ea636d91814db548dff280610e966f156a78c35d767b203d6719f30975be78cd33486a3222e6ece5459e2527fe5e420e906930e79e037845d55f9833c3f4d554cd60422399044fa9336cfa4fc994230edfc82cc746d9a29a12fb0c4829341c45aeda13c2d3d05f513dc64afd3b9fcd667c7f6eea3844a6a687c65b72a7affd5e0b451e14fdcaa69141efcf0b&crid=2799063]]> <\\/Tracking> <Tracking event=\\\"complete\\\"> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/hyperad\\/tracking\\/action\\/complete?click_id=66936789946c8b4ced0ac07e9330d097e59bf0a7c7db4afa766f9075e45445473e7582a2411a73b12d36a7e675db25ea8c32f8d1ea636d91814db548dff280610e966f156a78c35d767b203d6719f30975be78cd33486a3222e6ece5459e2527fe5e420e906930e79e037845d55f9833c3f4d554cd60422399044fa9336cfa4fc994230edfc82cc746d9a29a12fb0c4829341c45aeda13c2d3d05f513dc64afd3b9fcd667c7f6eea3844a6a687c65b72a7affd5e0b451e14fdcaa69141efcf0b&crid=2799063]]> <\\/Tracking> <Tracking event=\\\"skip\\\"> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/hyperad\\/tracking\\/action\\/skip?click_id=66936789946c8b4ced0ac07e9330d097e59bf0a7c7db4afa766f9075e45445473e7582a2411a73b12d36a7e675db25ea8c32f8d1ea636d91814db548dff280610e966f156a78c35d767b203d6719f30975be78cd33486a3222e6ece5459e2527fe5e420e906930e79e037845d55f9833c3f4d554cd60422399044fa9336cfa4fc994230edfc82cc746d9a29a12fb0c4829341c45aeda13c2d3d05f513dc64afd3b9fcd667c7f6eea3844a6a687c65b72a7affd5e0b451e14fdcaa69141efcf0b&crid=2799063]]> <\\/Tracking> <\\/TrackingEvents> <VideoClicks> <ClickThrough> <![CDATA[itms-apps:\\/\\/itunes.apple.com\\/app\\/id1371565796]]> <\\/ClickThrough> <ClickTracking> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/ct?q=8153aa504071ebc90202467ede0332b6411cf526f180700940395cb4b4a02654f444e414b63c28c5d2200a8f4d8db0178cc5285a6a5412ac38fa95e4652d6a3c7bd742c077d3a99b9e63234b490e7f094c3772afdb7ba142855c4382d08d580a798e24ba3238494a712648bfad4e68b397771d59ba57a527c31ba03ab873eeaff0eb6948471e11403f33628235c78d4337f6686c8043ee190354e4b10b869af043815e27abfe23469cad4e78ca4d631cce704cd6d260aad6b7e7fea0bc44505d04c28a9303c08416b5828ccc4e7cff0ab452e579574afd3716d9ae19d32222849ea97356f336ffa114ccb294f8986a629bf09dc353e4fbbe07f8d1731ace1251ee48aed309dcc0c379de12b9bcaffec4396dda11df7df2a1b09b682a3b745d4af0443bc6ccc4be9195e9c0d0acc9eb6869bcd70cfce6f5d361077e129622b3acd7cdb62334d591fe40bcb8369a1ba9396415b57cb71526e873fb2a0b57e575ff3306b8e2649965cd7f62b7274b555c1d8951d9ecc45e21c51430df793b4450bb099e48a7c82613b390e5a6d77a2e8f17197b578e3ae924796679437956269ec9971e17555efcc8bde2c7b92acc521ff64593b07c5b1b5650e9cb3fe5fce12d47ea65a31bded087076f05ccff5d791dbf79bff7d93c9acb951e68c3c7b6e47448503afb095a3d4f847039650b15c7765869a0d2f2557d6088cf4573f4b9e0e27dd9ad7fb0a8dc79c6c215c73bfef2b5a40f926a2a0e24c1cf611482c4fdcf54a64b809029044c2a05aea1abec0e7fc95e8fb638f6db30472cfae971b6ffe2bb0b958646ee8c8981b26bef2aa061f0d6f77ba359c772e2d208f208b05411b7bb647c3790ac42a0a81b82fed50150f0699b188059288b6364a546fa89e083f53ab22c6f86ece3473ed9d4de0d74b2787c41c9a613aa0af6fe937eab764ca7776547470434c717318a2062a782fcef76c8b92fd43a78296baee1e0cc922f28ea7b95496d4b5128a0a1632e908ac5456f38eb30919918cfe89cdea6ac078c0a85b5f6ad05070da235fb5b63fdc614982b9cd5]]> <\\/ClickTracking> <ClickTracking><![CDATA[https:\\/\\/appsrv.display.io\\/click?msessId=7d79e65e-26c5-4153-93e1-cb59faaecefc&tls=21305666_47_2&p=8159&app=8347]]><\\/ClickTracking><\\/VideoClicks> <MediaFiles> <MediaFile delivery=\\\"progressive\\\" bitrate=\\\"496\\\" type=\\\"video\\/mp4\\\" width=\\\"1080\\\" height=\\\"1920\\\"> <![CDATA[https:\\/\\/d3jdaktv9qp9iu.cloudfront.net\\/c\\/CjTxsB2oAobxX6SSNa8uhxjy.mp4]]> <\\/MediaFile> <\\/MediaFiles> <Icons><Icon program=\\\"AdChoices\\\" height=\\\"15\\\" width=\\\"15\\\" xPosition=\\\"0\\\" yPosition=\\\"0\\\"><StaticResource creativeType=\\\"image\\/png\\\"><![CDATA[https:\\/\\/static-content-1.smadex.com\\/uploads\\/Custom-Creatives\\/Global+Assets\\/PolicyButton.png]]><\\/StaticResource><IconClickThrough><![CDATA[https:\\/\\/smadex.com\\/data-subject-rights-policy]]><\\/IconClickThrough><\\/Icon><\\/Icons><\\/Linear> <\\/Creative> <Creative adID=\\\"2799063-Companion\\\"> <CompanionAds required=\\\"none\\\"> <Companion width=\\\"1080\\\" height=\\\"1920\\\"> <StaticResource creativeType=\\\"image\\/jpeg\\\"> <![CDATA[https:\\/\\/d3jdaktv9qp9iu.cloudfront.net\\/uploads\\/banners\\/WJrMdLBDnRuZVupb0m.jpeg]]> <\\/StaticResource> <CompanionClickThrough> <![CDATA[itms-apps:\\/\\/itunes.apple.com\\/app\\/id1371565796]]> <\\/CompanionClickThrough> <CompanionClickTracking> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/ct?q=8153aa504071ebc90202467ede0332b6411cf526f180700940395cb4b4a02654f444e414b63c28c5d2200a8f4d8db0178cc5285a6a5412ac38fa95e4652d6a3c7bd742c077d3a99b9e63234b490e7f094c3772afdb7ba142855c4382d08d580a798e24ba3238494a712648bfad4e68b397771d59ba57a527c31ba03ab873eeaff0eb6948471e11403f33628235c78d4337f6686c8043ee190354e4b10b869af043815e27abfe23469cad4e78ca4d631cce704cd6d260aad6b7e7fea0bc44505d04c28a9303c08416b5828ccc4e7cff0ab452e579574afd3716d9ae19d32222849ea97356f336ffa114ccb294f8986a629bf09dc353e4fbbe07f8d1731ace1251ee48aed309dcc0c379de12b9bcaffec4396dda11df7df2a1b09b682a3b745d4af0443bc6ccc4be9195e9c0d0acc9eb6869bcd70cfce6f5d361077e129622b3acd7cdb62334d591fe40bcb8369a1ba9396415b57cb71526e873fb2a0b57e575ff3306b8e2649965cd7f62b7274b555c1d8951d9ecc45e21c51430df793b4450bb099e48a7c82613b390e5a6d77a2e8f17197b578e3ae924796679437956269ec9971e17555efcc8bde2c7b92acc521ff64593b07c5b1b5650e9cb3fe5fce12d47ea65a31bded087076f05ccff5d791dbf79bff7d93c9acb951e68c3c7b6e47448503afb095a3d4f847039650b15c7765869a0d2f2557d6088cf4573f4b9e0e27dd9ad7fb0a8dc79c6c215c73bfef2b5a40f926a2a0e24c1cf611482c4fdcf54a64b809029044c2a05aea1abec0e7fc95e8fb638f6db30472cfae971b6ffe2bb0b958646ee8c8981b26bef2aa061f0d6f77ba359c772e2d208f208b05411b7bb647c3790ac42a0a81b82fed50150f0699b188059288b6364a546fa89e083f53ab22c6f86ece3473ed9d4de0d74b2787c41c9a613aa0af6fe937eab764ca7776547470434c717318a2062a782fcef76c8b92fd43a78296baee1e0cc922f28ea7b95496d4b5128a0a1632e908ac5456f38eb30919918cfe89cdea6ac078c0a85b5f6ad05070da235fb5b63fdc614982b9cd5&cc=companion]]> <\\/CompanionClickTracking> <\\/Companion> <\\/CompanionAds> <\\/Creative> <\\/Creatives> <Impression><![CDATA[https:\\/\\/impression.appsflyer.com\\/id1371565796?pid=smadex_int&c=1099188&af_siteid=305343404&af_c_id=42526&af_adset=Smadex_RV_Raid_DE_iOS_1099188&af_ad_id=2799063&af_ad=RAD_RondaGetRowdy%E2%80%9430sec_V151281_28_AndroidiOSPC_German_Portrait_IMG%3D38Q9&af_sub_siteid=5246194247&af_sub1=&af_sub2=PubMatic&af_viewthrough_lookback=24h&clickid=66936789946c8b4ced0ac07e9330d097e59bf0a7c7db4afa766f9075e45445473e7582a2411a73b12d36a7e675db25ea8c32f8d1ea636d91814db548dff280610e966f156a78c35d767b203d6719f30975be78cd33486a3222e6ece5459e2527fe5e420e906930e79e037845d55f9833c3f4d554cd60422399044fa9336cfa4fc994230edfc82cc746d9a29a12fb0c4829341c45aeda13c2d3d05f513dc64afd3b9fcd667c7f6eea3844a6a687c65b72a7affd5e0b451e14fdcaa69141efcf0b&idfa=&sha1_idfa=]]><\\/Impression><Impression><![CDATA[https:\\/\\/appsrv.display.io\\/imp?msessId=7d79e65e-26c5-4153-93e1-cb59faaecefc&tls=21305666_47_2&p=8159&app=8347]]><\\/Impression><Impression><![CDATA[http:\\/\\/example.com\\/winnoticeurl&price=5.24&id=6452af6ce0cf0&impid=1&bidid=12345&seatid=&curr=USD]]><\\/Impression><\\/InLine> <\\/Ad> <\\/VAST>\\n\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}
