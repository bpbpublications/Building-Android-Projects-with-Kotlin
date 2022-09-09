package com.bpb.android.bpbchat.ui.utils

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.replaceFragment(
    @IdRes containerViewId: Int,
    fragment: Fragment,
    tag: String = fragment.javaClass.simpleName,
    allowStateLoss: Boolean = false
) {
    val transaction = supportFragmentManager
        .beginTransaction()
        .replace(containerViewId, fragment, tag)

    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}