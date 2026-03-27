package com.jomar.senhorpintor

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.LruCache
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager

import kotlin.math.abs


fun Activity.makeItFullScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }
}

class FadePageTransformer: ViewPager.PageTransformer {
    override fun transformPage(view: View, position: Float) {
        view.alpha = when {
            position <= -1.0F -> 0.0F
            position == 0.0F -> 1.0F
            else -> 1.0F - abs(position)
        }
    }
}


fun View.makeItVisible(){
    this.visibility = View.VISIBLE
}

fun View.makeItGone(){
    this.visibility = View.GONE
}

fun View.makeItInvisible(){
    this.visibility = View.INVISIBLE
}

// Extension property to get bitmap from view
val View.bitmap: Bitmap
    get() {
        // Screenshot taken for the specified root view and its child elements.
        val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.draw(canvas)
        return bitmap
    }
// Extension property to get bitmap from RecyclerView
fun RecyclerView.getRecyclerViewScreenshot(): Bitmap? {
    val adapter = this.adapter
    var bigBitmap: Bitmap? = null
    if (adapter != null) {
        val size = adapter.itemCount
        var height = 0
        val paint = Paint()
        var iHeight = 0
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        // Use 1/8th of the available memory for this memory cache.
        val cacheSize = maxMemory / 8
        val bitmaCache: LruCache<String, Bitmap> = LruCache(cacheSize)
        for (i in 0 until size) {
            val holder = adapter.createViewHolder(this, adapter.getItemViewType(i))
            adapter.onBindViewHolder(holder, i)
            holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(this.width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            holder.itemView.layout(0, 0, holder.itemView.measuredWidth, holder.itemView.measuredHeight)
            holder.itemView.isDrawingCacheEnabled = true
            holder.itemView.buildDrawingCache()
            val drawingCache = holder.itemView.drawingCache
            if (drawingCache != null) {
                bitmaCache.put(i.toString(), drawingCache)
            }
            //                holder.itemView.setDrawingCacheEnabled(false);
//                holder.itemView.destroyDrawingCache();
            height += holder.itemView.measuredHeight
        }
        bigBitmap = Bitmap.createBitmap(this.measuredWidth, height, Bitmap.Config.ARGB_8888)
        val bigCanvas = Canvas(bigBitmap)
        bigCanvas.drawColor(Color.WHITE)
        for (i in 0 until size) {
            val bitmap: Bitmap = bitmaCache.get(i.toString())
            bigCanvas.drawBitmap(bitmap, 0f, iHeight.toFloat(), paint)
            iHeight += bitmap.height
            bitmap.recycle()
        }
    }
    return bigBitmap
}