/*
 * Copyright 2023 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.aloe.moment.flu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import io.flutter.embedding.android.ExclusiveAppComponent
import io.flutter.embedding.android.FlutterImageView
import io.flutter.embedding.android.FlutterSurfaceView
import io.flutter.embedding.android.FlutterTextureView
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineGroup
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.platform.PlatformPlugin

@Composable
fun FlutterLayout() {
    AndroidView(factory = { FluView(it) })
}

class FluView : FlutterView, DefaultLifecycleObserver, ExclusiveAppComponent<Activity> {
    private lateinit var flutterEngine: FlutterEngine
    private var platformPlugin: PlatformPlugin? = null

    constructor(context: Context) : super(context) {
        initEngine(context)
    }

    constructor(context: Context, flutterSurfaceView: FlutterSurfaceView) : super(context, flutterSurfaceView) {
        initEngine(context)
    }

    constructor(context: Context, flutterTextureView: FlutterTextureView) : super(context, flutterTextureView) {
        initEngine(context)
    }

    constructor(context: Context, flutterImageView: FlutterImageView) : super(context, flutterImageView) {
        initEngine(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initEngine(context)
    }

    private fun initEngine(context: Context) {
        flutterEngine = feGroup.createAndRunEngine(context.applicationContext, DartExecutor.DartEntrypoint.createDefault(), "/")
        flutterEngine.plugins.add(ResPlugin())
        ActivityCompat.setPermissionCompatDelegate(object : ActivityCompat.PermissionCompatDelegate {
            override fun requestPermissions(activity: Activity, permissions: Array<out String>, requestCode: Int): Boolean {
                return false
            }

            override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, data: Intent?): Boolean {
                return flutterEngine.activityControlSurface.onActivityResult(requestCode, resultCode, data)
            }
        })
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        (context as? Activity)?.let { activity ->
            platformPlugin = PlatformPlugin(activity, flutterEngine.platformChannel)
            findViewTreeLifecycleOwner()?.lifecycle?.let { lifecycle ->
                flutterEngine.activityControlSurface.attachToActivity(this, lifecycle)
                attachToFlutterEngine(flutterEngine)
                lifecycle.addObserver(this)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unhookActivityAndView()
    }

    override fun onResume(owner: LifecycleOwner) {
        flutterEngine.lifecycleChannel.appIsResumed()
        platformPlugin?.updateSystemUiOverlays()
    }

    override fun onPause(owner: LifecycleOwner) {
        flutterEngine.lifecycleChannel.appIsInactive()
    }

    override fun onStop(owner: LifecycleOwner) {
        flutterEngine.lifecycleChannel.appIsPaused()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        unhookActivityAndView()
    }

    override fun getAppComponent(): Activity = context as Activity

    private fun unhookActivityAndView() {
        findViewTreeLifecycleOwner()?.lifecycle?.removeObserver(this)
        flutterEngine.activityControlSurface.detachFromActivity()
        platformPlugin?.destroy()
        platformPlugin = null
        flutterEngine.lifecycleChannel.appIsDetached()
        detachFromFlutterEngine()
    }

    companion object {
        private lateinit var feGroup: FlutterEngineGroup
        fun initEngineGroup(context: Context) {
            feGroup = FlutterEngineGroup(context)
        }

        /*suspend fun getFluText(name: String): String {
          return withContext(Dispatchers.IO) {
            val result = FlutterInjector.instance().flutterLoader().getLookupKeyForAsset(name)
            val stream = context.assets.open(result)
            InputStreamReader(stream).readText()
          }
        }*/
    }
}
