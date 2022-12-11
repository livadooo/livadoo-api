package com.livadoo.services.storage.exceptions

import com.livadoo.common.exceptions.ErrorCodes
import com.livadoo.common.exceptions.ObjectNotFoundException

class DocumentNotFoundException(
    uuid: String
) : ObjectNotFoundException(
    title = "Document not found",
    detail = "Could not find document with id: $uuid",
    statusCode = ErrorCodes.DOCUMENT_NOT_FOUND
)