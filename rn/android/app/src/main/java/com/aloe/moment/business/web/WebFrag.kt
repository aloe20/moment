package com.aloe.moment.business.web

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.aloe.bean.WebBean
import com.aloe.moment.R
import com.aloe.moment.basic.BasicFrag
import com.aloe.moment.basic.BasicVm
import com.aloe.moment.databinding.FragWebBinding
import com.google.android.material.progressindicator.LinearProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class WebFrag : BasicFrag<WebVm, FragWebBinding>() {
  private lateinit var webView: WebLayout
  private lateinit var progressView: LinearProgressIndicator

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    addWebView()
    progressView = binding.layoutTitle.viewStubProgress.inflate() as LinearProgressIndicator
    progressView.isIndeterminate = false
    vm.data?.also { bean ->
      val uri = Uri.parse(bean.github ?: "")
      with(binding.layoutTitle.toolbar) {
        title = bean.name
        if (uri.host?.contains("github.com") == true) {
          menu.add("github").setIcon(R.drawable.ic_github).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
          setOnMenuItemClickListener {
            val name = uri.pathSegments[uri.pathSegments.lastIndex]
            findNavController().navigate(R.id.frag_web, bundleOf("data" to WebBean(name, bean.github)))
            true
          }
        }
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    (webView.parent as ViewGroup).removeView(webView)
  }

  private fun addWebView() {
    if (this::webView.isInitialized) {
      ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).also {
        (binding.root as ViewGroup).addView(webView, it)
      }
    }
  }

  override suspend fun loadData() {
    delay(50)
    webView = WebLayout(requireContext())
    webView.setOnProgress {
      progressView.visibility = if (it == 100) View.GONE else View.VISIBLE
      progressView.progress = it
    }
    addWebView()
    webView.loadUrl(vm.data?.url)
  }
}

@HiltViewModel
class WebVm @Inject constructor(state: SavedStateHandle) : BasicVm() {
  val data: WebBean? = state["data"]
}