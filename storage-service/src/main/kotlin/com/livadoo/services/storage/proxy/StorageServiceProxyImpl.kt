package com.livadoo.services.storage.proxy

import com.livadoo.proxy.storage.StorageServiceProxy
import com.livadoo.services.storage.services.StorageService
import org.springframework.stereotype.Service

@Service
class StorageServiceProxyImpl(
    private val storageService: StorageService
) : StorageServiceProxy {

    override suspend fun uploadProductImage(fileName: String, contentType: String, contentBytes: ByteArray): String {
        return storageService.uploadProductImage(fileName, contentType, contentBytes)
    }

    override suspend fun uploadCategoryImage(fileName: String, contentType: String, contentBytes: ByteArray): String {
        return storageService.uploadCategoryImage(fileName, contentType, contentBytes)
    }

    override suspend fun uploadProfilePortrait(fileName: String, contentType: String, contentBytes: ByteArray): String {
        return storageService.uploadProfilePortrait(fileName, contentType, contentBytes)
    }
}