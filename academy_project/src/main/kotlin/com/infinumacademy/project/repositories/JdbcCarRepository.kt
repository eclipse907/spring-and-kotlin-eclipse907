package com.infinumacademy.project.repositories

import com.infinumacademy.project.models.Car
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.time.Year
import javax.sql.DataSource

@Repository
class JdbcCarRepository(
    private val jdbcTemplate: NamedParameterJdbcTemplate,
    dataSource: DataSource
) : CarRepository {

    private val jdbcInsertCar = SimpleJdbcInsert(dataSource)
        .withTableName("car")
        .usingGeneratedKeyColumns("id")

    override fun findById(id: Long) = jdbcTemplate.queryForObject(
        "SELECT * FROM car WHERE id = :id",
        mapOf("id" to id)
    ) { resultSet, _ ->
        Car(
            resultSet.getLong("id"),
            resultSet.getLong("owner_id"),
            resultSet.getDate("date_added").toLocalDate(),
            resultSet.getString("manufacturer_name"),
            resultSet.getString("model_name"),
            Year.parse(resultSet.getInt("production_year").toString()),
            resultSet.getLong("serial_number")
        )
    }

    override fun save(car: Car): Long {
        val newId = jdbcInsertCar.executeAndReturnKey(mapOf(
            "owner_id" to car.ownerId,
            "date_added" to car.dateAdded,
            "manufacturer_name" to car.manufacturerName,
            "model_name" to car.modelName,
            "production_year" to car.productionYear.toString().toInt(),
            "serial_number" to car.serialNumber
        ))
        return newId.toLong()
    }

    override fun findAll(): List<Car> = jdbcTemplate.query("SELECT * FROM car") { resultSet, _ ->
        Car(
            resultSet.getLong("id"),
            resultSet.getLong("owner_id"),
            resultSet.getDate("date_added").toLocalDate(),
            resultSet.getString("manufacturer_name"),
            resultSet.getString("model_name"),
            Year.parse(resultSet.getInt("production_year").toString()),
            resultSet.getLong("serial_number")
        )
    }

}
