package com.livadoo.common.exceptions

import org.zalando.problem.Status
import javax.annotation.concurrent.Immutable

@Immutable
class UnauthorizedException(detail: String = "You are not authenticated") : BaseThrowableProblem(
    "You are unauthorized",
    detail,
    Status.UNAUTHORIZED,
)