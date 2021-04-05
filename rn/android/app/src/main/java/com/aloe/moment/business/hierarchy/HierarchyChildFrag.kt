package com.aloe.moment.business.hierarchy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.aloe.bean.TreeBean
import com.aloe.bean.TreeChildBean
import com.aloe.bean.WebBean
import com.aloe.moment.R
import com.aloe.moment.basic.BasicFrag
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HierarchyChildFrag @Inject constructor() : BasicFrag<HierarchyChildVm, ViewBinding>() {
  @ExperimentalFoundationApi
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return ComposeView(requireContext()).apply {
      setContent {
        HierarchyChildLayout(vm) { tree, child ->
          if (vm.isHierarchy()) {
            findNavController().navigate(
              R.id.frag_hierarchy_detail,
              bundleOf("tree_classify_id" to (tree?.id ?: 0), "tree_child_id" to (child?.id ?: 0))
            )
          } else {
            findNavController().navigate(
              R.id.action_frag_home_to_frag_web,
              bundleOf("data" to WebBean(child?.name, child?.link))
            )
          }
        }
      }
    }
  }

  override suspend fun loadData() = vm.loadListData()
}

@Suppress("FunctionName")
@ExperimentalFoundationApi
@Composable
fun HierarchyChildLayout(
  viewModel: HierarchyChildVm = viewModel(),
  click: (TreeBean?, TreeChildBean?) -> Unit
) {
  val data by viewModel.getListLiveData().observeAsState()
  val color = Color(0xFFEEEEEE)
  LazyColumn {
    data?.forEach {
      stickyHeader {
        Box(modifier = Modifier.fillParentMaxWidth().height(48.dp).background(color).clickable {
          click.invoke(it, null)
        }) {
          Text(it.name, modifier = Modifier.align(Alignment.Center), fontSize = 16.sp)
        }
      }
      for (i in it.children.indices step 2) {
        item {
          Row(modifier = Modifier.fillParentMaxWidth().padding(horizontal = 16.dp)) {
            val size = when (it.children.size.rem(3)) {
              0 -> it.children.size - 1
              1 -> it.children.size + 1
              else -> it.children.size
            }
            for (j in 0..2) {
              val position = i.div(2) * 3 + j
              if (position <= size) {
                if (j != 0) Box(modifier = Modifier.width(16.dp))
                val bg = if (position < it.children.size) color else Color.White
                val bottom = if (position > size - 3) 16.dp else 0.dp
                Box(
                  modifier = Modifier.height(120.dp).weight(1F)
                    .padding(top = 16.dp, bottom = bottom).clip(RoundedCornerShape(8))
                    .clickable(enabled = position < it.children.size) {
                      val child = it.children[position]
                      click.invoke(null, child)
                    }.background(bg)
                ) {
                  Text(
                    if (position < it.children.size) it.children[position].name else "",
                    modifier = Modifier.align(Alignment.Center).padding(horizontal = 8.dp)
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}