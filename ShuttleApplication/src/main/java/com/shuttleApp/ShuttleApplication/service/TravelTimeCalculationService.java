package com.shuttleApp.ShuttleApplication.service;

import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TravelTimeCalculationService {

    public static double calculateTravelTime( double startLongitude, double startLatitude, double endLongitude, double endLatitude) {
        RestTemplate restTemplate = new RestTemplate();

        String profile = "mapbox/driving";
        String coordinates = startLongitude + "," + startLatitude + ";" + endLongitude + "," + endLatitude;

        System.out.println(coordinates);

        // Replace with your Mapbox access token
        String mapboxAccessToken = "pk.eyJ1IjoidnNhcmVwYWwiLCJhIjoiY2xvYWpnZjd0MGhicjJrcGIzOXg0bXdsbiJ9.pX5cIjeSFGOb6fJW-hzimQ";
        String url = "https://api.mapbox.com/directions/v5/" + profile + "/" + coordinates + "?waypoints_per_route=true&access_token=" + mapboxAccessToken;


        // Send a GET request to the Mapbox Directions API
        String response = restTemplate.getForObject(url, String.class);

        System.out.println(response);

        // Parse the JSON response
        double duration = 0;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            // Extract the "duration" from the JSON response
            duration = jsonNode.path("routes").get(0).path("duration").asDouble();

            // The "duration" is in seconds, you can convert it to minutes, hours, etc. as needed
            System.out.println(duration / 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duration/60;
    }
}
