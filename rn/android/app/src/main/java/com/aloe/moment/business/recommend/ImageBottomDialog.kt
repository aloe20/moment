package com.aloe.moment.business.recommend

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.aloe.moment.BuildConfig
import com.aloe.moment.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.joda.time.DateTime
import java.io.File

class ImageBottomDialog : BottomSheetDialogFragment() {
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val photoLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
      setFragmentResult(RecommendFrag.REQ_IMAGE, bundleOf("uri" to it))
    }
    val time = DateTime.now().toString("yyyyMMdd_HHmmss")
    val dir = requireContext().getExternalFilesDir(Environment.DIRECTORY_DCIM)
    val file = File(dir, "IMG_$time.jpg")
    val authority = "${BuildConfig.APPLICATION_ID}.provider"
    val uri = FileProvider.getUriForFile(requireContext(), authority, file)
    val launcher = registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
      this.takeIf { result }?.setFragmentResult(RecommendFrag.REQ_IMAGE, bundleOf("uri" to uri))
    }
    val goSetting = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
      val granted = ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
      if (granted == PackageManager.PERMISSION_GRANTED) {
        launcher.launch(uri)
      }
    }
    val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
      if (granted) {
        launcher.launch(uri)
      } else {
        val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
          .setData(Uri.fromParts("package", requireContext().packageName, null))
        goSetting.launch(intent)
      }
    }
    return ComposeView(requireContext()).apply {
      setContent {
        HomeBottomContentLayout(
          { requestPermission.launch(Manifest.permission.CAMERA) },
          { photoLauncher.launch("image/*") })
      }
    }
  }
}

@Composable
@Suppress("FunctionName")
fun HomeBottomContentLayout(takePictureClick: () -> Unit, photoAlbumClick: () -> Unit) {
  Column {
    Text(
      text = stringResource(id = R.string.take_pictures),
      modifier = Modifier.align(Alignment.CenterHorizontally)
        .clickable(onClick = takePictureClick).fillMaxWidth().padding(vertical = 16.dp),
      fontSize = 18.sp,
      textAlign = TextAlign.Center
    )
    Divider()
    Text(
      text = stringResource(id = R.string.photo_album),
      modifier = Modifier.align(Alignment.CenterHorizontally)
        .clickable(onClick = photoAlbumClick).fillMaxWidth().padding(vertical = 16.dp),
      fontSize = 18.sp,
      textAlign = TextAlign.Center
    )
  }
}