package com.livadoo.services.storage.exceptions

import com.livadoo.common.exceptions.ErrorCodes
import com.livadoo.common.exceptions.ObjectNotFoundException


class FileNotFoundException(uri: String) : ObjectNotFoundException(
    title = "File not found",
    detail = "File with uri: $uri not found",
    statusCode = ErrorCodes.FILE_NOT_FOUND
)