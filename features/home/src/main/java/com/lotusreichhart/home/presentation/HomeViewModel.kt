package com.lotusreichhart.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lotusreichhart.domain.entities.BannerEntity
import com.lotusreichhart.domain.use_cases.banner.GetListBannerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import com.lotusreichhart.home.R

@HiltViewModel
class HomeViewModel @Inject constructor(
    getListBannerUseCase: GetListBannerUseCase
) : ViewModel() {

    private val defaultBanners = listOf(
        BannerEntity(
            id = -1,
            title = "",
            imageUrl = R.drawable.default_banner_1.toString(),
            actionUrl = null,
            displayOrder = 1
        ),
        BannerEntity(
            id = -2,
            title = "",
            imageUrl = R.drawable.default_banner_2.toString(),
            actionUrl = null,
            displayOrder = 2
        ),
        BannerEntity(
            id = -3,
            title = "",
            imageUrl = R.drawable.default_banner_3.toString(),
            actionUrl = null,
            displayOrder = 3
        )
    )

    private val fakeGridItems = listOf(
        GridItemData(1, isLarge = true),
        GridItemData(2, isLarge = true),
        GridItemData(3, isLarge = false),
        GridItemData(4, isLarge = false),
        GridItemData(5, isLarge = false),
        GridItemData(6, isLarge = false)
    )
    private val fakeHorizontalListItems = (1..10).map { HorizontalItemData(it) }
    private val fakeMainListItems = (1..20).map { MainListItemData(it) }

    val uiState: StateFlow<HomeUiState> = getListBannerUseCase()
        .map { banners ->
            val bannersToDisplay = banners.ifEmpty {
                defaultBanners
            }
            HomeUiState(
                isLoading = false,
                banners = bannersToDisplay,
                gridItems = fakeGridItems,
                horizontalListItems = fakeHorizontalListItems,
                mainListItems = fakeMainListItems
            )
        }
        .catch { exception ->
            Log.e("HomeViewModel", "Lỗi khi lấy banner", exception)
            emit(
                HomeUiState(
                    isLoading = false,
                    banners = defaultBanners,
                    error = exception.localizedMessage,
                    gridItems = fakeGridItems,
                    horizontalListItems = fakeHorizontalListItems,
                    mainListItems = fakeMainListItems
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeUiState(
                isLoading = true,
                banners = defaultBanners,
                gridItems = fakeGridItems,
                horizontalListItems = fakeHorizontalListItems,
                mainListItems = fakeMainListItems
            )
        )
}