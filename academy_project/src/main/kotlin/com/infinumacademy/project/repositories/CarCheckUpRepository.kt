package com.infinumacademy.project.repositories

import com.infinumacademy.project.models.CarCheckUp
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository

interface CarCheckUpRepository : Repository<CarCheckUp, Long> {
    fun findById(id: Long): CarCheckUp?

    fun save(carCheckUp: CarCheckUp): CarCheckUp

    fun findByCarIdOrderByTimeOfCheckUpDesc(carId: Long): List<CarCheckUp>

    @Query("select car_check_up from CarCheckUp car_check_up join fetch car_check_up.car order by car_check_up.timeOfCheckUp desc")
    fun findAllByOrderByTimeOfCheckUpDesc(): List<CarCheckUp>

    fun findByCarIdOrderByTimeOfCheckUpDesc(carId: Long, pageable: Pageable): Page<CarCheckUp>
}
