package com.infinumacademy.project.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class ControllerAdvisor : ResponseEntityExceptionHandler() {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleCarNotFoundException(ex: IllegalArgumentException, request: WebRequest) =
        ResponseEntity.badRequest().body(
            ExceptionResponse(ex.message, request.getDescription(false), LocalDateTime.now())
        )

    @ExceptionHandler(NullPointerException::class)
    fun handleNullPointerException(ex: NullPointerException, request: WebRequest) =
        ResponseEntity.internalServerError().body(
            ExceptionResponse(ex.message, request.getDescription(false), LocalDateTime.now())
        )

}
