package uk.ac.ed.inf.validation;

import uk.ac.ed.inf.OrderValidator;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.CreditCardInformation;
import uk.ac.ed.inf.ilp.data.Pizza;
import uk.ac.ed.inf.ilp.data.Restaurant;

public class PizzaValidator {

    /** Checks all credit card validation
     * @return order validation status
     */
    public OrderValidationCode pizzaValidator(Pizza[] pizzas, int totalPrice, Restaurant[] restaurants) {
        if (validateOrderTotal(totalPrice, pizzas) != OrderValidationCode.NO_ERROR) {
            return validateOrderTotal(totalPrice, pizzas);
        }
        else if (validatePizzas(pizzas, restaurants) != OrderValidationCode.NO_ERROR) {
            return validatePizzas(pizzas, restaurants);
        }
        else if (validatePizzaNum(pizzas) != OrderValidationCode.NO_ERROR) {
            return validatePizzaNum(pizzas);
        }
        else {
            return OrderValidationCode.NO_ERROR;
        }
    }

    /** Validates if the order total is correct
     * @param totalPrice  The price of the order
     * @param pizzas      The pizzas in the order
     * @return @return order validation status
     */
    private OrderValidationCode validateOrderTotal(int totalPrice, Pizza[] pizzas) {
        int pizzaTotal = 0;
        for (int i = 0; i < pizzas.length; i++) {
            pizzaTotal += pizzas[i].priceInPence();
        }

        if (pizzaTotal != (totalPrice - 1)) {
            return OrderValidationCode.TOTAL_INCORRECT;
        }
        else {
            return OrderValidationCode.NO_ERROR;
        }
    }

    /** Validates if all pizzas are sold by the available restaurants
     * @param pizzas The pizzas in the order
     * @param restaurants The list of restaurants.
     * @return order validation status
     */
    private OrderValidationCode validatePizzas(Pizza[] pizzas, Restaurant[] restaurants) {
        for (int i = 0; i < pizzas.length; i++) {
            boolean inMenu = false;
            for (int r = 0; r < restaurants.length; r++) {
                Pizza[] menu = restaurants[i].menu();
                for (int m = 0; m < menu.length; m++) {
                    if (menu[m].equals(pizzas[i])) {
                        inMenu = true;
                        break;
                    }
                }
            }
            if (!inMenu) {
                return OrderValidationCode.PIZZA_NOT_DEFINED;
            }
        }
        return  OrderValidationCode.NO_ERROR;
    }


    /** Validates that the max number of pizzas has not been exceeded
     * @param pizzas The pizzas in the order
     * @return order validation status
     */
    private OrderValidationCode validatePizzaNum(Pizza[] pizzas) {
        if (pizzas.length > 4) {
            return OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED;
        }
        else {
            return OrderValidationCode.NO_ERROR;
        }
    }
}
