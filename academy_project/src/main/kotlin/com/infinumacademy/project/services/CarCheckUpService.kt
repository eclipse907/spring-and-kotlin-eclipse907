package com.infinumacademy.project.services

import com.infinumacademy.project.exceptions.CarCheckUpNotFoundException
import com.infinumacademy.project.exceptions.WrongCarCheckUpCarIdException
import com.infinumacademy.project.exceptions.WrongCarCheckUpDataException
import com.infinumacademy.project.models.CarCheckUp
import com.infinumacademy.project.repositories.CarCheckUpRepository
import com.infinumacademy.project.repositories.CarRepository
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CarCheckUpService(
    private val carCheckUpRepository: CarCheckUpRepository,
    private val carRepository: CarRepository
) {

    fun getCarCheckUpWithId(id: Long): CarCheckUp {
        try {
            return carCheckUpRepository.findById(id) ?: throw CarCheckUpNotFoundException(id)
        } catch (ex: IncorrectResultSizeDataAccessException) {
            throw CarCheckUpNotFoundException(id)
        }
    }

    fun addCarCheckUp(carCheckUp: CarCheckUp): CarCheckUp {
        try {
            carCheckUpRepository.findById(carCheckUp.id)?.let {
                throw WrongCarCheckUpDataException("Car check-up with id ${carCheckUp.id} already exists")
            }
        } catch (ignorable: IncorrectResultSizeDataAccessException) {
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
        try {
            carRepository.findById(carCheckUp.carId)
                ?: throw WrongCarCheckUpCarIdException("Car check-up has non existent car id")
        } catch (ex: IncorrectResultSizeDataAccessException) {
            throw WrongCarCheckUpCarIdException("Car check-up has non existent car id")
        }
        carCheckUpRepository.save(carCheckUp)
        return carCheckUp
    }

    fun getAllCarCheckUps() = carCheckUpRepository.findAll().sortedByDescending { it.timeOfCheckUp }

}
