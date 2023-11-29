package uk.ac.ed.inf.validation;

import uk.ac.ed.inf.OrderValidator;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class RestaurantValidator {

    public OrderValidationCode restaurantValidator(Restaurant[] restaurants, Pizza[] pizzas, LocalDate orderDay) {
        if (validateRestaurants(restaurants, pizzas) != OrderValidationCode.NO_ERROR) {
            return validateRestaurants(restaurants, pizzas);
        }
        else if (validateRestaurantOpen(restaurants, orderDay, pizzas) != OrderValidationCode.NO_ERROR) {
            return (validateRestaurantOpen(restaurants, orderDay, pizzas));
        }
        else {
            return OrderValidationCode.NO_ERROR;
        }
    }


    /** Validates that the pizzas were ordered from the same restaurant
     * @param restaurants The list of restaurants
     * @return order validation status
     */
    private OrderValidationCode validateRestaurants(Restaurant[] restaurants, Pizza[] pizzas) {
        for (int i = 0; i < pizzas.length - 1; i++) {
            Restaurant restaurant = findRestaurant(pizzas[i], restaurants);
            Restaurant nextRestaurant = findRestaurant(pizzas[i+1], restaurants);

            if (restaurant != nextRestaurant) {
                return OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS;
            }
        }
        return OrderValidationCode.NO_ERROR;
    }

    /** Validates if he restaurant is open
     * @param restaurants The restaurants
     * @param orderDay The day the order was made
     * @return order validation status
     */
    private OrderValidationCode validateRestaurantOpen(Restaurant[] restaurants, LocalDate orderDay, Pizza[] pizzas) {
        Restaurant restaurant = findRestaurant(pizzas[0], restaurants);

        DayOfWeek[] openDays = restaurant.openingDays();
        for (int i = 0; i < openDays.length; i++) {
            if (openDays[i] == orderDay.getDayOfWeek()) {
                return OrderValidationCode.NO_ERROR;
            }
        }

        return OrderValidationCode.RESTAURANT_CLOSED;
    }

    /** Checks which restaurant a pizza came from
     * @param pizza - The pizza we want to check
     * @param restaurants  - The list of operating restaurants
     * @return The restaurant the pizza is sold at
     */
    public static Restaurant findRestaurant(Pizza pizza, Restaurant[] restaurants) {
        Restaurant restaurant = null;
        for (int i = 0; i < restaurants.length; i++) {
            //Check which restaurant the pizza came from
            Pizza[] menu = restaurants[i].menu();
            for (int m = 0; m < menu.length; m++) {
                if (pizza.name().equals(menu[m].name())) {
                    restaurant = restaurants[i];
                    break;
                }
            }
        }
        return restaurant;
    }

}
