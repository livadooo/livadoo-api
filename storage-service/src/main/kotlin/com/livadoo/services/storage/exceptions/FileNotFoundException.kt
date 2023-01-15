package com.livadoo.services.storage.exceptions

import com.livadoo.services.common.exceptions.ErrorCodes
import com.livadoo.services.common.exceptions.ObjectNotFoundException
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorCode
import io.github.wimdeblauwe.errorhandlingspringbootstarter.ResponseErrorProperty

@ResponseErrorCode(ErrorCodes.FILE_NOT_FOUND)
class FileNotFoundException(
    @ResponseErrorProperty val uri: String
) : ObjectNotFoundException("Could not find file with uri: $uri")