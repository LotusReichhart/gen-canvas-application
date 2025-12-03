package com.lotusreichhart.gencanvas.feature.home.presentation

import com.lotusreichhart.gencanvas.core.model.banner.Banner
import com.lotusreichhart.gencanvas.feature.home.presentation.model.GridItemData
import com.lotusreichhart.gencanvas.feature.home.presentation.model.HorizontalItemData
import com.lotusreichhart.gencanvas.feature.home.presentation.model.MainListItemData

internal data class HomeTabUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val banners: List<Banner> = emptyList(),
    val error: String? = null,

    val gridItems: List<GridItemData> = emptyList(),
    val horizontalListItems: List<HorizontalItemData> = emptyList(),
    val mainListItems: List<MainListItemData> = emptyList()
)
