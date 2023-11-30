package uk.ac.ed.inf;

import uk.ac.ed.inf.data.Move;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import static uk.ac.ed.inf.OrderValidator.getValidatedOrders;

public class PizzaDronzApp {
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args){
        if (args.length != 2) {
            throw new RuntimeException("Please input two arguments");
        }

        String restWebsite = args[1];
        String date = args[0];

        if (!RestClient.isAlive(restWebsite)) {
            throw new RuntimeException("The inputted REST service is unavailable");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            //Handle invalid date
            throw new RuntimeException("Invalid date inputted", e);
        }

        //Update Restaurants and Orders from REST
        Restaurant[] restaurants = RestClient.getRestaurants(restWebsite);
        Order[] orders = RestClient.getOrders(date, restWebsite);
        NamedRegion centralArea = RestClient.getCentralArea(restWebsite);
        NamedRegion[] noFlyZones = RestClient.getNoFlyZones(restWebsite);

        //Validate Orders
        Order[] validatedOrders = getValidatedOrders(orders, restaurants);

        //Calculate flight path
        ArrayList<Move> path = FlightPathCalculator.calculateAllPaths(validatedOrders, restaurants, noFlyZones, centralArea);

        //Create the result files
        ResultFileHandler.createAllResults(orders, path, date);
        System.out.println("Program finished running");
    }
}
