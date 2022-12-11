package com.livadoo.services.advice.validation

import com.livadoo.services.advice.violations.Violation
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import java.util.stream.Collectors
import java.util.stream.Stream

interface BaseBindingResultAdviceTrait : BaseValidationAdviceTrait {

    fun createViolation(error: FieldError): Violation {
        return Violation(error.field, error.defaultMessage ?: "")
    }

    fun createViolation(error: ObjectError): Violation? {
        return Violation(error.objectName, error.defaultMessage ?: "")
    }

    fun createViolations(result: BindingResult): List<Violation> {
        val fieldErrors = result.fieldErrors.stream().map(this::createViolation)
        val globalErrors = result.globalErrors.stream().map(this::createViolation)
        return Stream.concat(fieldErrors, globalErrors).collect(Collectors.toList<Violation>())
    }
}