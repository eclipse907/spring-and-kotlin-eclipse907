package com.infinumacademy.project.services

import com.infinumacademy.project.exceptions.CarNotFoundException
import com.infinumacademy.project.exceptions.WrongCarDataException
import com.infinumacademy.project.models.Car
import com.infinumacademy.project.repositories.CarCheckUpRepository
import com.infinumacademy.project.repositories.CarRepository
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Year

@Service
class CarService(
    private val carRepository: CarRepository,
    private val carCheckUpRepository: CarCheckUpRepository
) {

    fun getCarWithId(id: Long): Car {
        try {
            return carRepository.findById(id)?.copy(carCheckUps = carCheckUpRepository.findByCarId(id))
                ?: throw CarNotFoundException(id)
        } catch (ex: IncorrectResultSizeDataAccessException) {
            throw CarNotFoundException(id)
        }
    }

    fun addCar(car: Car): Car {
        if (car.dateAdded > LocalDate.now()) {
            throw WrongCarDataException("Car added date can't be after current date")
        }
        if (car.manufacturerName.isBlank()) {
            throw WrongCarDataException("Manufacturer name can't be blank")
        }
        if (car.modelName.isBlank()) {
            throw WrongCarDataException("Model name can't be blank")
        }
        if (car.productionYear.isAfter(Year.now())) {
            throw WrongCarDataException("Car production year can't be after current year")
        }
        val id = carRepository.save(car)
        return car.copy(id = id)
    }

    fun getAllCars() = carRepository.findAll()

}