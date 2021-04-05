package com.aloe.moment.business.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.aloe.moment.R
import com.aloe.moment.basic.BasicFrag
import com.google.accompanist.coil.CoilImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFrag : BasicFrag<SplashVm, ViewBinding>() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return ComposeView(requireContext()).apply {
      setContent { SplashLayout(vm) {
        //findNavController().navigate(R.id.action_frag_splash_to_frag_home)
        vm.sendUdpData()
      } }
    }
  }
}

@Suppress("FunctionName")
@Composable
fun SplashLayout(viewModel: SplashVm = viewModel(), click: () -> Unit) {
  val url by viewModel.getImage().observeAsState()
  Box(Modifier.fillMaxWidth().fillMaxHeight()) {
    CoilImage(
      data = url ?: "",
      contentDescription = null,
      modifier = Modifier.matchParentSize(),
      contentScale = ContentScale.Crop
    )
    Text(
      stringResource(R.string.skip),
      Modifier.clickable(onClick = click).clip(RoundedCornerShape(topStart = 8.dp)).background(Color(0x33000000))
        .padding(horizontal = 16.dp, vertical = 8.dp).align(Alignment.BottomEnd),
      color = Color.White
    )
  }
}