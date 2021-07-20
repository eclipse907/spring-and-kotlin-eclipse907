package com.infinumacademy.project.services

import com.infinumacademy.project.exceptions.CarNotFoundException
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
            throw IllegalArgumentException("Car added date can't be after current date")
        }
        if (car.manufacturerName.isBlank()) {
            throw IllegalArgumentException("Manufacturer name can't be blank")
        }
        if (car.modelName.isBlank()) {
            throw IllegalArgumentException("Model name can't be blank")
        }
        if (car.productionYear.isAfter(Year.now())) {
            throw IllegalArgumentException("Car production year can't be after current year")
        }
        car.id = carRepository.getAllCarIds().maxOrNull()?.plus(1) ?: 0
        carRepository.insert(car)
        return car
    }

    fun addCarCheckUpToCar(carCheckUp: CarCheckUp) = carRepository.updateCarWithCarCheckUp(carCheckUp)

    fun getAllCars() = carRepository.getAllCars().onEach { car -> car.carCheckUps.sortByDescending { it.timeOfCheckUp } }

}