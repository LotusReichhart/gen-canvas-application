package com.lotusreichhart.gencanvas.core.domain.repository

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow

interface EditingRepository {
    val currentImageUri: StateFlow<Uri?>

    /** Trạng thái nút Undo (có thể bấm được không?) */
    val canUndo: StateFlow<Boolean>

    /** Trạng thái nút Redo (có thể bấm được không?) */
    val canRedo: StateFlow<Boolean>

    /**
     * Khởi tạo phiên làm việc mới hoặc tiếp tục phiên cũ.
     * @param uri URI của ảnh gốc hoặc ảnh đang sửa dở.
     * Logic: Nếu uri này khác với currentImageUri -> Reset session. Ngược lại -> Resume.
     */
    fun initializeSession(uri: Uri)

    /**
     * Cập nhật ảnh mới sau khi một thao tác chỉnh sửa hoàn tất (Cắt, Xoay...).
     * @param newUri URI của file ảnh mới vừa được tạo ra.
     */
    fun updateImage(newUri: Uri)

    /** Quay lại bước trước */
    fun undo()

    /** Đi tới bước sau (nếu đã undo) */
    fun redo()

    /**
     * Dọn dẹp phiên làm việc.
     * - Xóa toàn bộ file tạm trong bộ nhớ đệm (Cache).
     * - Reset các Stack.
     * Gọi hàm này khi User thoát Editor, Lưu thành công, hoặc Hủy bỏ.
     */
    fun clearSession()
}