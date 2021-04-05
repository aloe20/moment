package com.aloe.moment.business.react

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.aloe.moment.basic.BasicFrag
import com.aloe.moment.basic.BasicVm
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReactFrag @Inject constructor() : BasicFrag<BasicVm, ViewBinding>() {
  private var reactRootView: ReactView? = null
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    reactRootView = ReactView(requireContext())
    return reactRootView
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    requireActivity().onBackPressedDispatcher.addCallback(
      viewLifecycleOwner,
      object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
          reactRootView?.reactInstanceManager?.onBackPressed()
        }
      })
    reactRootView?.setBackBtnHandler {
      if (reactRootView?.reactInstanceManager == null) {
        findNavController().popBackStack()
      } else {
        reactRootView?.reactInstanceManager?.onBackPressed()
      }
    }?.loadPage(null, null)
  }
}