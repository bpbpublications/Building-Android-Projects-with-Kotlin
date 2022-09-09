package com.bpb.android.clips

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment

open class BpbClipsBaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(view) { v, windowInsets ->
            v.updatePadding(top = windowInsets.systemWindowInsetTop)
            windowInsets.consumeSystemWindowInsets()
        }
    }
}