package com.livadoo.services.storage.proxy

import com.livadoo.proxy.storage.StorageServiceProxy
import com.livadoo.services.storage.services.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StorageServiceProxyImpl @Autowired constructor(
    private val storageService: StorageService
) : StorageServiceProxy {

    override suspend fun uploadFile(fileName: String, contentType: String, contentBytes: ByteArray): String {
        return storageService.upload(fileName, contentType, contentBytes)
    }
}