package com.shuttleApp.ShuttleApplication.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GeocodingService{

    public static String getCoordinates( String searchText) {
        String searchEndpoint = "mapbox.places";
        String accessToken = "pk.eyJ1IjoidnNhcmVwYWwiLCJhIjoiY2xvYWpnZjd0MGhicjJrcGIzOXg0bXdsbiJ9.pX5cIjeSFGOb6fJW-hzimQ";

        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("https://api.mapbox.com/geocoding/v5/%s/%s.json?access_token=%s", searchEndpoint, searchText, accessToken);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String responseBody = response.getBody();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // Extract coordinates from the "features" array
            JsonNode features = jsonNode.get("features");
            if (features.isArray()) {
                for (JsonNode feature : features) {
                    JsonNode geometry = feature.get("geometry");
                    if (geometry != null) {
                        JsonNode coordinates = geometry.get("coordinates");
                        if (coordinates != null && coordinates.isArray() && coordinates.size() == 2) {
                            double latitude = coordinates.get(1).asDouble();
                            double longitude = coordinates.get(0).asDouble();
                            return latitude + ", " + longitude;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Coordinates not found";
    }
}
