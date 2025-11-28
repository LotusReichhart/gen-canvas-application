package com.lotusreichhart.gencanvas.core.domain.usecase.banner

import com.lotusreichhart.gencanvas.core.domain.repository.BannerRepository
import com.lotusreichhart.gencanvas.core.domain.repository.SettingRepository
import com.lotusreichhart.gencanvas.core.model.banner.Banner
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test

class GetListBannerUseCaseTest {

    private val bannerRepository = mockk<BannerRepository>(relaxed = true)
    private val settingRepository = mockk<SettingRepository>(relaxed = true)
    private lateinit var useCase: GetListBannerUseCase

    @Before
    fun setup() {
        useCase = GetListBannerUseCase(bannerRepository, settingRepository)
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

        TestCase.assertEquals(fakeBanners, resultList)
    }

    @Test
    fun `should CALL fetchBanners when cache is OLD`() = runTest {
        coEvery { settingRepository.getLastBannerRefreshTime() } returns 0L
        every { bannerRepository.getBannersStream() } returns flowOf(emptyList())

        useCase().collect()

        coVerify(exactly = 1) { bannerRepository.fetchBanners() }
    }

    @Test
    fun `should NOT call fetchBanners when cache is NEW`() = runTest {
        coEvery { settingRepository.getLastBannerRefreshTime() } returns System.currentTimeMillis()
        every { bannerRepository.getBannersStream() } returns flowOf(emptyList())

        useCase().collect()

        coVerify(exactly = 0) { bannerRepository.fetchBanners() }
    }
}