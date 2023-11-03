package com.shuttleApp.ShuttleApplication.service;

import com.shuttleApp.ShuttleApplication.model.Location;
import com.shuttleApp.ShuttleApplication.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;

@Component
public class ShuttleDropOffs {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ShuttleDropOffs(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String updateShuttleLoc(List<Location> shuttleLocation) {


        String sqlGetTopPassenger = "SELECT * FROM get_top_shuttle_passenger_details()";
        List<Passenger> passengerLocations = jdbcTemplate.query(sqlGetTopPassenger, new BeanPropertyRowMapper<>(Passenger.class));



        if (!shuttleLocation.isEmpty()) {
            Location shuttle = shuttleLocation.get(0);
            double shuttleLongitude = shuttle.getLongitude();
            double shuttleLatitude = shuttle.getLatitude();

            if(!passengerLocations.isEmpty()){

            Passenger passenger = passengerLocations.get(0);
            double passengerLongitude = passenger.getLongitude();
            double passengerLatitude = passenger.getLatitude();
            BigInteger passengerSuid = passenger.getSuid();



             if (shuttleLatitude == passengerLatitude && shuttleLongitude == passengerLongitude) {

                        //Drop passenger off at destination
                        String updateSql = "CAll remove_students_from_shuttle(CAST(? AS BIGINT));";
                        jdbcTemplate.update(updateSql,passengerSuid);

                        return passengerSuid + "Passenger has been dropped";


             }}

            if (shuttleLatitude == 43.03789557301325  && shuttleLongitude == -76.13133627075227) {

                System.out.println("in call!");
                //Shuttle is at stop, add more passengers
                String addPassengerSql = "CAll add_students_to_shuttle();";
                jdbcTemplate.update(addPassengerSql);

                return "Shuttle at campus! Passengers added";

            }
        }
        else {
            return "Shuttle location not found";
        }

        return "";

    }
}
