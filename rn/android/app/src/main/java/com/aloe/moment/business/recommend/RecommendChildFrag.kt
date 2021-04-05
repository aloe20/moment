package com.aloe.moment.business.recommend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.aloe.moment.basic.BasicFrag
import com.aloe.moment.basic.BasicItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecommendChildFrag : BasicFrag<RecommendChildVm, ViewBinding>() {
  val topAdapter = TopAdapter()
  val listAdapter = PagingAdapter()
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return RecyclerView(requireContext())
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val recyclerView = view as RecyclerView
    recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    recyclerView.adapter = ConcatAdapter(topAdapter, listAdapter)
    recyclerView.addItemDecoration(BasicItemDecoration(recyclerView))
  }

  override suspend fun loadData() {
    vm.loadTopData().observe(viewLifecycleOwner) { topAdapter.submitList(it) }
    vm.getPager().observe(viewLifecycleOwner) { listAdapter.submitData(lifecycle, it) }
  }
}