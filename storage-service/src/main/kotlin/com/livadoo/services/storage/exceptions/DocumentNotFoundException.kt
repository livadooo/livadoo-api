package com.livadoo.services.storage.exceptions

import com.livadoo.utils.exception.ErrorCodes
import com.livadoo.utils.exception.NotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.DOCUMENT_NOT_FOUND)
class DocumentNotFoundException(
    @ResponseErrorProperty val uuid: String,
) : NotFoundException("Could not find document with uuid: $uuid")
