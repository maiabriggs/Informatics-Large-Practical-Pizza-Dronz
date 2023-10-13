package uk.ac.ed.inf.validation;

import uk.ac.ed.inf.OrderValidator;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class RestaurantValidator {

    /** Validates that the pizzas were ordered from the same restaurant
     * @param restaurants The list of restaurants
     * @return order validation status
     */
    private OrderValidationCode validateRestaurants(Restaurant[] restaurants, Pizza[] pizzas) {
        for (int i = 0; i < restaurants.length; i++) {
            boolean allPizzaInSameRest = true;
            Pizza[] menu = restaurants[i].menu();
            for (int j = 0; j < pizzas.length; j++) {
                for (int m = 0; m < menu.length; m++) {
                    if (!menu[m].equals(pizzas[j])) {
                        allPizzaInSameRest = false;
                    }
                }
            }
            if (allPizzaInSameRest == true) {
                return OrderValidationCode.NO_ERROR;
            }
        }
        return OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS;
    }

    /** Validates if he restaurant is open
     * @param restaurants The restaurants
     * @param orderDay The day the order was made
     * @return order validation status
     */
    private OrderValidationCode validateRestaurantOpen(Restaurant[] restaurants, LocalDate orderDay, Pizza[] pizzas) {
        Restaurant restaurant = null;
        for (int i = 0; i < restaurants.length; i++) {
            //Check which restaurant the pizza came from
            Pizza[] menu = restaurants[i].menu();
            for (int m = 0; m < menu.length; m++) {
                if (pizzas[0].equals(menu[m])) {
                    restaurant = restaurants[i];
                    break;
                }
            }
        }

        DayOfWeek[] openDays = restaurant.openingDays();
        for (int i = 0; i < openDays.length; i++) {
            if (openDays[i] == orderDay.getDayOfWeek()) {
                return OrderValidationCode.NO_ERROR;
            }
        }

        return OrderValidationCode.RESTAURANT_CLOSED;
    }

}
