package com.aloe.moment.basic

import androidx.fragment.app.Fragment
import com.aloe.moment.business.article.ArticleFrag
import com.aloe.moment.business.hierarchy.HierarchyChildFrag
import com.aloe.moment.business.hierarchy.HierarchyFrag
import com.aloe.moment.business.project.ProjectFrag
import com.aloe.moment.business.recommend.RecommendFrag
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import javax.inject.Named

@Module
@InstallIn(FragmentComponent::class)
object FragModule {
  @Provides
  @Named("HomeFrag")
  fun getFragments(
    frag1: RecommendFrag,
    frag2: HierarchyFrag,
    frag3: ProjectFrag,
    frag4: ArticleFrag
  ): MutableList<Fragment> = mutableListOf(frag1, frag2, frag3, frag4)

  @Provides
  @Named("HierarchyFrag")
  fun getNaviFrag(frag1: HierarchyChildFrag, frag2: HierarchyChildFrag): MutableList<Fragment> =
    mutableListOf(frag1, frag2)
}