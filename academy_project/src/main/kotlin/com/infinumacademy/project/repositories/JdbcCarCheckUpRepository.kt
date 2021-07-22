package com.infinumacademy.project.repositories

import com.infinumacademy.project.models.CarCheckUp
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class JdbcCarCheckUpRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) : CarCheckUpRepository {

    override fun findById(id: Long) = jdbcTemplate.queryForObject(
        "SELECT * FROM car_check_up WHERE id = :id",
        mapOf("id" to id)
    ) { resultSet, _ ->
        CarCheckUp(
            resultSet.getLong("id"),
            resultSet.getTimestamp("time_of_check_up").toLocalDateTime(),
            resultSet.getString("worker_name"),
            resultSet.getDouble("price"),
            resultSet.getLong("car_id")
        )
    }

    override fun save(carCheckUp: CarCheckUp) {
        jdbcTemplate.update(
            "INSERT INTO car_check_up (id, time_of_check_up, worker_name, price, car_id)" +
                    "VALUES (:id, :timeOfCheckUp, :workerName, :price, :carId)",
            mapOf(
                "id" to carCheckUp.id,
                "timeOfCheckUp" to carCheckUp.timeOfCheckUp,
                "workerName" to carCheckUp.workerName,
                "price" to carCheckUp.price,
                "carId" to carCheckUp.carId
            )
        )
    }

    override fun findByCarId(carId: Long): List<CarCheckUp> =
        jdbcTemplate.query(
            "SELECT * FROM car_check_up WHERE car_id = :carId",
            mapOf("carId" to carId)
        ) { resultSet, _ ->
            CarCheckUp(
                resultSet.getLong("id"),
                resultSet.getTimestamp("time_of_check_up").toLocalDateTime(),
                resultSet.getString("worker_name"),
                resultSet.getDouble("price"),
                resultSet.getLong("car_id")
            )
        }

    override fun findAll(): List<CarCheckUp> = jdbcTemplate.query("SELECT * FROM car_check_up") { resultSet, _ ->
        CarCheckUp(
            resultSet.getLong("id"),
            resultSet.getTimestamp("time_of_check_up").toLocalDateTime(),
            resultSet.getString("worker_name"),
            resultSet.getDouble("price"),
            resultSet.getLong("car_id")
        )
    }

}