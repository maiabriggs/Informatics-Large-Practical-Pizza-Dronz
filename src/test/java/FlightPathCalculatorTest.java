import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ed.inf.FlightPathCalculator;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import java.io.IOException;

public class FlightPathCalculatorTest {

    private Order order;
    private Restaurant[] restaurants;

    private NamedRegion centralArea;
    private NamedRegion[] noFlyZones;
    private static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);

    @Before
    public void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            String ORDER = "{\"orderNo\":\"45C59C9C\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"7562\",\"creditCardExpiry\":\"09/24\",\"cvv\":\"159\"}},{\"orderNo\":\"0086F124\",\"orderDate\":\"2023-09-01\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4299725940827052\",\"creditCardExpiry\":\"07/09\",\"cvv\":\"582\"}}";
            order = mapper.readValue(ORDER, Order.class);
            System.out.println("read the order");
            String RESTAURANT_LIST = "[{\"name\":\"Civerinos Slice\",\"location\":{\"lng\":-3.1912869215011597,\"lat\":55.945535152517735},\"openingDays\":[\"MONDAY\",\"TUESDAY\",\"FRIDAY\",\"SATURDAY\",\"SUNDAY\"],\"menu\":[{\"name\":\"R1: Margarita\",\"priceInPence\":1000},{\"name\":\"R1: Calzone\",\"priceInPence\":1400}]},{\"name\":\"Sora Lella Vegan Restaurant\",\"location\":{\"lng\":-3.202541470527649,\"lat\":55.943284737579376},\"openingDays\":[\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\"],\"menu\":[{\"name\":\"R2: Meat Lover\",\"priceInPence\":1400},{\"name\":\"R2: Vegan Delight\",\"priceInPence\":1100}]},{\"name\":\"Domino's Pizza - Edinburgh - Southside\",\"location\":{\"lng\":-3.1838572025299072,\"lat\":55.94449876875712},\"openingDays\":[\"WEDNESDAY\",\"THURSDAY\",\"FRIDAY\",\"SATURDAY\",\"SUNDAY\"],\"menu\":[{\"name\":\"R3: Super Cheese\",\"priceInPence\":1400},{\"name\":\"R3: All Shrooms\",\"priceInPence\":900}]},{\"name\":\"Sodeberg Pavillion\",\"location\":{\"lng\":-3.1940174102783203,\"lat\":55.94390696616939},\"openingDays\":[\"TUESDAY\",\"WEDNESDAY\",\"SATURDAY\",\"SUNDAY\"],\"menu\":[{\"name\":\"R4: Proper Pizza\",\"priceInPence\":1400},{\"name\":\"R4: Pineapple & Ham & Cheese\",\"priceInPence\":900}]},{\"name\":\"La Trattoria\",\"location\":{\"lng\":-3.1810810679852035,\"lat\":55.938910643735845},\"openingDays\":[\"MONDAY\",\"THURSDAY\",\"SATURDAY\",\"SUNDAY\"],\"menu\":[{\"name\":\"R5: Pizza Dream\",\"priceInPence\":1400},{\"name\":\"R5: My kind of pizza\",\"priceInPence\":900}]},{\"name\":\"Halal Pizza\",\"location\":{\"lng\":-3.185428203143916,\"lat\":55.945846113595},\"openingDays\":[\"MONDAY\",\"TUESDAY\",\"WEDNESDAY\",\"SATURDAY\",\"SUNDAY\"],\"menu\":[{\"name\":\"R6: Sucuk delight\",\"priceInPence\":1400},{\"name\":\"R6: Dreams of Syria\",\"priceInPence\":900}]},{\"name\":\"World of Pizza\",\"location\":{\"lng\":-3.179798972064253,\"lat\":55.939884084483},\"openingDays\":[\"THURSDAY\",\"FRIDAY\",\"TUESDAY\",null],\"menu\":[{\"name\":\"R7: Hot, hotter, the hottest\",\"priceInPence\":1400},{\"name\":\"R7: All you ever wanted\",\"priceInPence\":900}]}]";
            restaurants = mapper.readValue(RESTAURANT_LIST, Restaurant[].class);
            System.out.println("read the restaurants");
            String CENTRAL_AREA = "{\"name\":\"central\",\"vertices\":[{\"lng\":-3.192473,\"lat\":55.946233},{\"lng\":-3.192473,\"lat\":55.942617},{\"lng\":-3.184319,\"lat\":55.942617},{\"lng\":-3.184319,\"lat\":55.946233}]}";
            centralArea = mapper.readValue(CENTRAL_AREA, NamedRegion.class);
            System.out.println("read the central area");
            String NO_FLY_ZONES = "[{\"name\":\"George Square Area\",\"vertices\":[{\"lng\":-3.190578818321228,\"lat\":55.94402412577528},{\"lng\":-3.1899887323379517,\"lat\":55.94284650540911},{\"lng\":-3.187097311019897,\"lat\":55.94328811724263},{\"lng\":-3.187682032585144,\"lat\":55.944477740393744},{\"lng\":-3.190578818321228,\"lat\":55.94402412577528}]},{\"name\":\"Dr Elsie Inglis Quadrangle\",\"vertices\":[{\"lng\":-3.1907182931900024,\"lat\":55.94519570234043},{\"lng\":-3.1906163692474365,\"lat\":55.94498241796357},{\"lng\":-3.1900262832641597,\"lat\":55.94507554227258},{\"lng\":-3.190133571624756,\"lat\":55.94529783810495},{\"lng\":-3.1907182931900024,\"lat\":55.94519570234043}]},{\"name\":\"Bristo Square Open Area\",\"vertices\":[{\"lng\":-3.189543485641479,\"lat\":55.94552313663306},{\"lng\":-3.189382553100586,\"lat\":55.94553214854692},{\"lng\":-3.189259171485901,\"lat\":55.94544803726933},{\"lng\":-3.1892001628875732,\"lat\":55.94533688994374},{\"lng\":-3.189194798469543,\"lat\":55.94519570234043},{\"lng\":-3.189135789871216,\"lat\":55.94511759833873},{\"lng\":-3.188138008117676,\"lat\":55.9452738061846},{\"lng\":-3.1885510683059692,\"lat\":55.946105902745614},{\"lng\":-3.1895381212234497,\"lat\":55.94555918427592},{\"lng\":-3.189543485641479,\"lat\":55.94552313663306}]},{\"name\":\"Bayes Central Area\",\"vertices\":[{\"lng\":-3.1876927614212036,\"lat\":55.94520696732767},{\"lng\":-3.187555968761444,\"lat\":55.9449621408666},{\"lng\":-3.186981976032257,\"lat\":55.94505676722831},{\"lng\":-3.1872327625751495,\"lat\":55.94536993377657},{\"lng\":-3.1874459981918335,\"lat\":55.9453361389472},{\"lng\":-3.1873735785484314,\"lat\":55.94519344934259},{\"lng\":-3.1875935196876526,\"lat\":55.94515665035927},{\"lng\":-3.187624365091324,\"lat\":55.94521973430925},{\"lng\":-3.1876927614212036,\"lat\":55.94520696732767}]}]";
            noFlyZones = mapper.readValue(NO_FLY_ZONES, NamedRegion[].class);
            System.out.println("read the no fly zones");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCalculateFlightPath() {
        FlightPathCalculator.calculateFlightPath(order.getOrderNo(), APPLETON_TOWER, restaurants[3].location(), noFlyZones, centralArea);
    }

    //TODO: Find better tests
}
