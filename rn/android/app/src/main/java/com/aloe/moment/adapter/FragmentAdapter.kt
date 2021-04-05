package com.aloe.moment.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter<T : Fragment>(fragment: Fragment, val list: List<T>) : FragmentStateAdapter(fragment) {
  override fun getItemCount(): Int = list.size
  override fun createFragment(position: Int): Fragment = list[position]
}