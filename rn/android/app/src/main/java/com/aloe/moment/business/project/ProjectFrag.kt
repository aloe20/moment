package com.aloe.moment.business.project

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.findNavController
import com.aloe.moment.R
import com.aloe.moment.basic.BasicFrag
import com.aloe.moment.databinding.FragProjectBinding
import com.google.android.material.progressindicator.LinearProgressIndicator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProjectFrag @Inject constructor() : BasicFrag<ProjectVm, FragProjectBinding>() {
  private lateinit var progressView: LinearProgressIndicator
  val adapter = ProjectAdapter(true)
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    progressView = binding.layoutTitle.viewStubProgress.inflate() as LinearProgressIndicator
    with(binding.layoutTitle.toolbar) {
      canBack = false
      setTitle(R.string.project)
      menu.add(R.string.project_classify).setIcon(R.drawable.ic_classify)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
      setOnMenuItemClickListener {
        findNavController().navigate(R.id.frag_project_classify)
        true
      }
    }
    binding.recyclerView.adapter = adapter
  }

  override suspend fun loadData() {
    progressView.visibility = View.VISIBLE
    vm.getPager().observe(viewLifecycleOwner) {
      progressView.visibility = View.GONE
      adapter.submitData(lifecycle, it)
    }
  }
}