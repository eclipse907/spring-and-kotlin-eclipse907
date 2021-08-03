package com.infinumacademy.project.repositories

import com.infinumacademy.project.models.CarCheckUp
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.Repository
import java.time.LocalDateTime

interface CarCheckUpRepository : Repository<CarCheckUp, Long> {
    fun findById(id: Long): CarCheckUp?

    fun save(carCheckUp: CarCheckUp): CarCheckUp

    fun findByCarIdOrderByTimeOfCheckUpDesc(carId: Long): List<CarCheckUp>

    fun findAllByOrderByTimeOfCheckUpDesc(pageable: Pageable): Page<CarCheckUp>

    fun findByCarIdOrderByTimeOfCheckUpDesc(carId: Long, pageable: Pageable): Page<CarCheckUp>

    fun findAllByTimeOfCheckUpBeforeOrderByTimeOfCheckUpDesc(
        beforeDateTime: LocalDateTime,
        pageable: Pageable
    ): Page<CarCheckUp>

    fun findAllByTimeOfCheckUpBetweenOrderByTimeOfCheckUp(
        afterDateTime: LocalDateTime,
        beforeDateTime: LocalDateTime
    ): List<CarCheckUp>

}
