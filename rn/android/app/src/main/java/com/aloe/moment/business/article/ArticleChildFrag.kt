package com.aloe.moment.business.article

import android.os.Bundle
import android.view.View
import com.aloe.moment.basic.BasicFrag
import com.aloe.moment.basic.BasicItemDecoration
import com.aloe.moment.databinding.FragArticleChildBinding
import com.aloe.moment.databinding.LayoutTitleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleChildFrag : BasicFrag<ArticleChildVm, FragArticleChildBinding>() {
  private val adapter = ArticleAdapter()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    vm.bean?.keyword?.also {
      LayoutTitleBinding.bind(binding.viewStub.inflate()).toolbar.title = vm.bean?.name
    }
    binding.recyclerView.addItemDecoration(BasicItemDecoration(binding.recyclerView))
    binding.recyclerView.adapter = adapter
  }

  override suspend fun loadData() {
    vm.getPager().observe(viewLifecycleOwner) { adapter.submitData(lifecycle, it) }
  }
}