package com.livadoo.common.exceptions

import org.zalando.problem.AbstractThrowableProblem
import org.zalando.problem.Exceptional
import org.zalando.problem.Status
import java.net.URI
import javax.annotation.concurrent.Immutable

@Immutable
open class BaseThrowableProblem(
    title: String,
    detail: String,
    status: Status
) : AbstractThrowableProblem(
    TYPE,
    title,
    status,
    detail
) {
    override fun getCause(): Exceptional? = null
}

private val TYPE = URI.create("about:blank")