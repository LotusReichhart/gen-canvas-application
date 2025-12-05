package com.lotusreichhart.gencanvas.feature.editing.domain.factory

import com.lotusreichhart.gencanvas.feature.editing.domain.model.EditTool
import com.lotusreichhart.gencanvas.feature.editing.domain.model.CropStyle
import com.lotusreichhart.gencanvas.feature.editing.domain.model.EditorFeature
import com.lotusreichhart.gencanvas.feature.editing.domain.model.EditorTool
import com.lotusreichhart.gencanvas.feature.editing.domain.model.ToolStyle
import javax.inject.Inject

/**
 * Factory cung cấp cấu hình mặc định cho Editor.
 * Giúp ViewModel không phải hardcode danh sách.
 */
internal class EditorConfigFactory @Inject constructor() {

    fun getAvailableFeatures(): List<EditorFeature> {
        return listOf(
            EditorFeature.EDIT,
        )
    }

    fun getToolsForFeature(feature: EditorFeature): List<EditorTool> {
        return when (feature) {
            EditorFeature.EDIT -> EditTool.entries
            else -> emptyList()
        }
    }

    fun getStylesForTool(tool: EditorTool): List<ToolStyle> {
        return when (tool) {
            EditTool.CROP -> CropStyle.getAll()
            else -> emptyList()
        }
    }

    fun getDefaultTool(feature: EditorFeature): EditorTool? {
        return when (feature) {
            EditorFeature.EDIT -> EditTool.CROP
            else -> null
        }
    }

    fun getDefaultStyle(tool: EditorTool): ToolStyle? {
        return when (tool) {
            EditTool.CROP -> CropStyle.Free
            else -> null
        }
    }
}