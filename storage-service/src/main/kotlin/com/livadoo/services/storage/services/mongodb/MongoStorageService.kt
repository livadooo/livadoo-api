package com.livadoo.services.storage.services.mongodb

import com.livadoo.services.storage.config.LivadooProperties
import com.livadoo.services.storage.data.Document
import com.livadoo.services.storage.exceptions.DocumentNotFoundException
import com.livadoo.services.storage.services.FileStorageService
import com.livadoo.services.storage.services.StorageService
import com.livadoo.services.storage.services.mongodb.entity.DocumentEntity
import com.livadoo.services.storage.services.mongodb.entity.toDto
import com.livadoo.services.storage.services.mongodb.repository.DocumentRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import javax.annotation.PostConstruct

@Profile("default", "local")
@Service
class MongoStorageService @Autowired constructor(
    private val documentRepository: DocumentRepository,
    private val fileStorageService: FileStorageService,
    private val properties: LivadooProperties,
) : StorageService {

    private val logger = LoggerFactory.getLogger(MongoStorageService::class.java)
    override suspend fun uploadProductImage(fileName: String, contentType: String, contentBytes: ByteArray): String {
        return uploadFile("products", fileName, contentType, contentBytes)
    }

    override suspend fun uploadProfilePortrait(fileName: String, contentType: String, contentBytes: ByteArray): String {
        return uploadFile("profile-portraits", fileName, contentType, contentBytes)
    }

    private suspend fun uploadFile(directory: String, fileName: String, contentType: String, contentBytes: ByteArray): String {
        logger.info("Uploading file")
        
        val uuid = "${UUID.randomUUID()}"
        val extension = fileName.split(".").last()
        val name = "$uuid.$extension"
        val uri = fileStorageService.upload(name, contentBytes, directory)
        val entity = DocumentEntity(uuid, uri, fileName, contentType, Instant.now())
        documentRepository.save(entity).awaitFirstOrNull()
        
        return "${properties.storage.local.baseUrl}/$uuid"
    }

    override suspend fun download(uuid: String): Document {
        val documentEntity = documentRepository.findByUuid(uuid)
            .awaitFirstOrNull() ?: throw DocumentNotFoundException(uuid)

        return fileStorageService.download(documentEntity.uri).let { bytes ->
            documentEntity.toDto().apply { content = bytes }
        }
    }

    @PostConstruct
    fun patchDocuments() {
        val (_, _, oldBucket, newBucket, patchBucket) = properties.storage.local
        if (!patchBucket) {
            logger.info("Documents patch not activated")
            return
        }

        logger.info("Documents patch activated - Starting patching")
        val updateDocumentsFlux = documentRepository.findByUriContains(oldBucket)
            .map { document ->
                val newUri = document.uri.replace(oldBucket, newBucket)
                document.apply { uri = newUri }
            }
        documentRepository.saveAll(updateDocumentsFlux)
            .subscribe()
        logger.info("Patching completed")
    }
}
