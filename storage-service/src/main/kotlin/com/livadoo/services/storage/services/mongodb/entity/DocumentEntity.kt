package com.livadoo.services.storage.services.mongodb.entity

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import com.livadoo.services.storage.data.Document as LivadooDocument

@Document(collection = "documents")
data class DocumentEntity(
    var uuid: String,
    var uri: String,
    var name: String,
    var contentType: String,
    var createdAt: Instant,
    @Id
    var id: String? = null,
    @Version
    var version: Int = 0
) {

}

fun DocumentEntity.toDto() = LivadooDocument(uuid, uri, name, contentType, createdAt, id)