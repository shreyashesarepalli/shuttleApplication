package com.shuttleApp.ShuttleApplication.service;

import com.shuttleApp.ShuttleApplication.model.Location;

import java.util.List;

public class EtaCalculator {

    public static double calculateEta(List<Location> passengerLocations, List<Location> shuttleLocation) {

        // Create a new PassengerLocation object for the last location (campus)
        Location lastLocation = new Location();
        lastLocation.setLatitude(43.03789557301325);
        lastLocation.setLongitude(-76.13133627075227);

        // Add the last location to the shuttleLocation list
        passengerLocations.add(lastLocation);

        double totalDuration = 0.0;

        if (!shuttleLocation.isEmpty()) {
            Location shuttle = shuttleLocation.get(0);
            double shuttleLongitude = shuttle.getLongitude();
            double shuttleLatitude = shuttle.getLatitude();

            for (Location location : passengerLocations) {
                double passengerLongitude = location.getLongitude();
                double passengerLatitude = location.getLatitude();

                // Calculate the ETA using the correct order of longitude and latitude
                double eta = TravelTimeCalculationService.calculateTravelTime(shuttleLongitude, shuttleLatitude, passengerLongitude, passengerLatitude);
                totalDuration += eta;

                // Set the shuttle's current location to the passenger's location
                shuttleLongitude = passengerLongitude;
                shuttleLatitude = passengerLatitude;
            }
        } else {
            System.out.println("Shuttle location not found.");
        }

        return totalDuration;
    }


}
