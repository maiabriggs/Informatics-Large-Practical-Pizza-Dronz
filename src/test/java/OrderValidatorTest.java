import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ed.inf.OrderValidator;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class OrderValidatorTest {
    private Order order;
    private OrderValidator orderValidator;
    private Restaurant[] restaurants = {new Restaurant("Civerinos Slice",new LngLat(-3.1912869215011597,55.945535152517735),
            new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
            new Pizza[]{new Pizza("Margarita",1000), new Pizza("Calzone",1400)}),

            new Restaurant("Sora Lella Vegan Restaurant", new LngLat(-3.202541470527649,55.943284737579376),
                    new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY},
                    new Pizza[]{new Pizza("Meat Lover",1400), new Pizza("Vegan Delight",1100)}),

            new Restaurant("Domino's Pizza - Edinburgh - Southside",new LngLat(-3.1838572025299072,55.94449876875712),
                    new DayOfWeek[]{DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                    new Pizza[]{new Pizza("Super Cheese",1400), new Pizza("All Shrooms",900)}),

            new Restaurant("Sodeberg Pavillion", new LngLat(-3.1940174102783203,55.94390696616939),
                    new DayOfWeek[]{DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                    new Pizza[]{new Pizza("Proper Pizza",1400), new Pizza("Pineapple & Ham & Cheese",900)})};

    @Before
    public void setUp() {
        orderValidator = new OrderValidator();
    }

    @Test
    public void testUndefined() {
        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)}, new CreditCardInformation("13499472696504",
                "06/28", "952"));

        assertEquals(OrderValidationCode.UNDEFINED, order.getOrderValidationCode());
    }

    @Test
    public void testNoError() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.NO_ERROR, order.getOrderValidationCode());
    }

    @Test
    public void cardTooShortInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cardTooLongInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("13499472696505543534534435","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cardNotNumInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1111mmmmmm222222","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void expiryDateInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/21", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void expiryDateMonthInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","09/23", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cvvInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/21", "9524"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.CVV_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cvvTooShortInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/21", "95"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.CVV_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cvvNotNumInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/21", "95e"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.CVV_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void totalIncorrect() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 0, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.TOTAL_INCORRECT, order.getOrderValidationCode());
    }

    @Test
    public void testPizzaUndefined() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Pizza Chives", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.PIZZA_NOT_DEFINED, order.getOrderValidationCode());
    }

    @Test
    public void tooManyPizza() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 5600, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900), new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED, order.getOrderValidationCode());
    }

    @Test
    public void testPizzaMultipleRestaurants() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("Pineapple & Ham & Cheese", 900)},
                new CreditCardInformation("1349947269650466","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS, order.getOrderValidationCode());
    }

    @Test
    public void restaurantClosed() {

        order = new Order("19514FE0", LocalDate.of(2023, 10, 16), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.RESTAURANT_CLOSED, order.getOrderValidationCode());
    }


}
