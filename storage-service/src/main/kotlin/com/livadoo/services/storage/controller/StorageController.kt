package com.livadoo.services.storage.controller

import com.livadoo.services.common.utils.extractContent
import com.livadoo.services.storage.extension.extractHeaders
import com.livadoo.services.storage.services.StorageService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/files")
class StorageController(
    private val storageService: StorageService
) {

    private val logger = LoggerFactory.getLogger(StorageController::class.java)

    @PostMapping("/profile-portraits")
    suspend fun uploadProfileImage(@RequestPart("file") filePart: FilePart): String {
        val (contentType, contentBytes) = filePart.extractContent()

        return storageService.uploadProfilePortrait(filePart.filename(), contentType, contentBytes)
    }

    @PostMapping("/products")
    suspend fun uploadProductImage(@RequestPart("file") filePart: FilePart): String {
        val (contentType, contentBytes) = filePart.extractContent()

        return storageService.uploadProductImage(filePart.filename(), contentType, contentBytes)
    }

    @GetMapping("/{uuid}")
    suspend fun download(@PathVariable uuid: String): ResponseEntity<ByteArray> {
        logger.info("REST --> Downloading file with uuid: $uuid")
        val document = storageService.download(uuid)
        val headers = document.extractHeaders()

        return ResponseEntity.ok()
            .headers(headers)
            .body(document.content)
    }
}
