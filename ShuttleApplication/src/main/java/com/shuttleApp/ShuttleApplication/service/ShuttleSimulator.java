package com.shuttleApp.ShuttleApplication.service;

import com.shuttleApp.ShuttleApplication.model.Location;
import com.shuttleApp.ShuttleApplication.model.Passenger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ShuttleSimulator {
    private static final double SPEED = 10.0;
    private static final double TIME_INTERVAL = 1.0;

    private static JdbcTemplate jdbcTemplate;

    private static List<double[]> calculateCoordinatesBetweenPoints(double startLat, double startLon, double destLat, double destLon) {
        List<double[]> coordinates = new ArrayList<>();

        int numSegments = 10;

        // Calculate the step size for latitude and longitude
        double latStep = (destLat - startLat) / numSegments;
        double lonStep = (destLon - startLon) / numSegments;

        // Start at the initial coordinates
        double currentLat = startLat;
        double currentLon = startLon;

        // Calculate intermediate coordinates and add them to the list
        for (int i = 0; i <= numSegments; i++) {
            double[] coordinate = {currentLat, currentLon};
            coordinates.add(coordinate);

            // Move to the next coordinate
            currentLat += latStep;
            currentLon += lonStep;
        }

        System.out.println(coordinates);
        return coordinates;
    }

    public static void updateShuttleLocation(List<Location> shuttleLocation) {

        String sqlGetPassengers = "SELECT * FROM get_shuttle_passenger_details()";
        List<Passenger> passengerLocations = jdbcTemplate.query(sqlGetPassengers, new BeanPropertyRowMapper<>(Passenger.class));

        System.out.println(passengerLocations);
        if (!shuttleLocation.isEmpty()) {
            Location shuttle = shuttleLocation.get(0);
            double shuttleLongitude = shuttle.getLongitude();
            double shuttleLatitude = shuttle.getLatitude();

            List<double[]> waypoints;

            for (Passenger passenger : passengerLocations) {
                double passengerLongitude = passenger.getLongitude();
                double passengerLatitude = passenger.getLatitude();
                BigInteger passengerSuid = passenger.getSuid();

                waypoints = calculateCoordinatesBetweenPoints(shuttleLatitude,shuttleLongitude,passengerLatitude,passengerLongitude);

                for (double[] waypoint : waypoints) {
                    double latitude = waypoint[0];
                    double longitude = waypoint[1];

                    // Simulate shuttle movement to the coordinate
                    simulateShuttleMovement(latitude, longitude);

                    // Check if the shuttle has reached a passenger's destination
                    if (latitude == passengerLatitude && longitude == passengerLongitude) {
                        String updateSql = "CAll remove_students_from_shuttle(?);";
                        jdbcTemplate.update(updateSql,passengerSuid);

                    }

                    // Sleep to simulate real-time movement
                    try {
                        Thread.sleep((long) (1000)); // Sleep for the interval duration in milliseconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Set the shuttle's current location to the passenger's location
                shuttleLongitude = passengerLongitude;
                shuttleLatitude = passengerLatitude;
            }
        } else {
            System.out.println("Shuttle location not found.");
        }



    }

    private static void simulateShuttleMovement(double latitude, double longitude) {

        String updateSql = "CAll update_shuttle_location(CAST( ? AS DOUBLE PRECISION), CAST( ? AS DOUBLE PRECISION));";
        jdbcTemplate.update(updateSql, latitude ,longitude);

        System.out.println("Shuttle Location Updated - Latitude: " + latitude + ", Longitude: " + longitude);
    }


}



