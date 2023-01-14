package com.livadoo.services.storage.proxy

import com.livadoo.proxy.storage.StorageServiceProxy
import com.livadoo.services.storage.services.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StorageServiceProxyImpl @Autowired constructor(
    private val storageService: StorageService
) : StorageServiceProxy {

    override suspend fun uploadProductImage(fileName: String, contentType: String, contentBytes: ByteArray): String {
        return storageService.uploadProductImage(fileName, contentType, contentBytes)
    }

    override suspend fun uploadProfilePortrait(fileName: String, contentType: String, contentBytes: ByteArray): String {
        return storageService.uploadProfilePortrait(fileName, contentType, contentBytes)
    }
}