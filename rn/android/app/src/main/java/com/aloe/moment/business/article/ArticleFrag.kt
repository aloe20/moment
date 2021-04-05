package com.aloe.moment.business.article

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.aloe.bean.ClassifyBean
import com.aloe.moment.R
import com.aloe.moment.adapter.FragmentAdapter
import com.aloe.moment.basic.BasicFrag
import com.aloe.moment.databinding.LayoutTabBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ArticleFrag @Inject constructor() : BasicFrag<ArticleVm, LayoutTabBinding>() {
  private val fragments = mutableListOf<ArticleChildFrag>()

  @Suppress("UNCHECKED_CAST")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    with(binding.layoutTitle.toolbar) {
      canBack = false
      setTitle(R.string.wx_article)
      menu.add(R.string.search).setIcon(R.drawable.ic_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
      setOnMenuItemClickListener {
        with(binding.viewPager) {
          (adapter as? FragmentAdapter<ArticleChildFrag>)?.list?.takeUnless { it.isEmpty() }
            ?.get(currentItem)?.arguments?.also {
              findNavController().navigate(R.id.frag_article_search, it)
            }
        }
        true
      }
    }
    vm.getTabLiveData().observe(viewLifecycleOwner) {
      it?.also { initTabs(it) }
    }
  }

  override suspend fun loadData() = vm.loadTabData()

  private fun initTabs(list: List<ClassifyBean>) {
    val tabLayout = binding.viewStubTab2.inflate() as TabLayout
    if (fragments.isEmpty()) {
      fragments.addAll(List(list.size) {
        ArticleChildFrag().apply { arguments = bundleOf("data" to list[it]) }
      })
    }
    binding.viewPager.adapter = FragmentAdapter(this, fragments)
    TabLayoutMediator(tabLayout, binding.viewPager) { tab, position ->
      tab.text = list[position].name
    }.attach()
  }
}