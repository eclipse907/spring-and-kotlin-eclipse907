package com.infinumacademy.project.services

import com.infinumacademy.project.exceptions.CarCheckUpNotFoundException
import com.infinumacademy.project.exceptions.CarNotFoundException
import com.infinumacademy.project.exceptions.WrongCarCheckUpDataException
import com.infinumacademy.project.models.CarCheckUp
import com.infinumacademy.project.repositories.CarCheckUpRepository
import com.infinumacademy.project.repositories.CarRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CarCheckUpService(
    private val carCheckUpRepository: CarCheckUpRepository,
    private val carRepository: CarRepository
) {

    fun getCarCheckUpWithId(id: Long) = carCheckUpRepository.findById(id) ?: throw CarCheckUpNotFoundException(id)

    fun addCarCheckUp(carCheckUp: CarCheckUp): CarCheckUp {
        carCheckUpRepository.findById(carCheckUp.id)?.let {
            throw WrongCarCheckUpDataException("Car check-up with id ${carCheckUp.id} already exists")
        }
        if (carCheckUp.timeOfCheckUp > LocalDateTime.now()) {
            throw WrongCarCheckUpDataException("Date and time of check-up can't be after current date and time")
        }
        if (carCheckUp.workerName.isBlank()) {
            throw WrongCarCheckUpDataException("Worker name can't be blank")
        }
        if (carCheckUp.price < 0) {
            throw WrongCarCheckUpDataException("Price can't be less than zero")
        }
        carRepository.updateCarWithCarCheckUp(carCheckUp) ?: throw CarNotFoundException(carCheckUp.id)
        carCheckUpRepository.save(carCheckUp)
        return carCheckUp
    }

    fun getAllCarCheckUps() = carCheckUpRepository.findAll().sortedByDescending { it.timeOfCheckUp }

}
