package com.infinumacademy.project.repositories

import com.infinumacademy.project.models.CarCheckUp
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import javax.sql.DataSource

@Repository
class JdbcCarCheckUpRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    dataSource: DataSource
) : CarCheckUpRepository {

    private val jdbcInsertCarCheckUp = SimpleJdbcInsert(dataSource)
        .withTableName("car_check_up")
        .usingGeneratedKeyColumns("id")

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

    override fun save(carCheckUp: CarCheckUp): Long {
        val newId = jdbcInsertCarCheckUp.executeAndReturnKey(
            mapOf(
                "time_of_check_up" to carCheckUp.timeOfCheckUp,
                "worker_name" to carCheckUp.workerName,
                "price" to carCheckUp.price,
                "car_id" to carCheckUp.carId
            )
        )
        return newId.toLong()
    }

    override fun findByCarId(carId: Long): List<CarCheckUp> =
        jdbcTemplate.query(
            "SELECT * FROM car_check_up WHERE car_id = :carId ORDER BY time_of_check_up DESC",
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

    override fun findAll(): List<CarCheckUp> =
        jdbcTemplate.query("SELECT * FROM car_check_up ORDER BY time_of_check_up DESC") { resultSet, _ ->
            CarCheckUp(
                resultSet.getLong("id"),
                resultSet.getTimestamp("time_of_check_up").toLocalDateTime(),
                resultSet.getString("worker_name"),
                resultSet.getDouble("price"),
                resultSet.getLong("car_id")
            )
        }

}