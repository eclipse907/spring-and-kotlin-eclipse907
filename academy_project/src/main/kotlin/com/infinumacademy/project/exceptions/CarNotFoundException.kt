package com.infinumacademy.project.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class CarNotFoundException(id: Long) : ResponseStatusException(HttpStatus.NOT_FOUND, "Car with id $id not found")