package com.infinumacademy.project.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class CarCheckUpNotFoundException(id: Long) :
    ResponseStatusException(HttpStatus.NOT_FOUND, "Car check-up with id $id not found")