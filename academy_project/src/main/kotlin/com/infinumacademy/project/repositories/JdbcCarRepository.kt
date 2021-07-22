package com.infinumacademy.project.repositories

import com.infinumacademy.project.exceptions.CarIdCreationException
import com.infinumacademy.project.models.Car
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.time.Year

@Repository
class JdbcCarRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) : CarRepository {

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
        val idHolder = GeneratedKeyHolder()
        val namedParameters = MapSqlParameterSource()
        namedParameters.addValues(
            mapOf(
                "ownerId" to car.ownerId,
                "dateAdded" to car.dateAdded,
                "manufacturerName" to car.manufacturerName,
                "modelName" to car.modelName,
                "productionYear" to car.productionYear.toString().toInt(),
                "serialNumber" to car.serialNumber
            )
        )
        jdbcTemplate.update(
            "INSERT INTO car (owner_id, date_added, manufacturer_name, model_name, production_year, serial_number) " +
                    "VALUES (:ownerId, :dateAdded, :manufacturerName, :modelName, :productionYear, :serialNumber)",
            namedParameters, idHolder, arrayOf("id")
        )
        return idHolder.key?.toLong()
            ?: throw CarIdCreationException("Error while creating id for car in database")
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
