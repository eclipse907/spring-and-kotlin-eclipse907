package com.infinumacademy.project.services

import com.infinumacademy.project.dtos.AddCarCheckUpDto
import com.infinumacademy.project.dtos.CarCheckUpDto
import com.infinumacademy.project.exceptions.CarCheckUpNotFoundException
import com.infinumacademy.project.exceptions.WrongCarCheckUpCarIdException
import com.infinumacademy.project.exceptions.WrongCarCheckUpDataException
import com.infinumacademy.project.repositories.CarCheckUpRepository
import com.infinumacademy.project.repositories.CarRepository
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
        if (carCheckUpToAdd.timeOfCheckUp > LocalDateTime.now()) {
            throw WrongCarCheckUpDataException("Date and time of check-up can't be after current date and time")
        }
        return CarCheckUpDto(carCheckUpRepository.save(carCheckUpToAdd.toCarCheckUp {
            carRepository.findById(it) ?: throw WrongCarCheckUpCarIdException("No car with given id found")
        }))
    }

    fun getAllCarCheckUps() =
        carCheckUpRepository.findAllByOrderByTimeOfCheckUpDesc().map { CarCheckUpDto(it) }

    fun getAllCarCheckUpsWithCarId(carId: Long, pageable: Pageable) =
        carCheckUpRepository.findByCarIdOrderByTimeOfCheckUpDesc(carId, pageable).map { CarCheckUpDto(it) }

}
