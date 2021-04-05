package com.aloe.moment.business.article.search

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.aloe.bean.ClassifyBean
import com.aloe.moment.R
import com.aloe.moment.basic.BasicFrag
import com.aloe.moment.databinding.FragArticleHotkeyBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFrag : BasicFrag<SearchVm, FragArticleHotkeyBinding>(), View.OnClickListener {
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.inputLayout.hint = "在${vm.bean?.name ?: ""}公众号中搜索内容"
    binding.inputLayout.setEndIconOnClickListener {
      onClick(binding.inputLayout)
    }
    vm.getHotKeyLiveData().observe(viewLifecycleOwner) {
      binding.groupHotkey.removeAllViews()
      it?.forEach {
        binding.groupHotkey.addView(
          (layoutInflater.inflate(R.layout.item_child_chip, binding.groupHotkey, false) as Chip).apply {
            setChipBackgroundColorResource(R.color.color_divider)
            setOnClickListener(this@SearchFrag)
            text = it.name
          })
      }
    }
  }

  override suspend fun loadData() = vm.loadHotKeyData()

  override fun onClick(v: View) {
    var keyword: String? = null
    if (v == binding.inputLayout) {
      keyword = binding.editText.text?.toString()
    } else if (v is Chip) {
      keyword = v.text.toString()
    }
    vm.bean?.takeUnless { keyword.isNullOrEmpty() }?.also {
      findNavController().navigate(R.id.frag_article_child, bundleOf("data" to ClassifyBean(it.id, it.name, keyword)))
    }
  }
}