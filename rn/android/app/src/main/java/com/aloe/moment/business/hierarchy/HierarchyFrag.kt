package com.aloe.moment.business.hierarchy

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.aloe.moment.R
import com.aloe.moment.adapter.FragmentAdapter
import com.aloe.moment.basic.BasicFrag
import com.aloe.moment.basic.BasicVm
import com.aloe.moment.databinding.FragHierarchyBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HierarchyFrag @Inject constructor() : BasicFrag<BasicVm, FragHierarchyBinding>() {
  @Inject
  @Named("HierarchyFrag")
  lateinit var fragments: MutableList<Fragment>
  private val tabs: IntArray = intArrayOf(R.string.hierarchy, R.string.navigation)
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    fragments.forEachIndexed { index, fragment ->
      fragment.arguments = bundleOf("type" to getString(tabs[index]))
    }
    binding.viewPager.adapter = FragmentAdapter(this, fragments)
    with(binding.layoutTitle) {
      toolbarLayout.isTitleEnabled = false
      toolbar.canBack = false
      (viewStubTab.inflate() as TabLayout).also {
        TabLayoutMediator(it, binding.viewPager) { tab, position ->
          tab.setText(tabs[position])
        }.attach()
      }
    }
  }
}