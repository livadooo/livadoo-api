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
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import javax.annotation.PostConstruct

@Service
class MongoStorageService @Autowired constructor(
    private val documentRepository: DocumentRepository,
    private val fileStorageService: FileStorageService,
    private val livadooProperties: LivadooProperties
) : StorageService {

    private val logger = LoggerFactory.getLogger(MongoStorageService::class.java)

    override suspend fun uploadFile(fileName: String, contentType: String, contentBytes: ByteArray): String {
        logger.info("Uploading file")
        
        val uuid = "${UUID.randomUUID()}"
        val extension = fileName.split(".").last()
        val name = "$uuid.$extension"
        val uri = fileStorageService.upload(name, contentBytes)
        val entity = DocumentEntity(uuid, uri, fileName, contentType, Instant.now())
        documentRepository.save(entity).awaitFirstOrNull()
        
        return uuid
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
        val (bucket, oldBucket, patchBucket) = livadooProperties.storage.googleCloud
        if (!patchBucket) {
            logger.info("Documents patch not activated")
            return
        }

        logger.info("Documents patch activated - Starting patching")
        val updateDocumentsFlux = documentRepository.findByUriContains(oldBucket)
            .map { document ->
                val newUri = document.uri.replace(oldBucket, bucket)
                document.apply { uri = newUri }
            }
        documentRepository.saveAll(updateDocumentsFlux)
            .subscribe()
        logger.info("Patching completed")
    }
}
