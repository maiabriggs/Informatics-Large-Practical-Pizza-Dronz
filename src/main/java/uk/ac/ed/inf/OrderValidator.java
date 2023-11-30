package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.interfaces.OrderValidation;
import uk.ac.ed.inf.validation.CardValidator;
import uk.ac.ed.inf.validation.PizzaValidator;
import uk.ac.ed.inf.validation.RestaurantValidator;

import java.util.Arrays;

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

    public static Order[] getValidatedOrders(Order[] ordersToValidate, Restaurant[] restaurants) {
        OrderValidator orderValidator = new OrderValidator();

        for (Order value : ordersToValidate) {
            orderValidator.validateOrder(value, restaurants);
        }

        return Arrays.stream(ordersToValidate)
                .filter(order -> order.getOrderValidationCode() == OrderValidationCode.NO_ERROR)
                .toList().toArray(Order[]::new);
    }

    /**
     * Sets the order status of an order to 'DELIVERED'
     * @param order The order that has been delivered
     */
    public static void orderDelivered(Order order) {
        order.setOrderStatus(OrderStatus.DELIVERED);
    }

}
