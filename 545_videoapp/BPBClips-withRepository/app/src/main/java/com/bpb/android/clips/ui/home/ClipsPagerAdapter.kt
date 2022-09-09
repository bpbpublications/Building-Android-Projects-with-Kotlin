package com.bpb.android.clips.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bpb.android.clips.repository.data.clips.model.Clips

class ClipsPagerAdapter(
    val fragment: HomeFragment,
    val dataList: ArrayList<Clips> = arrayListOf()
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun createFragment(position: Int): Fragment {
        val clipsFragment = ClipFragment.newInstance(
            position, dataList[position]
        )
        clipsFragment.listener = fragment
        return clipsFragment
    }

    fun updateClipsAt(index: Int, clips: Clips) {
        if (index < 0 || index >= dataList.size) return
        dataList[index] = clips
    }
}