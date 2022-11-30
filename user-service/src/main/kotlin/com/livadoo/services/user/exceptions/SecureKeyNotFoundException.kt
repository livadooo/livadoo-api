package com.livadoo.services.user.exceptions

import com.livadoo.common.exceptions.ObjectNotFoundException

class SecureKeyNotFoundException(
    detail: String
) : ObjectNotFoundException("Clé non trouvée", detail)