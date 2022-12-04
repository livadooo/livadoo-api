package com.livadoo.services.storage.controller

import com.livadoo.common.utils.extractContent
import com.livadoo.services.storage.data.Document
import com.livadoo.services.storage.services.StorageService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/v1/files")
class StorageController @Autowired constructor(
    private val storageService: StorageService
) {

    private val logger = LoggerFactory.getLogger(StorageController::class.java)

    @PostMapping
    suspend fun upload(@RequestPart("file") filePart: FilePart): String {
        logger.info("REST --> Uploading file")

        val (contentType, contentBytes) = filePart.extractContent()
        return storageService
            .uploadFile(filePart.filename(), contentType, contentBytes)
            .also { logger.info("REST --> File uploaded") }
    }

    @GetMapping("/{uuid}")
    suspend fun download(@PathVariable uuid: String): ResponseEntity<ByteArray> {
//        logger.info("REST --> Downloading file with UUID: $uuid")
        val document = storageService.download(uuid)
        val headers = buildDocumentResponse(document)

        return ResponseEntity.ok()
            .headers(headers)
            .body(document.content)
    }

    private val buildDocumentResponse: (document: Document) -> HttpHeaders = { document ->
        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType(document.contentType)
        val contentDisposition = ContentDisposition.builder("attachment")
            .filename(document.name)
            .build()
        headers.contentDisposition = contentDisposition
        headers
    }
}
