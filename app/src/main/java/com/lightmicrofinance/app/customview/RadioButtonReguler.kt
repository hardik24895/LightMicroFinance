package com.commonProject.customview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.RadioButton


@SuppressLint("AppCompatCustomView")
class RadioButtonReguler : RadioButton {
    constructor(context: Context) : super(context) {
        val face = Typeface.createFromAsset(context.assets, "font/Poppins-Medium.ttf")
        this.typeface = face
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val face = Typeface.createFromAsset(context.assets, "font/Poppins-Medium.ttf")
        this.typeface = face
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        val face = Typeface.createFromAsset(context.assets, "font/Poppins-Medium.ttf")
        this.typeface = face
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}

