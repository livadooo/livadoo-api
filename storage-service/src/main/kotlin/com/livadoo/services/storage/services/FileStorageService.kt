package com.livadoo.services.storage.services

import com.livadoo.services.storage.config.LivadooProperties
import com.livadoo.services.storage.exceptions.DocumentFileNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Profile
import org.springframework.core.io.WritableResource
import org.springframework.stereotype.Service
import org.springframework.util.StreamUtils
import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

interface FileStorageService {

    fun upload(fileName: String, contentBytes: ByteArray): String

    fun download(uri: String): ByteArray
}

@Profile("default", "local")
@Service
class DefaultFileStorageService @Autowired constructor(
    properties: LivadooProperties
) : FileStorageService {
    private val logger = LoggerFactory.getLogger(DefaultFileStorageService::class.java)

    private val folder = Paths.get(properties.storage.path)

    override fun upload(fileName: String, contentBytes: ByteArray): String {
        checkAndCreateFolder()
        Files.copy(contentBytes.inputStream(), this.folder.resolve(fileName), StandardCopyOption.REPLACE_EXISTING)
        return "${folder.toAbsolutePath()}/$fileName"
    }

    override fun download(uri: String): ByteArray {
        val fileArray: ByteArray
        try {
            fileArray = File(uri).readBytes()
        } catch (e: FileNotFoundException) {
            logger.error("$uri not found. Current folder is : $folder")
            throw DocumentFileNotFoundException()
        }
        return fileArray
    }

    private fun checkAndCreateFolder() {
        if (!Files.exists(folder)) Files.createDirectory(folder)
    }
}

@Profile("prod", "uat")
@Service
class GoogleCloudFileStorageService @Autowired constructor(
    private val context: ApplicationContext,
    private val properties: LivadooProperties
) : FileStorageService {
    private val logger = LoggerFactory.getLogger(GoogleCloudFileStorageService::class.java)

    override fun upload(fileName: String, contentBytes: ByteArray): String {
        logger.info("Uploading file Google Storage")
        val uri = "gs://${properties.storage.googleCloud.bucket}/$fileName"
        val resource = context.getResource(uri) as WritableResource
        resource.outputStream.use { os -> os.write(contentBytes) }
        return resource.uri.toString().also {
            logger.info("File update to Google Storage")
        }
    }

    override fun download(uri: String): ByteArray {
        logger.info("Downloading file : $uri")
        val resource = context.getResource(uri) as WritableResource
        return StreamUtils.copyToByteArray(resource.inputStream).also {
            logger.info("File downloaded")
        }
    }
}
