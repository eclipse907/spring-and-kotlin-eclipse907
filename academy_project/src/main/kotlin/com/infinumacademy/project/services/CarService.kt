package com.infinumacademy.project.services

import com.infinumacademy.project.exceptions.CarNotFoundException
import com.infinumacademy.project.exceptions.WrongCarDataException
import com.infinumacademy.project.models.Car
import com.infinumacademy.project.models.CarCheckUp
import com.infinumacademy.project.repositories.CarRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Year

@Service
class CarService(private val carRepository: CarRepository) {

    fun getCarWithId(id: Long) = carRepository.findById(id)?.apply {
        this.carCheckUps.sortByDescending { it.timeOfCheckUp }
    } ?: throw CarNotFoundException(id)

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
        car.id = carRepository.getAllCarIds().maxOrNull()?.plus(1) ?: 0
        carRepository.save(car)
        return car
    }

    fun getAllCars() = carRepository.findAll().onEach { car -> car.carCheckUps.sortByDescending { it.timeOfCheckUp } }

}