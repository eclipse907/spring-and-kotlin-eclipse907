package com.infinumacademy.project.exceptions

open class ResourceNotFoundException(message: String) : RuntimeException(message)

class CarCheckUpNotFoundException(id: Long) : ResourceNotFoundException("Car check-up with id $id not found")

class CarNotFoundException(id: Long) : ResourceNotFoundException("Car with id $id not found")

open class WrongCarDataException(message: String) : RuntimeException(message)

class WrongCarDateAddedException(message: String) : WrongCarDataException(message)

class WrongCarProductionYearException(message: String): WrongCarDataException(message)

open class WrongCarCheckUpDataException(message: String): RuntimeException(message)

class WrongCarCheckUpCarIdException(message: String) : WrongCarCheckUpDataException(message)

class WrongCarModelInCarRequestException(message: String) : WrongCarDataException(message)

class NoCarModelsRetrievedException(message: String) : RuntimeException(message)

class CarSerialNumberAlreadyExistsException(message: String) : WrongCarDataException(message)
