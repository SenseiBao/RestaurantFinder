package com.yourpackage;

import java.util.Scanner;
import java.util.InputMismatchException;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

public class Main {
    public static void main(String[] args) {
        //API stuff
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("API key") //Replace with actual API key
                .build();

        //scanners to find user latitude and longitude
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Enter your latitude: (note you can find your latitude and longitude by \n " +
                "right clicking on your location in google maps");
        double lat = myScanner.nextDouble();
        System.out.println("Enter your longitude: (note you can find your latitude and longitude by \n " +
                "right clicking on your location in google maps");
        double lon = myScanner.nextDouble();

        //search radius
        int radius = 0;
        boolean valid = false;
        while (!valid) {
            System.out.println("What should the search radius be in meters?");
            try {
                radius = myScanner.nextInt();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer with no decimals.");
                myScanner.next();
            }
        }

        try {
            //Searches for places closest to the location
            PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, new LatLng(lat, lon))
                    .radius(radius)  //radius in meters
                    .keyword("restaurant")
                    .await();

            //Prints the names, addresses, ratings, and URLs of the places found
            for (PlacesSearchResult result : response.results) {
                System.out.println("Name: " + result.name);
                System.out.println("Address: " + result.vicinity);
                if (result.rating > 0) {
                    System.out.println("Rating: " + result.rating);
                } else {
                    System.out.println("Rating: Not available");
                }

                //Prints the URL for the restaurant
                String placeUrl = "https://www.google.com/maps/place/?q=place_id:" + result.placeId;
                System.out.println("More Info: " + placeUrl);
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
