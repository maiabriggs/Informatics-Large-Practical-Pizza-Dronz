package uk.ac.ed.inf;

import uk.ac.ed.inf.data.Move;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.util.ArrayList;

import static uk.ac.ed.inf.OrderValidator.getValidatedOrders;

public class PizzaDronzApp {
    public static void main(String[] args){
        if (args.length != 2) {
            System.out.println("Missing two arguments");
            System.exit(1);
        }

        //TODO: check that the args are valid (i.e. valid date and URL)

        String restWebsite = args[1];
        String date = args[0];

        //Update Restaurants and Orders from REST
        Restaurant[] restaurants = RestClient.getRestaurants(restWebsite);
        Order[] orders = RestClient.getOrders(date, restWebsite);
        NamedRegion centralArea = RestClient.getCentralArea(restWebsite);
        NamedRegion[] noFlyZones = RestClient.noFlyZones(restWebsite);

        //Validate Orders
        Order[] validatedOrders = getValidatedOrders(orders, restaurants);

        ArrayList<Move> path = FlightPathCalculator.calculateAllPaths(validatedOrders, restaurants, noFlyZones, centralArea);

        //Create the result files
        ResultFileHandler.createAllResults(orders, path, date);
        System.out.println("Program finished running");
    }
}
