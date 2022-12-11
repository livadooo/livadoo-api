package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.BadInputException

class BadAuthorityException(
    authority: String
) : BadInputException("Bad input", "Authority: {$authority} is invalid")