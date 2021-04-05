package com.aloe.moment.business.hierarchy

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.aloe.moment.adapter.FragmentAdapter
import com.aloe.moment.basic.BasicFrag
import com.aloe.moment.business.recommend.RecommendChildFrag
import com.aloe.moment.databinding.LayoutTabBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HierarchyDetailFrag : BasicFrag<HierarchyDetailVm, LayoutTabBinding>() {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    vm.getTreeClassifyLiveData().observe(viewLifecycleOwner) {
      it?.also {
        binding.layoutTitle.toolbar.title = it.name
        val tabLayout = binding.viewStubTab2.inflate() as TabLayout
        binding.viewPager.adapter =
          FragmentAdapter(this, it.children.map { RecommendChildFrag().apply { arguments = bundleOf("cid" to it.id) } })
        TabLayoutMediator(tabLayout, binding.viewPager) { tab, position ->
          tab.text = it.children[position].name
        }.attach()
      }
    }
    vm.getTreeChildLiveData().observe(viewLifecycleOwner) {
      it?.also {
        binding.layoutTitle.toolbar.title = it.name
        binding.viewPager.adapter =
          FragmentAdapter(this, listOf(RecommendChildFrag().apply { arguments = bundleOf("cid" to it.id) }))
      }
    }
  }

  override suspend fun loadData() {
    vm.loadTreeData()
    vm.loadTreeChildData()
  }
}
