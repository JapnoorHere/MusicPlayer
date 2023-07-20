package com.droidbytes.musicplayer

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSeekBar


class CircularSeekBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatSeekBar(context, attrs, defStyleAttr) {

    // Custom attributes variables
    private var progressDrawable: Drawable? = null
    private var thumbDrawable: Drawable? = null

    init {
        // Retrieve custom attributes from XML
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircularSeekBar)
        progressDrawable = typedArray.getDrawable(R.styleable.CircularSeekBar_progressDrawable)
        thumbDrawable = typedArray.getDrawable(R.styleable.CircularSeekBar_thumbDrawable)
        typedArray.recycle()

        // Set the custom drawables
        progressDrawable?.let { setProgressDrawable(it) }
        thumbDrawable?.let { setThumb(it) }
    }
}
