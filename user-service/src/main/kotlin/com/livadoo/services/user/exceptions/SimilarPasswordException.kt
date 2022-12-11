package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.BadInputException
import com.livadoo.common.exceptions.ErrorCodes

class SimilarPasswordException(
) : BadInputException(
    title = "Similar passwords",
    detail = "Passwords are too similar",
    statusCode = ErrorCodes.SIMILAR_PASSWORD
)