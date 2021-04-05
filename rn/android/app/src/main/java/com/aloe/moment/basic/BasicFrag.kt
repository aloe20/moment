package com.aloe.moment.basic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@Suppress("UNCHECKED_CAST")
open class BasicFrag<Vm : BasicVm, Vb : ViewBinding> : Fragment() {
  private val types: Array<Type> = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
  protected lateinit var vm: Vm
  protected lateinit var binding: Vb
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    createVm()
    lifecycleScope.launchWhenResumed { loadData() }
  }

  protected open fun createVm() {
    vm = createViewModelLazy((types[0] as Class<Vm>).kotlin, { viewModelStore }).value
    vm.initOwner(this)
  }

  protected open suspend fun loadData() = Unit

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    if (ViewBinding::class.java.name != (types[1] as Class<*>).name) try {
      binding = (types[1] as Class<Vb>).getMethod("inflate", LayoutInflater::class.java)(null, inflater) as Vb
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return if (this::binding.isInitialized) binding.root
    else super.onCreateView(inflater, container, savedInstanceState)
  }
}