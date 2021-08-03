package com.infinumacademy.project.converters

import com.infinumacademy.project.annotations.EnumConverter
import com.infinumacademy.project.dtos.UpcomingCarCheckUpsInterval
import com.infinumacademy.project.exceptions.IllegalUpcomingCarCheckUpsInterval
import org.springframework.core.convert.converter.Converter

@EnumConverter
class UpcomingCarCheckUpsIntervalConverter : Converter<String, UpcomingCarCheckUpsInterval> {

    override fun convert(source: String) = when (source) {
        "week" -> UpcomingCarCheckUpsInterval.WEEK
        "month" -> UpcomingCarCheckUpsInterval.MONTH
        "halfYear" -> UpcomingCarCheckUpsInterval.HALF_YEAR
        else -> throw IllegalUpcomingCarCheckUpsInterval("Wrong or non existent interval for upcoming car check-ups")
    }

}