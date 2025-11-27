package com.lotusreichhart.gencanvas.core.domain.usecase

import com.lotusreichhart.gencanvas.core.domain.repository.BannerRepository
import com.lotusreichhart.gencanvas.core.domain.repository.SettingsRepository
import com.lotusreichhart.gencanvas.core.domain.usecase.banner.GetListBannerUseCase
import com.lotusreichhart.gencanvas.core.model.banner.Banner
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class GetListBannerUseCaseTest {

    private val bannerRepository = mockk<BannerRepository>(relaxed = true)
    private val settingsRepository = mockk<SettingsRepository>(relaxed = true)
    private lateinit var useCase: GetListBannerUseCase

    @Before
    fun setup() {
        useCase = GetListBannerUseCase(bannerRepository, settingsRepository)
    }

    @Test
    fun `invoke should return banners from repository`() = runTest {
        val fakeBanners = listOf(
            Banner(
                id = 1,
                title = "Test",
                imageUrl = "",
                actionUrl = null,
                displayOrder = 1
            )
        )
        every { bannerRepository.getBannersStream() } returns flowOf(fakeBanners)

        val resultFlow = useCase()
        val resultList = resultFlow.first()

        assertEquals(fakeBanners, resultList)
    }

    @Test
    fun `should CALL fetchBanners when cache is OLD`() = runTest {
        coEvery { settingsRepository.getLastBannerRefreshTime() } returns 0L
        every { bannerRepository.getBannersStream() } returns flowOf(emptyList())

        useCase().collect()

        coVerify(exactly = 1) { bannerRepository.fetchBanners() }
    }

    @Test
    fun `should NOT call fetchBanners when cache is NEW`() = runTest {
        coEvery { settingsRepository.getLastBannerRefreshTime() } returns System.currentTimeMillis()
        every { bannerRepository.getBannersStream() } returns flowOf(emptyList())

        useCase().collect()

        coVerify(exactly = 0) { bannerRepository.fetchBanners() }
    }
}