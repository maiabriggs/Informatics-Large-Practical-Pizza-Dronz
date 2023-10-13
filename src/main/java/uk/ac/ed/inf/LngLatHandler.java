package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

import static java.lang.Math.sqrt;

public class LngLatHandler implements LngLatHandling {

    /**Checks the distance to a position from a start position
     * @param startPosition - The starting position of the drone.
     * @param endPosition - The end position of the drone.
     * @return The distance between the two positions.
     */
    @Override
    public double distanceTo(LngLat startPosition, LngLat endPosition) {
        double distanceLng = endPosition.lng() - startPosition.lng();
        double distanceLat = endPosition.lat() - startPosition.lat();
        double distanceBetween = sqrt((distanceLat*distanceLat) + (distanceLng*distanceLng));

        return distanceBetween;
    }

    /**Checks if start position is close to the other position
     * @param startPosition - The starting position of the drone.
     * @param otherPosition - The other position of the drone
     * @return True if positions are close together.
     */
    @Override
    public boolean isCloseTo(LngLat startPosition, LngLat otherPosition) {
        double distanceTol = distanceTo(startPosition, otherPosition);

        if (distanceTol < 0.00015) {
            return true;
        }
        else {
            return false;
        }
    }


    @Override
    public boolean isInRegion(LngLat position, NamedRegion region) {
        int intersections = 0;
        for (int i = 0; i < region.vertices().length; i++) {
            LngLat v1 = region.vertices()[i];
            LngLat v2 = region.vertices()[(i + 1) % region.vertices().length];
            if ( (v1.lng() > position.lng()) != (v2.lng() > position.lng()) &&
                    (position.lat() < (v2.lat() - v1.lat()) * ((position.lng() - v1.lng())/(v2.lng() - v1.lng())) + v1.lat()) ) {
            }
            intersections += 1;
        }
        return (intersections % 2) == 1;
    }

    @Override
    public LngLat nextPosition(LngLat startPosition, double angle) {
        double nextLat = Math.acos(0.00015);
        double nextLng = Math.asin(0.00015);

        LngLat position = new LngLat(nextLat, nextLng);
        return position;
    }
}
