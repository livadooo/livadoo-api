package com.livadoo.services.storage.services

import com.livadoo.services.storage.data.Document


interface StorageService {

    suspend fun uploadProductImage(fileName: String, contentType: String, contentBytes: ByteArray): String

    suspend fun uploadCategoryImage(fileName: String, contentType: String, contentBytes: ByteArray): String

    suspend fun uploadProfilePortrait(fileName: String, contentType: String, contentBytes: ByteArray): String

    suspend fun download(uuid: String): Document
}
