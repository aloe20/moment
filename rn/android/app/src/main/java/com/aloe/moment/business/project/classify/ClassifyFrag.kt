package com.aloe.moment.business.project.classify

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.aloe.bean.ClassifyBean
import com.aloe.moment.R
import com.aloe.moment.adapter.FragmentAdapter
import com.aloe.moment.basic.BasicFrag
import com.aloe.moment.databinding.LayoutTabBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassifyFrag : BasicFrag<ClassifyVm, LayoutTabBinding>() {
  private val fragments = mutableListOf<Fragment>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding.layoutTitle.toolbar.setTitle(R.string.project_classify)
    vm.getTabLiveData().observe(viewLifecycleOwner) { it?.also { initTabs(it) } }
  }

  override suspend fun loadData() = vm.loadTabData()

  private fun initTabs(list: List<ClassifyBean>) {
    var selectIndex = -1
    val tabLayout = binding.viewStubTab2.inflate() as TabLayout
    with(tabLayout) {
      removeAllTabs()
      if (fragments.isEmpty()) {
        selectIndex = 0
        val cid = vm.cid
        fragments.addAll(List(list.size) {
          if (cid == list[it].id) {
            selectIndex = it
          }
          ClassifyChildFrag().apply { arguments = bundleOf("cid" to list[it].id) }
        })
      }
    }
    binding.viewPager.adapter = FragmentAdapter(this, fragments)
    TabLayoutMediator(tabLayout, binding.viewPager) { tab, position ->
      tab.text = list[position].name
    }.attach()
    if (selectIndex > -1) {
      binding.viewPager.setCurrentItem(selectIndex, false)
    }
  }
}