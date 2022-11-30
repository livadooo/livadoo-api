package com.livadoo.services.storage.services

import com.livadoo.services.storage.data.Document


interface StorageService {

    suspend fun upload(fileName: String, contentType: String, contentBytes: ByteArray): String

    suspend fun download(uuid: String): Document
}
