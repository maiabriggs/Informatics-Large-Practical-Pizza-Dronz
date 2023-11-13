package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.Console;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

public class RestClient {
    public static final String RESTAURANT_URL = "/restaurants";
    public static final String ORDER_URL = "/orders";

    /** Gets the list of restaurants from the web server
     * @return The List of restaurants
     */
    public static Restaurant[] getRestaurants(String url) {
        ObjectMapper mapper = new ObjectMapper();
        Restaurant[] restaurants;
        try {
            restaurants = mapper.readValue(new URL(url + RESTAURANT_URL), Restaurant[].class);
            System.out.println("read all restaurants");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return restaurants;
    }

    /**
     * Gets the Orders for a specified date time.
     * @param date - The date time orders should be retrieved from.
     * @return The list of orders
     */
    public static Order[] getOrders(String date, String url) {
        Order[] orders;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            orders = mapper.readValue(new URL(url + ORDER_URL + "/" + date), Order[].class);
            System.out.println("read all orders");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return orders;
    }


}
