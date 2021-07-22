package com.infinumacademy.project.exceptions

import org.springframework.dao.DataAccessException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class ControllerAdvisor : ResponseEntityExceptionHandler() {

    @ExceptionHandler(Throwable::class)
    fun handleExceptions(ex: Throwable) = ResponseEntity.internalServerError().body(
        ExceptionResponse(ex.message, null, LocalDateTime.now())
    )

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException) = ResponseEntity.status(ex.status).body(
        ExceptionResponse(ex.message, ex.reason, LocalDateTime.now())
    )

    @ExceptionHandler(DataAccessException::class)
    fun handleDataAccessException(ex: DataAccessException) = ResponseEntity.internalServerError().body(
        ExceptionResponse(ex.message, null, LocalDateTime.now()))

}
