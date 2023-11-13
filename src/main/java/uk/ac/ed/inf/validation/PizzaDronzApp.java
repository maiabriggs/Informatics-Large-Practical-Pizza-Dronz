package uk.ac.ed.inf.validation;

import uk.ac.ed.inf.RestClient;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PizzaDronzApp {
    public static void main(String[] args){
        if (args.length != 2) {
            System.out.println("Missing two arguments");
            System.exit(1);
        }

        String restWebsite = args[1];
        String date = args[0];

        //Get information from REST
        RestClient restClient = new RestClient();

        //Update Restaurants and Orders from REST
        Restaurant[] restaurants = restClient.getRestaurants(restWebsite);
        Order[] orders = restClient.getOrders(date, restWebsite);

        //Validate orders


    }
}
