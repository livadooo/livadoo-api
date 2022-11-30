package com.livadoo.common.exceptions

import org.zalando.problem.Status
import javax.annotation.concurrent.Immutable

@Immutable
class NotAllowedException(detail: String) : BaseThrowableProblem(
    "You don't have access to this resource",
    detail,
    Status.FORBIDDEN,
)