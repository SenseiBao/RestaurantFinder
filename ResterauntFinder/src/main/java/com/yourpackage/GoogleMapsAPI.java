package com.yourpackage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GoogleMapsAPI {
    //API stuff
    private static final String API_KEY = "Api Key"; //Replace with your actual API key

    /**
     * Finds nearby restaurants based on latitude and longitude.
     *
     * @param lat Latitude of the location.
     * @param lon Longitude of the location.
     * @return A list of restaurants near the specified location.
     * @throws IOException If there is an error making the request.
     */
    public static List<Restaurant> getNearbyRestaurants(double lat, double lon) throws IOException {
        //Creates the URL based on the lat and long
        String urlString = String.format(
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=1500&type=restaurant&opennow=true&key=%s",
                lat, lon, API_KEY
        );

        //Generates URL and opens an HTTP connection
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        //Reads the response
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        Scanner scanner = new Scanner(reader);
        StringBuilder jsonResponse = new StringBuilder();
        while (scanner.hasNext()) {
            jsonResponse.append(scanner.nextLine());
        }
        scanner.close();

        //Parses the JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse.toString());
        JsonNode resultsNode = rootNode.path("results");

        //Creates an arrayList of Restaurant objects
        List<Restaurant> restaurants = new ArrayList<>();
        for (JsonNode node : resultsNode) {
            String name = node.path("name").asText();
            String address = node.path("vicinity").asText();
            double rating = node.path("rating").asDouble(-1);
            restaurants.add(new Restaurant(name, address, rating));
        }

        return restaurants;
    }

    /**
     * Represents a restaurant with details such as name, address, and rating.
     */
    public static class Restaurant {
        private String name;
        private String address;
        private double rating;

        public Restaurant(String name, String address, double rating) {
            this.name = name;
            this.address = address;
            this.rating = rating;
        }

        @Override
        public String toString() {
            return String.format("Name: %s, Address: %s, Rating: %.1f", name, address, rating);
        }
    }
}
