package com.brandio.androidsample

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.brandio.ads.Controller
import com.brandio.ads.ads.Ad
import com.brandio.ads.exceptions.DIOError
import com.brandio.ads.listeners.AdEventListener
import com.brandio.ads.listeners.AdRequestListener
import com.brandio.ads.placements.Placement
import com.brandio.androidsample.tools.DIOAdRequestHelper
import com.brandio.androidsample.tools.DIOAdViewBinder
import com.brandio.androidsample.ui.theme.AndroidSampleTheme

class ComposeFeedActivity : ComponentActivity() {
    val TAG: String = "FeedActivity"
    private val initialAdPosition = 10
    private val receivedAds = mutableStateListOf<Ad?>().apply {
        repeat(400) { add(null) }
    }
    private var placement: Placement? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSampleTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        ItemList(
                            items = receivedAds,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                )
            }
        }
        val placementId = intent.getStringExtra(MainActivity.PLACEMENT_ID)

        try {
            placement = Controller.getInstance().getPlacement(placementId)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(
                this@ComposeFeedActivity,
                "DioSdkException: " + e.message,
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        for (i in 1..10) {
            loadAd(i)
        }
    }

    private fun loadAd(num: Int) {
        val adRequest =
            DIOAdRequestHelper.createAndPopulateAdRequest(placement)
        adRequest.setAdRequestListener(object : AdRequestListener {
            override fun onAdReceived(ad: Ad) {
                ad.setEventListener(object : AdEventListener {
                    override fun onShown(ad: Ad) {
                        Log.e(TAG, "onShown")
                    }

                    override fun onFailedToShow(ad: Ad) {
                        Log.e(TAG, "onFailedToShow")
                    }

                    override fun onClicked(ad: Ad) {
                        Log.e(TAG, "onClicked")
                    }

                    override fun onClosed(ad: Ad) {
                        Log.e(TAG, "onClosed")
                    }

                    override fun onAdCompleted(ad: Ad) {
                        Log.e(TAG, "onAdCompleted")
                    }
                })
                receivedAds[initialAdPosition * num] = ad
            }

            override fun onNoAds(error: DIOError) {
                Toast.makeText(
                    this@ComposeFeedActivity,
                    "No Ads placement " + (placement?.id ?: ""), Toast.LENGTH_LONG
                ).show()
            }

            override fun onFailedToLoad(dioError: DIOError) {
                Toast.makeText(
                    this@ComposeFeedActivity,
                    "Ad for placement " + (placement?.id ?: "") + " failed to load",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        adRequest.requestAd()
    }

    override fun onDestroy() {
        super.onDestroy()
        for (ad in receivedAds) {
            ad?.close()
        }
    }
}

@Composable
fun ItemList(items: List<Ad?>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items.size) { index ->
            ListItem(ad = items[index])
        }
    }
}

@Composable
fun ListItem(ad: Ad?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        AndroidView(
            modifier = Modifier.weight(1f),
            factory = { ctx ->
                val adView: View? = DIOAdViewBinder.getAdView(ad, ctx)
                adView ?: LayoutInflater.from(ctx).inflate(R.layout.infeed_list_item, null, false)

            }
        )

    }
}