package com.infinumacademy.project.services

import com.infinumacademy.project.dtos.AddCarCheckUpDto
import com.infinumacademy.project.dtos.CarCheckUpDto
import com.infinumacademy.project.dtos.UpcomingCarCheckUpsInterval
import com.infinumacademy.project.exceptions.CarCheckUpNotFoundException
import com.infinumacademy.project.exceptions.CarNotFoundException
import com.infinumacademy.project.exceptions.WrongCarCheckUpCarIdException
import com.infinumacademy.project.repositories.CarCheckUpRepository
import com.infinumacademy.project.repositories.CarRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CarCheckUpService(
    private val carCheckUpRepository: CarCheckUpRepository,
    private val carRepository: CarRepository
) {

    fun getCarCheckUpWithId(id: Long) = carCheckUpRepository.findById(id)?.let {
        CarCheckUpDto(it)
    } ?: throw CarCheckUpNotFoundException(id)

    fun addCarCheckUp(carCheckUpToAdd: AddCarCheckUpDto): CarCheckUpDto {
        return CarCheckUpDto(carCheckUpRepository.save(carCheckUpToAdd.toCarCheckUp {
            carRepository.findById(it) ?: throw WrongCarCheckUpCarIdException("No car with given id found")
        }))
    }

    fun getAllCarCheckUps(pageable: Pageable) =
        carCheckUpRepository.findAllByOrderByTimeOfCheckUpDesc(pageable).map { CarCheckUpDto(it) }

    fun getAllCarCheckUpsWithCarId(carId: Long, pageable: Pageable) =
        carRepository.findById(carId)?.let {
            carCheckUpRepository.findByCarIdOrderByTimeOfCheckUpDesc(carId, pageable).map { CarCheckUpDto(it) }
        } ?: throw CarNotFoundException(carId)

    fun getLastTenCarCheckUpsPerformed() =
        carCheckUpRepository.findAllByTimeOfCheckUpBeforeOrderByTimeOfCheckUpDesc(
            LocalDateTime.now(), PageRequest.of(0, 10)
        ).content.map { CarCheckUpDto(it) }

    fun getUpcomingCarCheckUps(duration: UpcomingCarCheckUpsInterval) =
        carCheckUpRepository.findAllByTimeOfCheckUpBetweenOrderByTimeOfCheckUp(
            LocalDateTime.now(),
            LocalDateTime.now().plus(duration.period)
        ).map { CarCheckUpDto(it) }

    fun deleteCarCheckUpWithId(id: Long) =
        carCheckUpRepository.findById(id)?.let { carCheckUpRepository.deleteById(id) }
            ?: throw CarCheckUpNotFoundException(id)

}
