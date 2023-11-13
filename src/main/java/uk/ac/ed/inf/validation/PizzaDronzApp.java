package uk.ac.ed.inf.validation;

import uk.ac.ed.inf.OrderValidator;
import uk.ac.ed.inf.RestClient;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

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

        //Validate Orders
        Order[] validatedOrders = getValidatedOrders(orders, restaurants);
    }


    public static Order[] getValidatedOrders(Order[] ordersToValidate, Restaurant[] restaurants) {
        OrderValidator orderValidator = new OrderValidator();

        for (int i = 0; i < ordersToValidate.length; i++) {
            orderValidator.validateOrder(ordersToValidate[i], restaurants);
        }

        Order[] orders = Arrays.stream(ordersToValidate)
                .filter(order -> order.getOrderValidationCode() == OrderValidationCode.NO_ERROR)
                .collect(Collectors.toList()).toArray(Order[]::new);

        return orders;
    }
}
