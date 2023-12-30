package com.livadoo.services.storage.services

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.livadoo.services.storage.config.LivadooProperties
import com.livadoo.services.storage.data.Document
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.util.*

@Profile("prod", "uat")
@Service
class GoogleCloudFileStorageService(
    private val properties: LivadooProperties,
    private val storage: Storage,
) : StorageService {
    private val logger = LoggerFactory.getLogger(GoogleCloudFileStorageService::class.java)

    override suspend fun uploadProductImage(fileName: String, contentType: String, contentBytes: ByteArray): String {
        logger.info("Uploading product image to Google Cloud Storage")

        return upload("products", fileName, contentType, contentBytes)
            .also { logger.info("Product image successfully uploaded to Google Storage") }
    }

    override suspend fun uploadCategoryImage(fileName: String, contentType: String, contentBytes: ByteArray): String {
        logger.info("Uploading category image to Google Cloud Storage")

        return upload("categories", fileName, contentType, contentBytes)
            .also { logger.info("category image successfully uploaded to Google Storage") }
    }

    override suspend fun uploadProfilePortrait(fileName: String, contentType: String, contentBytes: ByteArray): String {
        logger.info("Uploading profile portrait to local storage")

        return upload("profile-portraits", fileName, contentType, contentBytes)
            .also { logger.info("Profile portrait successfully uploaded to Google Storage") }
    }

    override suspend fun download(uuid: String): Document {
        TODO("Not yet implemented")
    }

    private suspend fun upload(
        subFolder: String,
        fileName: String,
        contentType: String,
        contentBytes: ByteArray,
    ): String {
        val bucket = properties.storage.googleCloud.publicBucket

        val extension = fileName.split(".").last()
        val name = "${UUID.randomUUID()}.$extension"
        val imageRelativePath = "$subFolder/$name"

        val imageFullUrl = "https://$bucket/$imageRelativePath"

        val blobId = BlobId.of(bucket, imageRelativePath)
        val blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType(contentType)
            .build()

        return storage
            .create(blobInfo, contentBytes)
            .let {
                imageFullUrl
            }
    }

}
