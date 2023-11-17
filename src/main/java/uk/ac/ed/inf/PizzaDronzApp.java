package uk.ac.ed.inf;

import uk.ac.ed.inf.OrderValidator;
import uk.ac.ed.inf.RestClient;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

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

        //Get information from REST
        RestClient restClient = new RestClient();

        //Update Restaurants and Orders from REST
        Restaurant[] restaurants = restClient.getRestaurants(restWebsite);
        Order[] orders = restClient.getOrders(date, restWebsite);
        NamedRegion centralArea = restClient.getCentralArea(restWebsite);
        NamedRegion[] noFlyZones = restClient.noFlyZones(restWebsite);

        //Validate Orders
        Order[] validatedOrders = getValidatedOrders(orders, restaurants);

        //Calculate the flight path
        FlightPathCalculator flightPathCalculator = new FlightPathCalculator();

    }
}
