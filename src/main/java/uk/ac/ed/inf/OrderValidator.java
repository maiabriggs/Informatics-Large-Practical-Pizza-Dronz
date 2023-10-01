package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.interfaces.OrderValidation;
import uk.ac.ed.inf.ilp.constant.*;

/**
 * a sample order validator which does nothing
 */
public class OrderValidator implements OrderValidation {
    @Override
    public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants) {
        orderToValidate.setOrderValidationCode(OrderValidationCode.NO_ERROR);
        return orderToValidate;
    }
}
