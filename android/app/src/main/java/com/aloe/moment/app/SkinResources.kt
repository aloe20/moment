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

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class SkinResources(private val resources: Resources, private val pm: PackageManager) :
    Resources(resources.assets, resources.displayMetrics, resources.configuration) {
    private var skinResources: Resources = resources
    private val liveData = MutableLiveData<String>()
    fun setSkinPath(path: String) = apply {
        pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)?.applicationInfo?.also {
            skinResources = pm.getResourcesForApplication(it)
            liveData.postValue("")
        }
    }

    override fun getDrawableForDensity(id: Int, density: Int, theme: Theme?): Drawable? {
        return getSkinId(id).let {
            if (it == 0) super.getDrawableForDensity(id, density, theme)
            else skinResources.getDrawableForDensity(it, density, theme)
        }
    }

    override fun getColorStateList(id: Int, theme: Theme?): ColorStateList = getSkinId(id).let {
        if (it == 0) super.getColorStateList(id, theme)
        else skinResources.getColorStateList(it, theme)
    }

    override fun getColor(id: Int, theme: Theme?): Int = getSkinId(id).let {
        if (it == 0) super.getColor(id, theme) else skinResources.getColor(it, theme)
    }

    @SuppressLint("DiscouragedApi")
    private fun getSkinId(id: Int): Int =
        skinResources.getIdentifier(resources.getResourceEntryName(id), resources.getResourceTypeName(id), "com.aloe.skin")

    fun addLifecycle(owner: LifecycleOwner, observer: Observer<String>) {
        liveData.observe(owner, observer)
    }
}