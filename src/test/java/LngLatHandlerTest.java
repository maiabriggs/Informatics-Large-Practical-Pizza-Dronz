import org.junit.Before;
import org.junit.Test;
import uk.ac.ed.inf.LngLatHandler;
import uk.ac.ed.inf.ilp.data.*;

import static org.junit.Assert.*;

public class LngLatHandlerTest {
    private LngLatHandler lngLatHandler;
    private final LngLat appleTonTower = new LngLat(-3.186874, 55.944494);
    private final LngLat civerinos = new LngLat(-3.1912869215011597, 55.945535152517735);
    private final LngLat soderberg = new LngLat( -3.1940174102783203, 55.94390696616939);

    private final LngLat[] vertices = {new LngLat( -3.192473,55.946233), new LngLat( -3.184319, 55.946233),
            new LngLat( -3.184319,55.942617), new LngLat( -3.192473, 55.942617)};
    private final NamedRegion region = new NamedRegion("Central Area", vertices);

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
        assertTrue(lngLatHandler.isCloseTo(appleTonTower, appleTonTower));
    }

    @Test
    public void isNotCloseTo() {
        assertFalse(lngLatHandler.isCloseTo(appleTonTower, civerinos));
    }

    @Test
    public void isAlmostCloseTo() {
        assertFalse(lngLatHandler.isCloseTo(new LngLat(0.00015, 0), new LngLat(0, 0)));
    }

    @Test
    public void isJustCloseTo() {
        assertTrue(lngLatHandler.isCloseTo(new LngLat(0, 0.00014), new LngLat(0, 0)));
    }

    @Test
    public void testInRegion() {
        assertTrue(lngLatHandler.isInRegion(new LngLat(-3.188396, 55.944425), region));
    }

    @Test
    public void isNotInRegion() {
        assertFalse(lngLatHandler.isInRegion(soderberg, region));
    }

    @Test
    public void isOnRegionLine() {
        assertFalse(lngLatHandler.isInRegion(new LngLat(55.946233, -3.192473), region));
    }

    @Test
    public void nextPositionNorth() {
        LngLat next = new LngLat( -3.186874, 55.944644);
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 90);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }

    @Test
    public void nextPositionSouth() {
        LngLat next = new LngLat( -3.186874, (55.944494 - 0.00015));
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 270);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }

    @Test
    public void nextPositionEast() {
        LngLat next = new LngLat( (-3.186874 + 0.00015), 55.944494);
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 0);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }

    @Test
    public void nextPositionWest() {
        LngLat next = new LngLat( (-3.186874 - 0.00015), 55.944494);
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 180);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }

    @Test
    public void nextPositionENE() {
        LngLat next = new LngLat((-3.186874 + Math.abs((Math.cos(22.5) * 0.00015))), (55.944494 + Math.abs((Math.sin(22.5) * 0.00015))));
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 22.5);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }

    @Test
    public void nextPositionNE() {
        LngLat next = new LngLat((-3.186874 + Math.abs((Math.cos(45) * 0.00015))), (55.944494 + Math.abs((Math.sin(45) * 0.00015))));
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 45);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }

    @Test
    public void nextPositionNNW() {
        LngLat next = new LngLat((-3.186874 - Math.abs((Math.cos(112.5 % 90) * 0.00015))), (55.944494 + Math.abs((Math.sin(112.5 % 90) * 0.00015))));
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 112.5);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }

    @Test
    public void nextPositionNW() {
        LngLat next = new LngLat((-3.186874 - Math.abs((Math.cos(135 % 90) * 0.00015))), (55.944494 + Math.abs((Math.sin(135 % 90) * 0.00015))));
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 135);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }

    @Test
    public void nextPositionWNW() {
        LngLat next = new LngLat((-3.186874 - Math.abs((Math.cos(147.5 % 90) * 0.00015))), (55.944494 + Math.abs((Math.sin(147.5 % 90) * 0.00015))));
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 147.5);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }

    @Test
    public void nextPositionWSW() {
        LngLat next = new LngLat((-3.186874 - Math.abs((Math.cos(202.5 % 90) * 0.00015))), (55.944494 - Math.abs((Math.sin(202.5 % 90) * 0.00015))));
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 202.5);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }

    @Test
    public void nextPositionSW() {
        LngLat next = new LngLat((-3.186874 - Math.abs((Math.cos(215 % 90) * 0.00015))), (55.944494 - Math.abs((Math.sin(215 % 90) * 0.00015))));
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 215);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }

    @Test
    public void nextPositionSSW() {
        LngLat next = new LngLat((-3.186874 - Math.abs((Math.cos(237.5 % 90) * 0.00015))), (55.944494 - Math.abs((Math.sin(237.5 % 90) * 0.00015))));
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 237.5);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }

    @Test
    public void nextPositionSSE() {
        LngLat next = new LngLat((-3.186874 + Math.abs((Math.cos(292.5 % 90) * 0.00015))), (55.944494 - Math.abs((Math.sin(292.5 % 90) * 0.00015))));
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 292.5);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }

    @Test
    public void nextPositionSE() {
        LngLat next = new LngLat((-3.186874 + Math.abs((Math.cos(315 % 90) * 0.00015))), (55.944494 - Math.abs((Math.sin(315 % 90) * 0.00015))));
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 315);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }

    @Test
    public void nextPositionESE() {
        LngLat next = new LngLat((-3.186874 + Math.abs((Math.cos(337.5 % 90) * 0.00015))), (55.944494 - Math.abs((Math.sin(337.5 % 90) * 0.00015))));
        LngLat found = lngLatHandler.nextPosition(appleTonTower, 337.5);
        assertEquals(next.lng(), found.lng(), 0.000000000001);
        assertEquals(next.lat(), found.lat(), 0.000000000001);
    }

}