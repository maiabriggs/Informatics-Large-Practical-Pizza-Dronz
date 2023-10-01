package uk.ac.ed.inf.validation;

import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.Pizza;

public class PizzaValidator {

    /** Validates if the order total is correct
     * @param totalPriceInPence  The price of the order
     * @return true if order is valid**/
    //TODO: Sort TOTAL ORDER
    private OrderValidationCode validateOrderTotal(int totalPriceInPence) {
        return OrderValidationCode.TOTAL_INCORRECT;
    }

    //TODO: Defined Pizzas
    /** Validates if all pizzas are defined
     * @param pizzas The pizzas in the order
     * @return true if all are valid
     */
    private OrderValidationCode validatePizzas(Pizza[] pizzas) {
        return OrderValidationCode.PIZZA_NOT_DEFINED;
    }

    //TODO: Max num of pizzas not exceeded
    /** Validates that the max number of pizzas has not been exceeded
     * @param pizzas The pizzas in the order
     * @return true if correct number of pizzas
     */
    private OrderValidationCode validatePizzaNum(Pizza[] pizzas) {
        return OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED;
    }

}
