package com.infinumacademy.project.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class CarCheckUpNotFoundException(id: Long) :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Car check-up with id $id not found")

class CarNotFoundException(id: Long) : ResponseStatusException(HttpStatus.NOT_FOUND, "Car with id $id not found")

class WrongCarDataException(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message)

class WrongCarCheckUpDataException(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message)

class WrongCarCheckUpCarIdException(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message)

class WrongCarModelInCarRequestException(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message)

class NoCarModelsRetrievedException(message: String) : ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, message)

class CarSerialNumberAlreadyExistsException(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message)

class IllegalUpcomingCarCheckUpsInterval(message: String) : ResponseStatusException(HttpStatus.BAD_REQUEST, message)
