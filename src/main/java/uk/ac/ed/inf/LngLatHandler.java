package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

import static java.lang.Math.sqrt;

public class LngLatHandler implements LngLatHandling {

    /**Checks the distance to a position from a start position.
     * @param startPosition - The starting position of the drone.
     * @param endPosition - The end position of the drone.
     * @return The distance between the two positions.
     */
    @Override
    public double distanceTo(LngLat startPosition, LngLat endPosition) {
        double distanceLng = endPosition.lng() - startPosition.lng();
        double distanceLat = endPosition.lat() - startPosition.lat();

        return sqrt((distanceLat*distanceLat) + (distanceLng*distanceLng));
    }

    /**Checks if start position is close to the other position.
     * @param startPosition - The starting position of the drone.
     * @param otherPosition - The other position of the drone.
     * @return True if positions are close together.
     */
    @Override
    public boolean isCloseTo(LngLat startPosition, LngLat otherPosition) {
        double distanceTol = distanceTo(startPosition, otherPosition);

        return distanceTol < 0.00015;
    }

    /**Checks if a given position is in a given region.
     * @param position The position to be checked.
     * @param region The provided region.
     * @return True if the position is in the region
     */
    @Override
    public boolean isInRegion(LngLat position, NamedRegion region) {
        int intersections = 0;
        for (int i = 0; i < region.vertices().length; i++) {
            LngLat v1 = region.vertices()[i];
            LngLat v2 = region.vertices()[(i + 1) % region.vertices().length];
            if ( (v1.lng() > position.lng()) != (v2.lng() > position.lng()) &&
                    (position.lat() < (v2.lat() - v1.lat()) * ((position.lng() - v1.lng())/(v2.lng() - v1.lng())) + v1.lat()) ) {
                intersections += 1;
            }
        }
        return (intersections % 2) == 1;
    }

    /**Calculates the next position of the drone.
     * @param startPosition Where the drone is currently.
     * @param angle The angle the drone is flying in.
     * @return The next LngLat position of the drone.
     */
    @Override
    public LngLat nextPosition(LngLat startPosition, double angle) {
        double nextLng;
        double nextLat;

        //If the angle is one of the four main directions (North, South, East, West)
        //North
        if (angle == 90) {
            nextLng = startPosition.lng();
            nextLat = startPosition.lat() + 0.00015;
        }
        //West
        else if (angle == 180) {
            nextLng = startPosition.lng() - 0.00015;
            nextLat = startPosition.lat();
        }
        //South
        else if (angle == 270) {
            nextLng = startPosition.lng();
            nextLat = startPosition.lat() - 0.00015;
        }
        //East
        else if (angle == 0) {
            nextLng = startPosition.lng() + 0.00015;
            nextLat = startPosition.lat();
        }

        //If it is not one of these directions
        else {
            double distanceToLat = Math.abs(Math.sin(angle % 90)*0.00015);
            double distanceToLng = Math.abs(Math.cos(angle % 90)*0.00015);

            //Between East and North
            if (angle < 90 && angle > 0) {
                nextLng = startPosition.lng() + distanceToLng;
                nextLat = startPosition.lat() + distanceToLat;
            }

            //Between North and West
            else if (angle > 90 && angle < 180) {
                nextLng = startPosition.lng() - distanceToLng;
                nextLat = startPosition.lat() + distanceToLat;
            }

            //Between West and South
            else if (angle > 180 && angle < 270) {
                nextLng = startPosition.lng() - distanceToLng;
                nextLat = startPosition.lat() - distanceToLat;
            }

            //Between South and East
            else {
                nextLng = startPosition.lng() + distanceToLng;
                nextLat = startPosition.lat() - distanceToLat;
            }

        }
        return new LngLat(nextLng, nextLat);
    }
}