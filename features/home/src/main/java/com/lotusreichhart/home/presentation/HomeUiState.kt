package com.lotusreichhart.home.presentation

import com.lotusreichhart.domain.entity.BannerEntity
import com.lotusreichhart.domain.entity.UserEntity

data class GridItemData(val id: Int, val isLarge: Boolean)
data class HorizontalItemData(val id: Int)
data class MainListItemData(val id: Int)

data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val banners: List<BannerEntity> = emptyList(),
    val error: String? = null,
    val isOffline: Boolean = false,

    val userEntity: UserEntity? = null,

    val gridItems: List<GridItemData> = emptyList(),
    val horizontalListItems: List<HorizontalItemData> = emptyList(),
    val mainListItems: List<MainListItemData> = emptyList()
)