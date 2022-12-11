package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.BadInputException
import com.livadoo.common.exceptions.ErrorCodes

class WrongPasswordException(
) : BadInputException(
    title = "Wrong password",
    detail = "Password you entered is incorrect",
    statusCode = ErrorCodes.WRONG_PASSWORD
)