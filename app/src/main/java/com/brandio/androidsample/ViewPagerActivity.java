package com.brandio.androidsample;

import static com.brandio.androidsample.tools.DIOAdViewBinder.getAdView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.brandio.ads.Controller;
import com.brandio.ads.ads.Ad;
import com.brandio.ads.ads.AdUnitType;
import com.brandio.ads.exceptions.DIOError;
import com.brandio.ads.listeners.AdEventListener;
import com.brandio.ads.listeners.AdRequestListener;
import com.brandio.ads.placements.InterscrollerPlacement;
import com.brandio.ads.placements.Placement;
import com.brandio.ads.request.AdRequest;
import com.brandio.androidsample.tools.DIOAdRequestHelper;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {
    public static final String TAG = "ViewPagerActivity";
    private final int initialAdPosition = 2;
    private final List<View> dataSet = new ArrayList<>(400);
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private Placement placement;
    private final ArrayList<Ad> receivedAds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        String placementId = getIntent().getStringExtra(MainActivity.PLACEMENT_ID);
        boolean isORTB = getIntent().getStringExtra(MainActivity.NAME).contains("ORTB");
        try {
            placement = Controller.getInstance().getPlacement(placementId);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ViewPagerActivity.this,
                    "DioSdkException: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ViewPager2 viewPager = findViewById(R.id.view_pager2);

        for (int i = 0; i <= 400; i++) {
            dataSet.add(null);
        }
        adapter = new RecyclerViewAdapter(dataSet, true);
        viewPager.setAdapter(adapter);

        for (int i = 1; i <= 10; i++) {
            if (isORTB) {
                loadAdFromORTB(i);
            } else {
                loadAd(i);
            }
        }
    }

    private void loadAd(int num) {
//        final AdRequest adRequest = placement.newAdRequest(); // use default ad request
        final AdRequest adRequest = DIOAdRequestHelper.createAndPopulateAdRequest(placement); // use customised ad request
        adRequest.setAdRequestListener(new AdRequestListener() {
            @Override
            public void onAdReceived(Ad ad) {
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
                receivedAds.add(ad);
                dataSet.set(initialAdPosition * num, getAdView(ad, ViewPagerActivity.this));
                adapter.notifyItemChanged(initialAdPosition + num);
            }

            @Override
            public void onNoAds(DIOError error) {
                Toast.makeText(ViewPagerActivity.this,
                        "No Ads placement " + placement.getId(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailedToLoad(DIOError dioError) {
                Toast.makeText(ViewPagerActivity.this,
                        "Ad for placement " + placement.getId() + " failed to load", Toast.LENGTH_LONG).show();

            }
        });

        adRequest.requestAd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Ad ad : receivedAds) {
            ad.close();
        }
    }

    private void loadAdFromORTB(int num) {
        if (placement.getType() != AdUnitType.INTERSCROLLER) {
            Toast.makeText(ViewPagerActivity.this,
                    "Wrong placement type. Only Interscroller is supported.", Toast.LENGTH_LONG).show();
            return;
        }

        InterscrollerPlacement interscrollerPlacement = (InterscrollerPlacement) placement;

        interscrollerPlacement.loadInterscrollerFromORTB(getSampleOrtbResponse(), new Placement.OnORTBLoadListener() {
            @Override
            public void onLoaded(Ad ad) {
                receivedAds.add(ad);
                dataSet.set(initialAdPosition * num, getAdView(ad, ViewPagerActivity.this));
                adapter.notifyItemChanged(initialAdPosition + num);
            }

            @Override
            public void onFailToLoad(DIOError e) {
                Log.e(TAG, "Failed to load ad");
            }
        });
    }

    //just a mock bid response to demonstrate how to render oRTB ads on DIO SDK
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
                "                    \"adm\": \"<?xml version=\\\"1.0\\\"?>\\n<VAST version=\\\"3.0\\\"> " +
                "<Ad id=\\\"2799063\\\"> " +
                "<InLine> " +
                "<AdSystem version=\\\"1.0\\\"> <![CDATA[AdServer]]> <\\/AdSystem> " +
                "<AdTitle> <![CDATA[Video Ad]]> <\\/AdTitle> " +
                "<Impression> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/hyperad\\/rtb\\/111285\\/impression\\/17-H4sIAAAAAAAAAI2SPWgUQRTHN4_zOI8zATmChZhoo4WzzOfOzhXmEnKYYBElIYXNMft1Wdi9O2_34ndhoY1oFWzSWFlYpE5n0EorLcROSWEVED8glYhzq0IECwemeDP_997__Xiw8WAKLMuCY3E3DwdZHuexTkz8fBtgfpihUGc5ojbBOM1aNiZYegSrSPue7weCQFMnnd4gztdS1NFp3O0gP9fI72sUd7NcJ8l_KL5_-PGiBpXUy6_Ck0ebY7A3cXhiov5lBp6OrVN00NnBvFvUllK50mWyhcTZfxc3IiKFcojk0qHccbnRwrdnH-8debN_bjS6OZ9nAIhlLgCUoLz17i1YpfkW1If9JI7y9sjk7o361vndrzNQWTdIoETUKBMmiesaOtJ2bGxBdbbfT0KoxRfXet0QynEvgxrhtpFYUM9SPcj7xQ_AGT2StuMibq-HA8LbLhwP4qyf6Ot23Jtenr8AVWYLW4H126hpuBrRBmMixCQizKc8lCIwCFQoqKe0wkQEfhAGKuC-KPLgKMOCccYxh8f7rypQXZydM-6h4jLuGif1lWHqJYN2vLQMTamUI33CsMscrCPHdR1fR77gLom0CgIYp5QpIqkZsts2Mzx8uXH_tOljCn3aPASvN04BcBPuvL9ThR2zRX8Q1wgh1BVGN04dilaXCEzO6WB6oZd6w0Hn76AMmGLKEOaIqBXiNDBrYGorLi4vaBVxabCjIPQ9REgYIFczD3EV4cDhkgdEwdTdvUvbJxabwIpzsgmlhRaUDY6qQ5jAVoEUKqMuVuG6oDs9WsJGHpqlv4I8s0Ojx9LN279IXvsJ3XT5Zi8DAAA\\/1\\/a9f47188-decb-11ed-8a3b-49f0d6474d19\\/0.200010\\/notify?cid=279848]]> <\\/Impression> " +
                "<Creatives> <Creative id=\\\"1\\\" sequence=\\\"1\\\"> <Linear skipoffset=\\\"00:00:05\\\"> <Duration>00:00:27<\\/Duration> <TrackingEvents> " +
                "<Tracking event=\\\"start\\\"> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/hyperad\\/tracking\\/action\\/start?click_id=66936789946c8b4ced0ac07e9330d097e59bf0a7c7db4afa766f9075e45445473e7582a2411a73b12d36a7e675db25ea8c32f8d1ea636d91814db548dff280610e966f156a78c35d767b203d6719f30975be78cd33486a3222e6ece5459e2527fe5e420e906930e79e037845d55f9833c3f4d554cd60422399044fa9336cfa4fc994230edfc82cc746d9a29a12fb0c4829341c45aeda13c2d3d05f513dc64afd3b9fcd667c7f6eea3844a6a687c65b72a7affd5e0b451e14fdcaa69141efcf0b&crid=2799063]]> <\\/Tracking> " +
                "<Tracking event=\\\"firstQuartile\\\"> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/hyperad\\/tracking\\/action\\/firstQuartile?click_id=66936789946c8b4ced0ac07e9330d097e59bf0a7c7db4afa766f9075e45445473e7582a2411a73b12d36a7e675db25ea8c32f8d1ea636d91814db548dff280610e966f156a78c35d767b203d6719f30975be78cd33486a3222e6ece5459e2527fe5e420e906930e79e037845d55f9833c3f4d554cd60422399044fa9336cfa4fc994230edfc82cc746d9a29a12fb0c4829341c45aeda13c2d3d05f513dc64afd3b9fcd667c7f6eea3844a6a687c65b72a7affd5e0b451e14fdcaa69141efcf0b&crid=2799063]]> <\\/Tracking> " +
                "<Tracking event=\\\"midpoint\\\"> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/hyperad\\/tracking\\/action\\/midpoint?click_id=66936789946c8b4ced0ac07e9330d097e59bf0a7c7db4afa766f9075e45445473e7582a2411a73b12d36a7e675db25ea8c32f8d1ea636d91814db548dff280610e966f156a78c35d767b203d6719f30975be78cd33486a3222e6ece5459e2527fe5e420e906930e79e037845d55f9833c3f4d554cd60422399044fa9336cfa4fc994230edfc82cc746d9a29a12fb0c4829341c45aeda13c2d3d05f513dc64afd3b9fcd667c7f6eea3844a6a687c65b72a7affd5e0b451e14fdcaa69141efcf0b&crid=2799063]]> <\\/Tracking> " +
                "<Tracking event=\\\"thirdQuartile\\\"> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/hyperad\\/tracking\\/action\\/thirdQuartile?click_id=66936789946c8b4ced0ac07e9330d097e59bf0a7c7db4afa766f9075e45445473e7582a2411a73b12d36a7e675db25ea8c32f8d1ea636d91814db548dff280610e966f156a78c35d767b203d6719f30975be78cd33486a3222e6ece5459e2527fe5e420e906930e79e037845d55f9833c3f4d554cd60422399044fa9336cfa4fc994230edfc82cc746d9a29a12fb0c4829341c45aeda13c2d3d05f513dc64afd3b9fcd667c7f6eea3844a6a687c65b72a7affd5e0b451e14fdcaa69141efcf0b&crid=2799063]]> <\\/Tracking> " +
                "<Tracking event=\\\"complete\\\"> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/hyperad\\/tracking\\/action\\/complete?click_id=66936789946c8b4ced0ac07e9330d097e59bf0a7c7db4afa766f9075e45445473e7582a2411a73b12d36a7e675db25ea8c32f8d1ea636d91814db548dff280610e966f156a78c35d767b203d6719f30975be78cd33486a3222e6ece5459e2527fe5e420e906930e79e037845d55f9833c3f4d554cd60422399044fa9336cfa4fc994230edfc82cc746d9a29a12fb0c4829341c45aeda13c2d3d05f513dc64afd3b9fcd667c7f6eea3844a6a687c65b72a7affd5e0b451e14fdcaa69141efcf0b&crid=2799063]]> <\\/Tracking> " +
                "<Tracking event=\\\"skip\\\"> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/hyperad\\/tracking\\/action\\/skip?click_id=66936789946c8b4ced0ac07e9330d097e59bf0a7c7db4afa766f9075e45445473e7582a2411a73b12d36a7e675db25ea8c32f8d1ea636d91814db548dff280610e966f156a78c35d767b203d6719f30975be78cd33486a3222e6ece5459e2527fe5e420e906930e79e037845d55f9833c3f4d554cd60422399044fa9336cfa4fc994230edfc82cc746d9a29a12fb0c4829341c45aeda13c2d3d05f513dc64afd3b9fcd667c7f6eea3844a6a687c65b72a7affd5e0b451e14fdcaa69141efcf0b&crid=2799063]]> <\\/Tracking> <\\/TrackingEvents> " +
                "<VideoClicks> <ClickThrough> <![CDATA[itms-apps:\\/\\/itunes.apple.com\\/app\\/id1371565796]]> <\\/ClickThrough> " +
                "<ClickTracking> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/ct?q=8153aa504071ebc90202467ede0332b6411cf526f180700940395cb4b4a02654f444e414b63c28c5d2200a8f4d8db0178cc5285a6a5412ac38fa95e4652d6a3c7bd742c077d3a99b9e63234b490e7f094c3772afdb7ba142855c4382d08d580a798e24ba3238494a712648bfad4e68b397771d59ba57a527c31ba03ab873eeaff0eb6948471e11403f33628235c78d4337f6686c8043ee190354e4b10b869af043815e27abfe23469cad4e78ca4d631cce704cd6d260aad6b7e7fea0bc44505d04c28a9303c08416b5828ccc4e7cff0ab452e579574afd3716d9ae19d32222849ea97356f336ffa114ccb294f8986a629bf09dc353e4fbbe07f8d1731ace1251ee48aed309dcc0c379de12b9bcaffec4396dda11df7df2a1b09b682a3b745d4af0443bc6ccc4be9195e9c0d0acc9eb6869bcd70cfce6f5d361077e129622b3acd7cdb62334d591fe40bcb8369a1ba9396415b57cb71526e873fb2a0b57e575ff3306b8e2649965cd7f62b7274b555c1d8951d9ecc45e21c51430df793b4450bb099e48a7c82613b390e5a6d77a2e8f17197b578e3ae924796679437956269ec9971e17555efcc8bde2c7b92acc521ff64593b07c5b1b5650e9cb3fe5fce12d47ea65a31bded087076f05ccff5d791dbf79bff7d93c9acb951e68c3c7b6e47448503afb095a3d4f847039650b15c7765869a0d2f2557d6088cf4573f4b9e0e27dd9ad7fb0a8dc79c6c215c73bfef2b5a40f926a2a0e24c1cf611482c4fdcf54a64b809029044c2a05aea1abec0e7fc95e8fb638f6db30472cfae971b6ffe2bb0b958646ee8c8981b26bef2aa061f0d6f77ba359c772e2d208f208b05411b7bb647c3790ac42a0a81b82fed50150f0699b188059288b6364a546fa89e083f53ab22c6f86ece3473ed9d4de0d74b2787c41c9a613aa0af6fe937eab764ca7776547470434c717318a2062a782fcef76c8b92fd43a78296baee1e0cc922f28ea7b95496d4b5128a0a1632e908ac5456f38eb30919918cfe89cdea6ac078c0a85b5f6ad05070da235fb5b63fdc614982b9cd5]]> <\\/ClickTracking> " +
                "<ClickTracking><![CDATA[https:\\/\\/appsrv.display.io\\/click?msessId=7d79e65e-26c5-4153-93e1-cb59faaecefc&tls=21305666_47_2&p=8159&app=8347]]><\\/ClickTracking><\\/VideoClicks> <MediaFiles> <MediaFile delivery=\\\"progressive\\\" bitrate=\\\"496\\\" type=\\\"video\\/mp4\\\" width=\\\"1080\\\" height=\\\"1920\\\"> <![CDATA[https:\\/\\/d3jdaktv9qp9iu.cloudfront.net\\/c\\/CjTxsB2oAobxX6SSNa8uhxjy.mp4]]> <\\/MediaFile> <\\/MediaFiles> " +
                "<Icons><Icon program=\\\"AdChoices\\\" height=\\\"15\\\" width=\\\"15\\\" xPosition=\\\"0\\\" yPosition=\\\"0\\\"><StaticResource creativeType=\\\"image\\/png\\\"><![CDATA[https:\\/\\/static-content-1.smadex.com\\/uploads\\/Custom-Creatives\\/Global+Assets\\/PolicyButton.png]]><\\/StaticResource><IconClickThrough><![CDATA[https:\\/\\/smadex.com\\/data-subject-rights-policy]]><\\/IconClickThrough><\\/Icon><\\/Icons><\\/Linear> <\\/Creative> <Creative adID=\\\"2799063-Companion\\\"> " +
                "<CompanionAds required=\\\"none\\\"> <Companion width=\\\"1080\\\" height=\\\"1920\\\"> <StaticResource creativeType=\\\"image\\/jpeg\\\"> <![CDATA[https:\\/\\/d3jdaktv9qp9iu.cloudfront.net\\/uploads\\/banners\\/WJrMdLBDnRuZVupb0m.jpeg]]> <\\/StaticResource> <CompanionClickThrough> <![CDATA[itms-apps:\\/\\/itunes.apple.com\\/app\\/id1371565796]]> <\\/CompanionClickThrough> <CompanionClickTracking> <![CDATA[https:\\/\\/geo-tracker.smadex.com\\/ct?q=8153aa504071ebc90202467ede0332b6411cf526f180700940395cb4b4a02654f444e414b63c28c5d2200a8f4d8db0178cc5285a6a5412ac38fa95e4652d6a3c7bd742c077d3a99b9e63234b490e7f094c3772afdb7ba142855c4382d08d580a798e24ba3238494a712648bfad4e68b397771d59ba57a527c31ba03ab873eeaff0eb6948471e11403f33628235c78d4337f6686c8043ee190354e4b10b869af043815e27abfe23469cad4e78ca4d631cce704cd6d260aad6b7e7fea0bc44505d04c28a9303c08416b5828ccc4e7cff0ab452e579574afd3716d9ae19d32222849ea97356f336ffa114ccb294f8986a629bf09dc353e4fbbe07f8d1731ace1251ee48aed309dcc0c379de12b9bcaffec4396dda11df7df2a1b09b682a3b745d4af0443bc6ccc4be9195e9c0d0acc9eb6869bcd70cfce6f5d361077e129622b3acd7cdb62334d591fe40bcb8369a1ba9396415b57cb71526e873fb2a0b57e575ff3306b8e2649965cd7f62b7274b555c1d8951d9ecc45e21c51430df793b4450bb099e48a7c82613b390e5a6d77a2e8f17197b578e3ae924796679437956269ec9971e17555efcc8bde2c7b92acc521ff64593b07c5b1b5650e9cb3fe5fce12d47ea65a31bded087076f05ccff5d791dbf79bff7d93c9acb951e68c3c7b6e47448503afb095a3d4f847039650b15c7765869a0d2f2557d6088cf4573f4b9e0e27dd9ad7fb0a8dc79c6c215c73bfef2b5a40f926a2a0e24c1cf611482c4fdcf54a64b809029044c2a05aea1abec0e7fc95e8fb638f6db30472cfae971b6ffe2bb0b958646ee8c8981b26bef2aa061f0d6f77ba359c772e2d208f208b05411b7bb647c3790ac42a0a81b82fed50150f0699b188059288b6364a546fa89e083f53ab22c6f86ece3473ed9d4de0d74b2787c41c9a613aa0af6fe937eab764ca7776547470434c717318a2062a782fcef76c8b92fd43a78296baee1e0cc922f28ea7b95496d4b5128a0a1632e908ac5456f38eb30919918cfe89cdea6ac078c0a85b5f6ad05070da235fb5b63fdc614982b9cd5&cc=companion]]> <\\/CompanionClickTracking> <\\/Companion> <\\/CompanionAds> <\\/Creative> <\\/Creatives> " +
                "<Impression><![CDATA[https:\\/\\/impression.appsflyer.com\\/id1371565796?pid=smadex_int&c=1099188&af_siteid=305343404&af_c_id=42526&af_adset=Smadex_RV_Raid_DE_iOS_1099188&af_ad_id=2799063&af_ad=RAD_RondaGetRowdy%E2%80%9430sec_V151281_28_AndroidiOSPC_German_Portrait_IMG%3D38Q9&af_sub_siteid=5246194247&af_sub1=&af_sub2=PubMatic&af_viewthrough_lookback=24h&clickid=66936789946c8b4ced0ac07e9330d097e59bf0a7c7db4afa766f9075e45445473e7582a2411a73b12d36a7e675db25ea8c32f8d1ea636d91814db548dff280610e966f156a78c35d767b203d6719f30975be78cd33486a3222e6ece5459e2527fe5e420e906930e79e037845d55f9833c3f4d554cd60422399044fa9336cfa4fc994230edfc82cc746d9a29a12fb0c4829341c45aeda13c2d3d05f513dc64afd3b9fcd667c7f6eea3844a6a687c65b72a7affd5e0b451e14fdcaa69141efcf0b&idfa=&sha1_idfa=]]><\\/Impression><Impression><![CDATA[https:\\/\\/appsrv.display.io\\/imp?msessId=7d79e65e-26c5-4153-93e1-cb59faaecefc&tls=21305666_47_2&p=8159&app=8347]]><\\/Impression><Impression><![CDATA[http:\\/\\/example.com\\/winnoticeurl&price=5.24&id=6452af6ce0cf0&impid=1&bidid=12345&seatid=&curr=USD]]><\\/Impression><\\/InLine> <\\/Ad> <\\/VAST>\\n\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}