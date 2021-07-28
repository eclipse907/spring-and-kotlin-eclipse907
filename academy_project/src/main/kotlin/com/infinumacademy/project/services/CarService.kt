package com.infinumacademy.project.services

import com.infinumacademy.project.dtos.AddCarDto
import com.infinumacademy.project.dtos.CarDto
import com.infinumacademy.project.exceptions.CarNotFoundException
import com.infinumacademy.project.exceptions.WrongCarDataException
import com.infinumacademy.project.repositories.CarCheckUpRepository
import com.infinumacademy.project.repositories.CarRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Year

@Service
class CarService(
    private val carRepository: CarRepository,
    private val carCheckUpRepository: CarCheckUpRepository
) {

    fun getCarWithId(id: Long) = carRepository.findById(id)?.let {
        CarDto(it, carCheckUpRepository.findByCarIdOrderByTimeOfCheckUpDesc(it.id))
    } ?: throw CarNotFoundException(id)

    fun addCar(carToAdd: AddCarDto): CarDto {
        if (carToAdd.dateAdded > LocalDate.now()) {
            throw WrongCarDataException("Car added date can't be after current date")
        }
        if (carToAdd.productionYear > Year.now().toString().toInt()) {
            throw WrongCarDataException("Car production year can't be after current year")
        }
        return CarDto(carRepository.save(carToAdd.toCar()))
    }

    fun getAllCars() = carRepository.findAll().map { CarDto(it) }

    fun getAllCars(pageable: Pageable) = carRepository.findAll(pageable).map { CarDto(it) }

}