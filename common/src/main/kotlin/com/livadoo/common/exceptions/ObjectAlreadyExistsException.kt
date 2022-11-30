package com.livadoo.common.exceptions

import org.zalando.problem.Status
import javax.annotation.concurrent.Immutable

@Immutable
open class ObjectAlreadyExistsException(title: String, detail: String) : BaseThrowableProblem(
    title,
    detail,
    Status.BAD_REQUEST,
)