package com.infinumacademy.project.exceptions

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime

@ControllerAdvice
class ControllerAdvisor : ResponseEntityExceptionHandler() {

    /*@ExceptionHandler(Throwable::class)
    fun handleExceptions(ex: Throwable) = ResponseEntity.internalServerError().body(
        ExceptionResponse(ex.message, null, LocalDateTime.now())
    )*/

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(ex: ResponseStatusException) = ResponseEntity.status(ex.status).body(
        ExceptionResponse(ex.message, ex.reason, LocalDateTime.now())
    )

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException) = ResponseEntity.badRequest().body(
        ExceptionResponse(ex.message, null, LocalDateTime.now())
    )

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> = ResponseEntity.status(status).body(
        ExceptionResponse("Bad post request arguments", ex.bindingResult.toString(), LocalDateTime.now())
    )

}
