import org.junit.Before;
import org.junit.Test;
import uk.ac.ed.inf.LngLatHandler;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import uk.ac.ed.inf.RestClient;


public class RestClientTest {
    RestClient restClient;

    private final Restaurant[] RESTAURANT_LIST = {new Restaurant("Civerinos Slice", new LngLat(-3.1912869215011597,55.945535152517735),
            new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
            new Pizza[]{new Pizza("Margarita",1000), new Pizza("Calzone",1400)}),

            new Restaurant("Sora Lella Vegan Restaurant", new LngLat(-3.202541470527649, 55.943284737579376),
                    new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY},
                    new Pizza[]{new Pizza("Meat Lover",1400), new Pizza("Vegan Delight",1100)}),

            new Restaurant("Domino's Pizza - Edinburgh - Southside", new LngLat(-3.1838572025299072, 55.94449876875712),
                    new DayOfWeek[]{DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                    new Pizza[]{new Pizza("Super Cheese",1400), new Pizza("All Shrooms",900)}),

            new Restaurant("Sodeberg Pavillion", new LngLat(-3.1940174102783203,55.94390696616939),
                    new DayOfWeek[]{DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                    new Pizza[]{new Pizza("Proper Pizza",1400), new Pizza("Pineapple & Ham & Cheese",900)})};






    @Before
    public void setUp() {
        restClient = new RestClient();
    }
    @Test
    public void testGetRestaurants(){

        Restaurant[] restaurants = restClient.getRestaurants();

        System.out.println(restaurants[0].menu()[0].name());
        System.out.println(RESTAURANT_LIST[0].menu()[0].name());


        assertEquals(RESTAURANT_LIST, restaurants);
    }
}
