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
import java.lang.Exception
import java.time.LocalDateTime

@ControllerAdvice
class ControllerAdvisor : ResponseEntityExceptionHandler() {

    @ExceptionHandler(Exception::class)
    fun handleExceptions(ex: Exception) = ResponseEntity.internalServerError().body(
        ExceptionResponse(ex.message, null, LocalDateTime.now())
    )

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse(ex.message, null, LocalDateTime.now()))

    @ExceptionHandler(WrongCarDataException::class)
    fun handleWrongCarDataException(ex: WrongCarDataException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse(ex.message, null, LocalDateTime.now()))

    @ExceptionHandler(WrongCarCheckUpDataException::class)
    fun handleWrongCarCheckUpDataException(ex: WrongCarCheckUpDataException) =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse(ex.message, null, LocalDateTime.now()))

    @ExceptionHandler(NoCarModelsRetrievedException::class)
    fun handleNoCarModelsRetrievedException(ex: NoCarModelsRetrievedException) =
        ResponseEntity.internalServerError().body(ExceptionResponse(ex.message, null, LocalDateTime.now()))

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> = ResponseEntity.status(status).body(
        ExceptionResponse("Bad post request arguments", ex.bindingResult.toString(), LocalDateTime.now())
    )

}
