package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.CreditCardInformation;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.interfaces.OrderValidation;
import uk.ac.ed.inf.ilp.constant.*;
import uk.ac.ed.inf.ilp.data.*;
import uk.ac.ed.inf.validation.CardValidator;
import uk.ac.ed.inf.validation.PizzaValidator;

/**
 * a sample order validator which does nothing
 */
public class OrderValidator implements OrderValidation {
    @Override
    public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants) {
        OrderValidationCode status = new CardValidator().validateCreditNumber(orderToValidate.getCreditCardInformation());
        return orderToValidate;
    }

}
