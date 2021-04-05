package com.aloe.moment.business.home

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.aloe.moment.R
import com.aloe.moment.adapter.FragmentAdapter
import com.aloe.moment.basic.BasicFrag
import com.aloe.moment.basic.BasicVm
import com.aloe.moment.databinding.FragHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class HomeFrag : BasicFrag<BasicVm, FragHomeBinding>() {
  @Inject
  @Named("HomeFrag")
  lateinit var fragments: MutableList<Fragment>
  private lateinit var pageChangeCallback: ViewPager2.OnPageChangeCallback
  private val ids = mutableListOf<Int>()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
      override fun onPageSelected(position: Int) {
        binding.bottomNavigation.selectedItemId = ids[position]
      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    with(binding.bottomNavigation) {
      with(menu) {
        add(Menu.NONE, Menu.FIRST, Menu.NONE, R.string.recommend).setIcon(R.drawable.ic_recommend)
        add(Menu.NONE, Menu.FIRST + 1, Menu.NONE, R.string.navigation).setIcon(R.drawable.ic_navigation)
        add(Menu.NONE, Menu.FIRST + 2, Menu.NONE, R.string.project).setIcon(R.drawable.ic_project)
        add(Menu.NONE, Menu.FIRST + 3, Menu.NONE, R.string.wx_article).setIcon(R.drawable.ic_article)
      }
      (getChildAt(0) as ViewGroup).forEach {
        ids.add(it.id)
        //取消长按提示
        it.setOnLongClickListener { true }
      }
      setOnNavigationItemSelectedListener { menu ->
        ids.indexOf(menu.itemId).takeUnless { it == binding.viewPager.currentItem }?.also {
          binding.viewPager.setCurrentItem(it, false)
        }
        true
      }
    }
    with(binding.viewPager) {
      adapter = FragmentAdapter(this@HomeFrag, fragments)
      isUserInputEnabled = false
      registerOnPageChangeCallback(pageChangeCallback)
    }
    val size = resources.getDimensionPixelSize(R.dimen.bottomNavigationBar)
    childFragmentManager.setFragmentResultListener(REQ_HOME, viewLifecycleOwner, { _, result ->
      binding.bottomNavigation.updateLayoutParams {
        height = (size - size * result.getFloat(BUNDLE_PROGRESS, 0F)).toInt()
      }
    })
  }

  override fun onDestroyView() {
    binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
    super.onDestroyView()
  }

  companion object {
    const val REQ_HOME = "req_home"
    const val BUNDLE_PROGRESS = "bundle_progress"
  }
}