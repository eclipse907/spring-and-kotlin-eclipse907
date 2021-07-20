package com.infinumacademy.project.services

import com.infinumacademy.project.exceptions.CarCheckUpNotFoundException
import com.infinumacademy.project.models.CarCheckUp
import com.infinumacademy.project.repositories.CarCheckUpRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CarCheckUpService(
    private val carCheckUpRepository: CarCheckUpRepository,
    private val carService: CarService
) {

    fun getCarCheckUpWithId(id: Long) = carCheckUpRepository.findById(id) ?: throw CarCheckUpNotFoundException(id)

    fun addCarCheckUp(carCheckUp: CarCheckUp): CarCheckUp {
        carCheckUpRepository.findById(carCheckUp.id)?.let {
            throw IllegalArgumentException("Car check-up with id ${carCheckUp.id} already exists")
        }
        if (carCheckUp.timeOfCheckUp > LocalDateTime.now()) {
            throw IllegalArgumentException("Date and time of check-up can't be after current date and time")
        }
        if (carCheckUp.workerName.isBlank()) {
            throw IllegalArgumentException("Worker name can't be blank")
        }
        if (carCheckUp.price < 0) {
            throw IllegalArgumentException("Price can't be less than zero")
        }
        carService.addCarCheckUpToCar(carCheckUp)
        carCheckUpRepository.insert(carCheckUp)
        return carCheckUp
    }

    fun getAllCarCheckUps() = carCheckUpRepository.getAllCarCheckUps().sortedByDescending { it.timeOfCheckUp }

}
