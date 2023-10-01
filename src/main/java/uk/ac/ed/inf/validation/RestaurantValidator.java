package uk.ac.ed.inf.validation;

import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.Restaurant;

public class RestaurantValidator {

    //TODO: Pizzas not from multiple restaurants
    /** Validates that the pizzas were ordered from the same restaurant
     * @param restaurants The restaurants ordered from
     * @return true if one restaurant
     */
    private OrderValidationCode validateRestaurants(Restaurant[] restaurants) {
        return OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS;
    }

    //TODO: Validate if restaurant is open
    /** Validates if he restaurant is open
     * @param restaurant The restaurant in order
     * @return true if restaurant is open
     */
    private OrderValidationCode validateRestaurantOpen(Restaurant[] restaurant) {
        return OrderValidationCode.RESTAURANT_CLOSED;
    }

}
