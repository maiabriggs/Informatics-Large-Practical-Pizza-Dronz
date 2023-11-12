package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.io.Console;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;

public class RestClient {

    public static final String REST_URL = "https://ilp-rest.azurewebsites.net";
    public static final String RESTAURANT_URL = "/restaurants";
    public static final String ORDER_URL = "/orders";

    /** Gets the list of restaurants from the web server
     * @return The List of restaurants
     */
    public static Restaurant[] getRestaurants() {
        ObjectMapper mapper = new ObjectMapper();
        Restaurant[] restaurants;
        try {
            restaurants = mapper.readValue(new URL(REST_URL + RESTAURANT_URL), Restaurant[].class);
            System.out.println("read all restaurants");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return restaurants;
    }

    /**
     * Gets the Orders for a specified date time.
     * @param dateTime - The date time orders should be retrieved from.
     * @return The list of orders
     */
    public static Order[] getOrders(LocalDateTime dateTime) {
        Order[] orders;
        ObjectMapper mapper = new ObjectMapper();

        try {
            orders = mapper.readValue(new URL(REST_URL + ORDER_URL), Order[].class);
            System.out.println("read all orders");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < orders.length; i++){

        }

        return orders;
    }

}
