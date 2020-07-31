package com.techustle.billablehour.v1.backend.exception

import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.async.AsyncWebRequest
import java.util.*
import javax.validation.ConstraintViolationException

@ControllerAdvice
class GlobalExceptionHandler {

    //handle resource not found exception
    @ExceptionHandler(InvalidConfigurationPropertyValueException::class)
    fun handleResourceNotFoundException(exception: InvalidConfigurationPropertyValueException,
                                        webRequest: WebRequest ): ResponseEntity<*>? {
        var errorDetails = ErrorDetails(Date(), exception.message, webRequest.getDescription(false))
        return ResponseEntity(errorDetails, HttpStatus.NOT_FOUND)
    }

    //handling custom validation errors
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun customValidationErrorValidation(exception: MethodArgumentNotValidException?): ResponseEntity<*>? {
        val errorDetails = ErrorDetails(Date(), "Validation Error",
                exception!!.bindingResult.fieldError!!.defaultMessage!!)
        return ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST)
    }


    //handle resource not found exception
    @ExceptionHandler(Exception::class)
    fun handleGlobalException(exception: Exception, webRequest: WebRequest ): ResponseEntity<*>? {
        var errorDetails = ErrorDetails(Date(), "Oops! Something went wrong!", webRequest.getDescription(false))
        return ResponseEntity(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR)
    }

}