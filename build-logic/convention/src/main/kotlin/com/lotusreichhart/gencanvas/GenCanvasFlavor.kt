package com.lotusreichhart.gencanvas

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

@Suppress("EnumEntryName")
enum class FlavorDimension {
    environment
}

@Suppress("EnumEntryName")
enum class GenCanvasFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    dev(FlavorDimension.environment, applicationIdSuffix = ".dev"),
    prod(FlavorDimension.environment)
}

fun configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: GenCanvasFlavor) -> Unit = {},
) {
    commonExtension.apply {
       FlavorDimension.values().forEach { flavorDimension ->
            flavorDimensions += flavorDimension.name
        }

        productFlavors {
            GenCanvasFlavor.values().forEach { genCanvasFlavor ->
                register(genCanvasFlavor.name) {
                    dimension = genCanvasFlavor.dimension.name
                    flavorConfigurationBlock(this, genCanvasFlavor)
                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (genCanvasFlavor.applicationIdSuffix != null) {
                            applicationIdSuffix = genCanvasFlavor.applicationIdSuffix
                        }
                    }
                }
            }
        }
    }
}