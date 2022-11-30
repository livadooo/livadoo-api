package com.livadoo.services.storage.exceptions

import com.livadoo.common.exceptions.ObjectNotFoundException
import org.springframework.data.annotation.Immutable

@Immutable
class DocumentFileNotFoundException : ObjectNotFoundException(
    "Document file not found",
    "File associated with the given UUID not found. Maybe it has been deleted."
)