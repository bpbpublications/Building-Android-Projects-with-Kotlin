package com.bpb.android.clips.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bpb.android.clips.repository.data.clips.model.ClipsModel

class ClipsPagerAdapter(
    fragment: Fragment,
    val dataList: ArrayList<ClipsModel> = arrayListOf()
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun createFragment(position: Int): Fragment {
        return ClipFragment.newInstance(dataList[position])
    }
}