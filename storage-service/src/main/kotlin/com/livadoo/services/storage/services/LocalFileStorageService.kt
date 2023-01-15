package com.livadoo.services.storage.services

import com.livadoo.services.storage.config.LivadooProperties
import com.livadoo.services.storage.exceptions.FileNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.io.FileNotFoundException as MissingFileException

interface FileStorageService {

    fun upload(fileName: String, contentBytes: ByteArray, directory: String): String

    fun download(uri: String): ByteArray
}

@Service
class LocalFileStorageService @Autowired constructor(
    properties: LivadooProperties
) : FileStorageService {
    private val logger = LoggerFactory.getLogger(LocalFileStorageService::class.java)

    private val folder = Paths.get(properties.storage.local.path)

    override fun upload(fileName: String, contentBytes: ByteArray, directory: String): String {
        val path = folder.resolve(directory)
        checkFolder(path)
        Files.copy(contentBytes.inputStream(), path.resolve(fileName), StandardCopyOption.REPLACE_EXISTING)
        return "${path.toAbsolutePath()}/$fileName"
    }

    override fun download(uri: String): ByteArray {
        val fileArray: ByteArray
        try {
            fileArray = File(uri).readBytes()
        } catch (ex: MissingFileException) {
            logger.error("$uri not found. Current folder is : $folder")
            throw FileNotFoundException(uri)
        }
        return fileArray
    }

    private fun checkFolder(path: Path) {
        if (!Files.exists(path)) Files.createDirectory(path)
    }
}