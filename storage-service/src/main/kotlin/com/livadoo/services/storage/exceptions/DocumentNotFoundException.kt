package com.livadoo.services.storage.exceptions

import com.livadoo.common.exceptions.ErrorCodes
import com.livadoo.common.exceptions.ObjectNotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.DOCUMENT_NOT_FOUND)
class DocumentNotFoundException(
    @ResponseErrorProperty val uuid: String
) : ObjectNotFoundException("Could not find document with uuid: $uuid")