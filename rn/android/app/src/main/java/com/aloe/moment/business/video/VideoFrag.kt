package com.aloe.moment.business.video

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import com.aloe.moment.basic.BasicFrag
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoFrag : BasicFrag<VideoVm, ViewBinding>() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return FrameLayout(requireContext())
  }
  override suspend fun loadData() {
    vm.loadVideo().observe(viewLifecycleOwner){
      Log.e("aloe", "$it")
    }
  }
}