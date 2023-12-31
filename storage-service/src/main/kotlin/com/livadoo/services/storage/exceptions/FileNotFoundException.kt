package com.livadoo.services.storage.exceptions

import com.livadoo.utils.exception.ErrorCodes
import com.livadoo.utils.exception.NotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.FILE_NOT_FOUND)
class FileNotFoundException(
    @ResponseErrorProperty val uri: String,
) : NotFoundException("Could not find file with uri: $uri")
