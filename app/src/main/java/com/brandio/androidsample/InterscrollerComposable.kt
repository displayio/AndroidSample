package com.brandio.androidsample

import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.brandio.ads.Controller
import com.brandio.ads.containers.InterscrollerContainer
import com.brandio.ads.placements.InterscrollerPlacement

private const val TAG = "ComposeIS"

/**
 * Interscroller ad slot for Jetpack Compose LazyColumn.
 *
 * Handles:
 * - Reveal/parallax effect via graphicsLayer (no View invalidation)
 * - Vertical scroll passthrough to LazyColumn (drag + fling)
 * - Tap forwarding to ad WebView for click handling
 *
 * Must be called from LazyColumn item scope (uses fillParentMaxHeight).
 */
@Composable
fun LazyItemScope.InterscrollerComposable(
    placementId: String,
    requestId: String,
    listState: LazyListState,
    topOffset: Float = 0f
) {
    val context = LocalContext.current
    var slotY by remember { mutableFloatStateOf(0f) }
    val flingBehavior = ScrollableDefaults.flingBehavior()
    var holderRef: RelativeLayout? by remember { mutableStateOf(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillParentMaxHeight()
            .clipToBounds()
            .onGloballyPositioned { coordinates ->
                slotY = coordinates.positionInRoot().y
            }
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { translationY = -(slotY - topOffset) },
            factory = {
                val holder = RelativeLayout(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
                holderRef = holder
                try {
                    val placement = Controller.getInstance()
                        .getPlacement(placementId) as InterscrollerPlacement
                    val container: InterscrollerContainer = placement.getContainer(requestId)
                    container.bindTo(holder)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to bind interscroller", e)
                }
                holder
            }
        )
        // Overlay: captures vertical scroll (drag + fling) for LazyColumn,
        // forwards taps to ad view for click handling
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scrollable(
                    state = listState,
                    orientation = Orientation.Vertical,
                    flingBehavior = flingBehavior,
                    reverseDirection = true
                )
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        holderRef?.let { holder ->
                            val now = SystemClock.uptimeMillis()
                            val x = offset.x
                            val y = offset.y + slotY
                            val down = MotionEvent.obtain(now, now, MotionEvent.ACTION_DOWN, x, y, 0)
                            val up = MotionEvent.obtain(now, now, MotionEvent.ACTION_UP, x, y, 0)
                            holder.dispatchTouchEvent(down)
                            holder.dispatchTouchEvent(up)
                            down.recycle()
                            up.recycle()
                        }
                    }
                }
        )
    }
}
