package com.aloe.moment.business.recommend

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.aloe.banner.BannerAdapter
import com.aloe.bean.WanBannerBean
import com.aloe.bean.WebBean
import com.aloe.moment.R
import com.aloe.moment.basic.BasicFrag
import com.aloe.moment.business.home.HomeFrag
import com.aloe.moment.databinding.FragRecommendBinding
import com.google.accompanist.coil.CoilImage
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecommendFrag @Inject constructor() : BasicFrag<RecommendVm, FragRecommendBinding>() {
  private val dialog by lazy { ImageBottomDialog() }
  private var scrollY = 0
  private lateinit var bannerAdapter: BannerAdapter<WanBannerBean>
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    with(binding.navigationView) {
      addHeaderView(ComposeView(view.context).apply {
        setContent {
          NavigationHeaderView(vm) {
            dialog.show(childFragmentManager, "image")
          }
        }
      })
      menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, R.string.react).setIcon(R.drawable.ic_react)
      menu.add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, "视频").setIcon(R.drawable.ic_video)
      setNavigationItemSelectedListener {
        when (it.itemId) {
          Menu.FIRST -> findNavController().navigate(R.id.frag_react)
          Menu.FIRST + 1 -> findNavController().navigate(R.id.frag_video)
        }
        true
      }
    }
    with(binding) {
      layoutTitle.toolbar.canBack = false
      layoutTitle.toolbar.setTitle(R.string.recommend)
      val toggle =
        ActionBarDrawerToggle(requireActivity(), drawerLayout, layoutTitle.toolbar, R.string.open, R.string.close)
      toggle.drawerArrowDrawable.color = Color.BLACK
      drawerLayout.addDrawerListener(toggle)
      toggle.syncState()
      drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
          setFragmentResult(HomeFrag.REQ_HOME, bundleOf(HomeFrag.BUNDLE_PROGRESS to slideOffset))
        }
      })
      lifecycleScope.launchWhenResumed {
        val con = intArrayOf(0, 0)
        ((childFragmentManager.findFragmentByTag("fragment") as? RecommendChildFrag)?.view as? RecyclerView)?.also {
          ((appBar.layoutParams as CoordinatorLayout.LayoutParams).behavior as? CoordinatorLayout.Behavior<View>)
            ?.onNestedPreScroll(coordinator, appBar, it, 0, scrollY, con, ViewCompat.TYPE_NON_TOUCH)
        }
      }
      appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, y -> scrollY = y })
    }
    if (this::bannerAdapter.isInitialized) {
      binding.banner.setAdapter(bannerAdapter)
    }
    childFragmentManager.setFragmentResultListener(REQ_IMAGE, this, { _, result ->
      dialog.dismiss()
      vm.updateImage(result.getParcelable("uri"))
    })
  }

  override suspend fun loadData() {
    initBannerAdapter()
    vm.loadBannerData().observe(viewLifecycleOwner) { binding.banner.setData(it) }
  }

  private fun initBannerAdapter() {
    with(binding.banner) {
      bannerAdapter = BannerAdapter(itemLayout, loop) { v, index ->
        v.findViewById<ImageView>(R.id.imageView).load(binding.banner.getItem<WanBannerBean>(index).imagePath)
      }
      binding.banner.setAdapter(bannerAdapter) { view, index ->
        val bean = binding.banner.getItem<WanBannerBean>(index)
        val resId = R.id.action_frag_home_to_frag_web
        view.findNavController().navigate(resId, bundleOf("data" to WebBean(bean.title, bean.url)))
      }
    }
  }

  companion object {
    const val REQ_IMAGE = "req_image"
  }
}

@Suppress("FunctionName")
@Composable
fun NavigationHeaderView(viewModel: RecommendVm = viewModel(), onClick: () -> Unit) {
  val image by viewModel.getImageLiveData().observeAsState()
  Box(modifier = Modifier.fillMaxWidth().height(200.dp).background(colorResource(R.color.purple_200))) {
    CoilImage(
      data = image ?: R.drawable.ic_user,
      contentDescription = null,
      modifier = Modifier.width(120.dp).height(120.dp).clip(CircleShape).align(Alignment.Center)
        .clickable(onClick = onClick),
      contentScale = ContentScale.Crop
    )
  }
}