import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ed.inf.LngLatHandler;
import uk.ac.ed.inf.OrderValidator;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.*;

import javax.naming.Name;
import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class LngLatHandlerTest {
    private LngLatHandler lngLatHandler;
    private LngLat appleTonTower = new LngLat(-3.186874, 55.944494);
    private LngLat civerinos = new LngLat(-3.1912869215011597, 55.945535152517735);

    private LngLat soraLellaVeganRestaurant = new LngLat( -3.202541470527649, 55.943284737579376);
    private LngLat dominos = new LngLat(-3.1838572025299072, 55.94449876875712);

    private LngLat soderberg = new LngLat( -3.1940174102783203, 55.94390696616939);

    private LngLat[] vertices = {new LngLat( -3.192473,55.946233), new LngLat( -3.184319, 55.946233),
                                 new LngLat( -3.184319,55.942617), new LngLat( -3.192473, 55.942617)};
    private NamedRegion region = new NamedRegion("Central Area", vertices);

    @Before
    public void setUp() {
         lngLatHandler = new LngLatHandler();
    }

    @Test
    public void testDistanceTo() {
        assertEquals(0.004534079261, lngLatHandler.distanceTo(appleTonTower, civerinos), 0.000000000001);
    }

    @Test
    public void testDistanceIsZero() {
        assertEquals(0, lngLatHandler.distanceTo(appleTonTower, appleTonTower), 0.000000000001);
    }

    @Test
    public void isCloseTo() {
        assertEquals(true, lngLatHandler.isCloseTo(appleTonTower, appleTonTower));
    }

    @Test
    public void isNotCloseTo() {
        assertEquals(false, lngLatHandler.isCloseTo(appleTonTower, civerinos));
    }

    @Test
    public void isAlmostCloseTo() {
        assertEquals(false, lngLatHandler.isCloseTo(new LngLat(0.00015, 0), new LngLat(0,0)));
    }

    @Test
    public void isJustCloseTo() {
        assertEquals(true, lngLatHandler.isCloseTo(new LngLat(0, 0.00014), new LngLat(0,0)));
    }

    @Test
    public void testInRegion() {
        assertEquals(true, lngLatHandler.isInRegion(new LngLat( -3.188396, 55.944425), region));
    }

    @Test
    public void isNotInRegion() {
        assertEquals(false, lngLatHandler.isInRegion(soderberg, region));
    }

    @Test
    public void isOnRegionLine() {
        assertEquals(false, lngLatHandler.isInRegion(new LngLat(55.946233, -3.192473), region));
    }

    @Test
    public void nextPosition() {
        LngLat next = new LngLat( -3.186724, 55.944494);
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 90);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }


}
