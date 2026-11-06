package com.nuvio.app.features.multiview

import com.nuvio.app.core.storage.DesktopStorage
import com.nuvio.app.core.storage.ProfileScopedKey

actual object MultiViewerLayoutStorage {
    private val store = DesktopStorage.store("nuvio_multiviewer_layout")
    private const val payloadKey = "multiviewer_layout_payload"

    actual fun loadPayload(): String? =
        store.getString(ProfileScopedKey.of(payloadKey))

    actual fun savePayload(payload: String) {
        store.putString(ProfileScopedKey.of(payloadKey), payload)
    }
}
