package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.io.IOException;
import java.net.URL;

public class RestClient {
    public static final String RESTAURANT_URL = "/restaurants";
    public static final String ORDER_URL = "/orders";
    public static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Check if the REST service is alive
     * @param url The URL of the REST service
     * @return true if the REST is alive.
     */
    public static boolean isAlive(String url) {
        boolean isAlive;
        try {
            isAlive = mapper.readValue(new URL(url + "/isAlive"), boolean.class);
            System.out.println("REST service is alive");
        } catch (IOException e) {
            isAlive = false;
        }

        return isAlive;
    }

    /** Gets the list of restaurants from the web server
     * @return The List of restaurants
     */
    public static Restaurant[] getRestaurants(String url) {
        Restaurant[] restaurants;
        try {
            restaurants = mapper.readValue(new URL(url + RESTAURANT_URL), Restaurant[].class);
            System.out.println("read all restaurants");
        } catch (IOException e) {
            throw new RuntimeException("Could not retrieve restaurants from REST.");
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
        mapper.registerModule(new JavaTimeModule());

        try {
            orders = mapper.readValue(new URL(url + ORDER_URL + "/" + date), Order[].class);
            System.out.println("read all orders");
        } catch (IOException e) {
            throw new RuntimeException("Could not retrieve orders from REST");
        }

        return orders;
    }

    public static NamedRegion getCentralArea(String url) {
        NamedRegion centralArea;
        try {
            centralArea = mapper.readValue(new URL(url + "/centralArea"), NamedRegion.class);
        }
        catch (IOException e) {
            throw new RuntimeException("Could not retrieve central area from REST");
        }
        return centralArea;
    }

    public static NamedRegion[] getNoFlyZones(String url) {
        NamedRegion[] noFlyZones;
        try {
            noFlyZones = mapper.readValue(new URL(url + "/noFlyZones"), NamedRegion[].class);
        }
        catch (IOException e) {
            throw new RuntimeException("Could not retrieve no-fly zones from REST");
        }
        return noFlyZones;
    }

}
