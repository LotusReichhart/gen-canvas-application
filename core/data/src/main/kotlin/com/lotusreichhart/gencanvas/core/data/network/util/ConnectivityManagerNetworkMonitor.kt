package com.lotusreichhart.gencanvas.core.data.network.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService
import com.lotusreichhart.gencanvas.core.domain.util.NetworkMonitor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectivityManagerNetworkMonitor @Inject constructor(
    @param:ApplicationContext private val context: Context
) : NetworkMonitor {

    private val connectivityManager = context.getSystemService<ConnectivityManager>()

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    @SuppressLint("MissingPermission")
    override val isOnline: Flow<Boolean> = callbackFlow {

        val networkCallback = object : ConnectivityManager.NetworkCallback() {

            /**
             * Được gọi khi hệ thống tìm thấy một mạng
             * đáp ứng các yêu cầu trong [NetworkRequest].
             *
             * Tác dụng: Phát (emit) 'true' để báo cho ứng dụng biết
             * "Đã kết nối mạng".
             */
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            /**
             * Được gọi khi một mạng (mà trước đó 'onAvailable' đã báo)
             * bị mất kết nối.
             *
             * Tác dụng: Phát (emit) 'false' để báo cho ứng dụng biết
             * "Đã mất kết nối mạng".
             */
            override fun onLost(network: Network) {
                trySend(false)
            }

            /**
             * Được gọi khi hệ thống không thể tìm thấy mạng
             * để đáp ứng yêu cầu (kể cả khi onLost không được gọi).
             *
             * Tác dụng: Đây là một cơ chế an toàn (safety net)
             * để đảm bảo gửi 'false' khi app khởi động
             * mà không tìm thấy bất kỳ mạng nào.
             */
            override fun onUnavailable() {
                trySend(false)
            }

            /**
             * Được gọi khi các thuộc tính (capabilities) của mạng thay đổi
             * (ví dụ: chuyển từ 4G sang 5G, hoặc mạng "không đo lường" (unmetered)
             * chuyển thành "có đo lường" (metered)).
             *
             * Tác dụng: Hiện tại trống hàm này vì logic của hệ thống
             * rất đơn giản (chỉ cần biết 'true'/'false').
             * 'onAvailable' và 'onLost' đã xử lý việc CÓ/MẤT
             * kết nối Internet, nên không cần xử lý thêm ở đây.
             */
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {

            }
        }

        // Kiểm tra trạng thái ban đầu khi Flow bắt đầu
        val initialNetwork = connectivityManager?.activeNetwork
        val initialCapabilities = connectivityManager?.getNetworkCapabilities(initialNetwork)
        val isInitiallyOnline =
            initialCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        trySend(isInitiallyOnline)

        // Xây dựng yêu cầu: "Chỉ quan tâm đến mạng CÓ INTERNET"
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        // Đăng ký callback để lắng nghe thay đổi
        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)

        // Khi Flow bị hủy (ví dụ: ViewModel bị hủy),
        // gỡ đăng ký callback để tránh rò rỉ bộ nhớ
        awaitClose {
            connectivityManager?.unregisterNetworkCallback(networkCallback)
        }
    }.conflate() // Chỉ lấy giá trị mới nhất nếu flow bị tắc nghẽn
        .distinctUntilChanged() // Chỉ phát giá trị nếu nó khác với giá trị trước đó (true -> true sẽ bị bỏ qua)
}