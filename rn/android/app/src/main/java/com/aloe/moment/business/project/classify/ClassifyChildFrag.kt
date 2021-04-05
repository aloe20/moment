package com.aloe.moment.business.project.classify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.aloe.moment.basic.BasicFrag
import com.aloe.moment.business.project.ProjectAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClassifyChildFrag : BasicFrag<ClassifyVm, ViewBinding>() {
  private val adapter = ProjectAdapter()
  private var scrollY = 0

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
    RecyclerView(requireContext())

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    (view as? RecyclerView)?.also {
      it.layoutManager = LinearLayoutManager(it.context)
      it.adapter = adapter
      it.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
          super.onScrolled(recyclerView, dx, dy)
          scrollY += dy
        }
      })
      it.scrollTo(0, scrollY)
    }
  }

  override suspend fun loadData() {
    vm.getPager().observe(viewLifecycleOwner) { adapter.submitData(lifecycle, it) }
  }
}