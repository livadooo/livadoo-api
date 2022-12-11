package com.livadoo.services.advice.violations

import javax.annotation.concurrent.Immutable

@Immutable
data class Violation(val field: String, val message: String)