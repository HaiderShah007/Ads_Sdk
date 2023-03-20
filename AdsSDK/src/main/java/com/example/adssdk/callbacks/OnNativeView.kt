package com.example.adssdk.callbacks

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

interface OnNativeView {
        fun nativeViewFind(
            cardViewNative: ConstraintLayout,
            headlineView: TextView,
            bodyView: TextView,
            ad: TextView
        )
    }