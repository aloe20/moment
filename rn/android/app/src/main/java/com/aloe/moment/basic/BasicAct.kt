package com.aloe.moment.basic

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.aloe.moment.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BasicAct : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(getLayout())
    if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) != Configuration.UI_MODE_NIGHT_YES) {
      //深色模式
      if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
        window.decorView.windowInsetsController?.setSystemBarsAppearance(
          WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
          WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
      } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
      }
    }
  }

  private fun getLayout(): FrameLayout = FrameLayout(this).apply {
    id = R.id.nav_host
    val frag = NavHostFragment.create(R.navigation.nav_graph)
    supportFragmentManager.beginTransaction().setReorderingAllowed(true).replace(id, frag)
      .setPrimaryNavigationFragment(frag).commitNowAllowingStateLoss()
  }
}