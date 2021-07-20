package com.infinumacademy.project.exceptions

import java.time.LocalDateTime

data class ExceptionResponse(
    val message: String?,
    val detail: String,
    val timestamp: LocalDateTime
)