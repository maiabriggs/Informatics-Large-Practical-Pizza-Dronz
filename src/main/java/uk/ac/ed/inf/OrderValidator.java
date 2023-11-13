package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.interfaces.OrderValidation;
import uk.ac.ed.inf.ilp.constant.*;
import uk.ac.ed.inf.ilp.data.*;
import uk.ac.ed.inf.validation.CardValidator;
import uk.ac.ed.inf.validation.PizzaValidator;
import uk.ac.ed.inf.validation.RestaurantValidator;

public class OrderValidator implements OrderValidation {
    @Override
    public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants) {
        OrderValidationCode creditStatus = new CardValidator().cardValidator(orderToValidate.getCreditCardInformation());
        if (creditStatus != OrderValidationCode.NO_ERROR) {
            orderToValidate.setOrderValidationCode(creditStatus);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        OrderValidationCode pizzaStatus = new PizzaValidator().pizzaValidator(orderToValidate.getPizzasInOrder(), orderToValidate.getPriceTotalInPence(), definedRestaurants);
        if (pizzaStatus != OrderValidationCode.NO_ERROR) {
            orderToValidate.setOrderValidationCode(pizzaStatus);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        OrderValidationCode restStatus = new RestaurantValidator().restaurantValidator(definedRestaurants, orderToValidate.getPizzasInOrder(), orderToValidate.getOrderDate());
        if (restStatus != OrderValidationCode.NO_ERROR) {
            orderToValidate.setOrderValidationCode(restStatus);
            orderToValidate.setOrderStatus(OrderStatus.INVALID);
            return orderToValidate;
        }

        orderToValidate.setOrderValidationCode(OrderValidationCode.NO_ERROR);
        orderToValidate.setOrderStatus(OrderStatus.VALID_BUT_NOT_DELIVERED);
        return orderToValidate;
    }

}
