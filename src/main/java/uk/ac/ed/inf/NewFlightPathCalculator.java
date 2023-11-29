package uk.ac.ed.inf;

import uk.ac.ed.inf.constant.Direction;
import uk.ac.ed.inf.data.Move;
import uk.ac.ed.inf.ilp.data.*;
import uk.ac.ed.inf.validation.RestaurantValidator;

import java.lang.reflect.Array;
import java.util.*;

public class NewFlightPathCalculator {

    private static PriorityQueue<Move> openSet;
    private static ArrayList<Move> closedSet;

    private static Move betterMove;

    private static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);

    private static LngLatHandler lngLatHandler;
    public static ArrayList<Move> createFlightPath(String orderNo, LngLat start, LngLat end, NamedRegion centralArea, NamedRegion[] noFly){
        lngLatHandler = new LngLatHandler();
        //We initialise the starting queue
        openSet = new PriorityQueue<>();
        closedSet = new ArrayList<>();

        //Add our starting move to the open list
        Move startMove = new Move(orderNo, start);
        openSet.add(startMove);
        System.out.println("...");
        int count = 0;

        while (!openSet.isEmpty()){
            Move currMove = openSet.poll();
            LngLat curr = currMove.getCurrLngLat();
            count += 1;
            for (Double direction : Direction.getAllDirections()) {
                //System.out.println("Direction: " + direction);
                LngLat neighbour = lngLatHandler.nextPosition(curr, direction);
                Move neighbourMove = new Move(orderNo, neighbour);
                neighbourMove.setAngle(direction);
                //System.out.println("Neighbour lng: " + neighbour.lng() + " lat: " + neighbour.lat());
                //Check if neighbour is the destination
                if (lngLatHandler.isCloseTo(end, neighbour) || count == 99999) {
                    //System.out.println("We have found our destination");
                    //Do all the things that should be done if we've found the path
                    neighbourMove.setParent(currMove);
                    return tracePath(neighbourMove);
                }

                if (!isOnClosedSet(neighbourMove) && !inNoFly(noFly, neighbour)) {
                    //Find g and h
                    //System.out.println("We have not already visited this neighbour and its not FORBIDDEN");
                    neighbourMove.setG(currMove.getG() + 1);
                    neighbourMove.setH(lngLatHandler.distanceTo(end, neighbour));

                    //Calculate total
                    neighbourMove.setTotal(neighbourMove.getG() + neighbourMove.getH());

                    if (isLowerOnOpenSet(neighbourMove)) {
                        //System.out.println("There's a better move");
                        neighbourMove.setParent(betterMove);
                        neighbourMove.setG(betterMove.getG() + 1);
                        neighbourMove.setTotal(neighbourMove.getG() + neighbourMove.getH());
                    }
                    else {
                        //System.out.println("No better move");
                        neighbourMove.setParent(currMove);
                        openSet.add(neighbourMove);

                    }
                }
            }
            //System.out.println("Finished assessing all neighbours! Mark as visited and REPEAT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            closedSet.add(currMove);
        }

        throw new RuntimeException("Cannot find a path");
    }

    /**
     * Checks if a node with the same position as the neighbour in the openSet list,
     * has a lower total than the neighbour.
     * @param neighbour
     * @return true if the total of neighbour is lower
     */
    public static boolean isLowerOnOpenSet(Move neighbour) {
        for (Move move : openSet) {
            if (lngLatHandler.isCloseTo(move.getCurrLngLat(), neighbour.getCurrLngLat()) && move.getTotal() <= neighbour.getTotal()) {
                betterMove = move;
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a node is in the closedSet
     * @param neighbour
     * @return true if the neighbour is on closedSet
     */
    public static boolean isOnClosedSet(Move neighbour) {
        for (Move move : closedSet) {
            if (lngLatHandler.isCloseTo(move.getCurrLngLat(), neighbour.getCurrLngLat())) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Move> tracePath(Move end) {
        ArrayList<Move> path = new ArrayList<>();
        Move curr = end;
        while (curr.parent() != null) {
            path.add(curr);
            curr = curr.parent();
        }
        path.add(curr);

        //Puts the path in the right order
        Collections.reverse(path);
        return path;
    }

    public static Move hover(String orderNo, LngLat position) {
        Move hover = new Move(orderNo, position);
        hover.setNextLngLat(position);
        hover.setAngle(999);
        return hover;
    }

    /**
     * Checks if the position is in a no fly region
     * @param noFly - The no fly region
     * @param position - The position to be checked
     * @return true if in region
     */
    public static boolean inNoFly(NamedRegion[] noFly, LngLat position) {
        for (NamedRegion region : noFly) {
            if (lngLatHandler.isInRegion(position, region)) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Move>calculateAllPaths(Order[] orders, Restaurant[] restaurants, NamedRegion centralArea, NamedRegion[] noFlyZones) {
        ArrayList<Move> path = new ArrayList<>();

        for (Order order : orders) {
            System.out.println("Onto next order!!!!!");
            //Find restaurant we are delivering to:
            Restaurant restaurant = RestaurantValidator.findRestaurant(order.getPizzasInOrder()[0], restaurants);

            //Go collect pizza
            path.addAll(createFlightPath(order.getOrderNo(), APPLETON_TOWER, restaurant.location(), centralArea, noFlyZones));
            path.add(hover(order.getOrderNo(), restaurant.location()));

            //Return and deliver
            path.addAll(createFlightPath(order.getOrderNo(), restaurant.location(), APPLETON_TOWER, centralArea, noFlyZones));
            path.add(hover(order.getOrderNo(), APPLETON_TOWER));
        }
        return path;
    }

}
