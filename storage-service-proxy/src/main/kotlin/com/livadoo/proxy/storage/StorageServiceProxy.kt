package com.livadoo.proxy.storage

interface StorageServiceProxy {

    suspend fun uploadFile(fileName: String, contentType: String, contentBytes: ByteArray): String
}