package com.livadoo.services.storage.exceptions

import com.livadoo.common.exceptions.ObjectNotFoundException
import org.springframework.data.annotation.Immutable

@Immutable
class DocumentNotFoundException(uuid: String) : ObjectNotFoundException(
    "Document not found",
    "No document found with uuid $uuid"
)