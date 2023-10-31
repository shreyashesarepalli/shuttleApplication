package com.shuttleApp.ShuttleApplication.api;

import com.shuttleApp.ShuttleApplication.service.*;
import com.shuttleApp.ShuttleApplication.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import java.math.BigInteger;

@RestController
public class ShuttleController {

        @Autowired
        private JdbcTemplate jdbcTemplate;


        @GetMapping("/requestPickup")
        public String requestPickup(@RequestParam BigInteger suid) {
            String sql = "SELECT is_valid_suid(CAST(? AS BIGINT))";
            boolean isValid = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, suid));


            if (isValid) {

                // Call the PostgreSQL function to get a list of passenger locations
                String sqlGetPassengers = "SELECT * FROM get_all_passengers_onn_shuttle()";
                List<Location> passengerLocations = jdbcTemplate.query(sqlGetPassengers, new BeanPropertyRowMapper<>(Location.class));

                double totalDuration = 0.0;

                // Call the PostgreSQL function to get the current shuttle location
                String sqlGetShuttleLocation = "SELECT * FROM get_shuttle_location()";
                List<Location> shuttleLocation = jdbcTemplate.query(sqlGetShuttleLocation, new BeanPropertyRowMapper<>(Location.class));

                totalDuration = EtaCalculator.calculateEta(passengerLocations, shuttleLocation);

                return "ETA: "+ totalDuration + " minutes";
            } else {
                return "Invalid SUID";
            }
        }



    // Implement addPassenger endpoint
    @GetMapping("/addPassenger")
    public String addPassenger(@RequestParam String suid, @RequestParam String address) {
        // Implement logic to add a passenger to the shuttle
        System.out.println("in add passenger");
        String sql = "SELECT is_valid_suid(CAST(? AS BIGINT))";
        boolean isValid = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, suid));


        if (isValid) {

            System.out.println("in isValid");

            String checkPassengerSql = "SELECT check_add_passenger(CAST(? AS BIGINT))";
            boolean isExistingPassenger = Boolean.TRUE.equals(jdbcTemplate.queryForObject(checkPassengerSql, Boolean.class, suid));

            System.out.println(isExistingPassenger);

            if(!isExistingPassenger) {

                System.out.println("in isValidPassenger");

                String[] addressParts = address.split(",");
                if (addressParts.length == 4) {
                    String street = addressParts[0].trim().replaceAll("\"", "");
                    String city = addressParts[1].trim().replaceAll("\"", "");
                    String state = addressParts[2].trim().replaceAll("\"", "");
                    String zip = addressParts[3].trim().replaceAll("\"", "");

                    String coordinates = GeocodingService.getCoordinates(address);
                    String[] coords = coordinates.split(",");
                    System.out.println(coordinates);

                    String latitude = coords[0];
                    String longitude= coords[1];



                    System.out.println(state + street + city + zip);
                    // Call the 'add_passenger' function with the extracted values
                    String insertSql = "CALL add_passenger(CAST(? AS BIGINT),CAST(? as TEXT), CAST(? as TEXT), CAST(? as TEXT), CAST(? as TEXT), CAST(? as DOUBLE PRECISION), CAST(? as DOUBLE PRECISION) )";
                    jdbcTemplate.update(insertSql, suid, street, city, state, zip, latitude, longitude);

                    System.out.println(insertSql);

                    return "Passenger added";
                } else {
                    return "Invalid Address";
                }
            }
            else{
                return "Passenger already added";
            }
        }else {
            return "Invalid SUID";
        }


    }

    // Implement shuttleLocation endpoint
    @GetMapping("/shuttleLocation")
    public String shuttleLocation(@RequestParam double longitude, @RequestParam double latitude) {
        // Implement logic to update shuttle location

        System.out.println(latitude);
        System.out.println(longitude);

        String updateSql = "CAll update_shuttle_location(CAST( ? AS DOUBLE PRECISION), CAST( ? AS DOUBLE PRECISION));";
        jdbcTemplate.update(updateSql, latitude ,longitude);


        List<Location> shuttle = new ArrayList<>();

        Location sLocation = new Location();
        sLocation.setLatitude(latitude);
        sLocation.setLongitude(longitude);

        shuttle.add(sLocation);


        ShuttleSimulator.updateShuttleLocation(shuttle);

        return "Shuttle location updated";
    }
}
