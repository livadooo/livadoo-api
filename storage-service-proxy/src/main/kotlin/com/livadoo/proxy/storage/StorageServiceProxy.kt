package com.livadoo.proxy.storage

interface StorageServiceProxy {

    suspend fun uploadProductImage(fileName: String, contentType: String, contentBytes: ByteArray): String

    suspend fun uploadCategoryImage(fileName: String, contentType: String, contentBytes: ByteArray): String

    suspend fun uploadProfilePortrait(fileName: String, contentType: String, contentBytes: ByteArray): String
}