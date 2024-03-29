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

package com.aloe.moment.app

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.startup.Initializer
import com.aloe.basic.log
import com.aloe.moment.page.flu.FluView
import com.aloe.moment.page.react.ReactView

class AppInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        FluView.initEngineGroup(context)
        ReactView.initRn(context)
        ProcessLifecycleOwner.get().lifecycle.addObserver(object :DefaultLifecycleObserver{
            override fun onStart(owner: LifecycleOwner) {
                "app on start".log()
            }

            override fun onStop(owner: LifecycleOwner) {
                "app on stop".log()
            }
        })
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> = mutableListOf()
}
