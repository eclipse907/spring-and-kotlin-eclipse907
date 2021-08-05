package com.infinumacademy.project.dtos

import java.time.Period

enum class UpcomingCarCheckUpsInterval(val period: Period) {
    WEEK(Period.ofWeeks(1)),
    MONTH(Period.ofMonths(1)),
    HALF_YEAR(Period.ofMonths(6))
}