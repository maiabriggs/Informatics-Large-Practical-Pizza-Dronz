package uk.ac.ed.inf;

import uk.ac.ed.inf.constant.Direction;
import uk.ac.ed.inf.data.Move;
import uk.ac.ed.inf.ilp.data.*;
import uk.ac.ed.inf.validation.RestaurantValidator;
import java.util.*;

public class FlightPathCalculator {
    private static PriorityQueue<Move> openSet;
    private static ArrayList<Move> closedSet;
    private static Move betterMove;
    private static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);
    private static LngLatHandler lngLatHandler;

    /**
     * Calculates the flight path from a start position to the end position.
     * @param orderNo The order number of the order we are considering
     * @param start The start of the flight path
     * @param end The end of the flight path
     * @param noFly The no-fly zones
     * @return The calculated flight path
     */
    public static ArrayList<Move> calculateFlightPath(String orderNo, LngLat start, LngLat end, NamedRegion[] noFly, NamedRegion centralArea){
        lngLatHandler = new LngLatHandler();
        //We initialise the starting queue
        openSet = new PriorityQueue<>();
        closedSet = new ArrayList<>();

        //Add our starting move to the open list
        Move startMove = new Move(orderNo, start);
        openSet.add(startMove);
        System.out.println("...finding a path...");
        //While there are still nodes to visit.
        while (!openSet.isEmpty()){
            Move currMove = openSet.poll();
            LngLat curr = currMove.getCurrLngLat();

            for (Double direction : Direction.getAllDirections()) {
                boolean valid = true;
                LngLat neighbour = lngLatHandler.nextPosition(curr, direction);
                Move neighbourMove = new Move(orderNo, neighbour);
                neighbourMove.setAngle(direction);

                //Check if neighbour is the destination
                if (lngLatHandler.isCloseTo(end, neighbour)) {
                    neighbourMove.setParent(currMove);
                    return tracePath(neighbourMove);
                }

                //Check if left central area
                if (!lngLatHandler.isInCentralArea(curr, centralArea) && lngLatHandler.isInCentralArea(neighbour, centralArea)) {
                    valid = false;
                }

                if (!isOnClosedSet(neighbourMove) && !inNoFly(noFly, neighbour) && valid) {
                    neighbourMove.setG(currMove.getG() + 1);
                    neighbourMove.setH(lngLatHandler.distanceTo(end, neighbour));
                    //Calculate total
                    neighbourMove.setTotal(neighbourMove.getG() + neighbourMove.getH());

                    //If there is a better way of getting to the neighbour
                    if (isLowerOnOpenSet(neighbourMove)) {
                        neighbourMove.setParent(betterMove);
                        neighbourMove.setG(betterMove.getG() + 1);
                        neighbourMove.setTotal(neighbourMove.getG() + neighbourMove.getH());
                    }

                    else {
                        neighbourMove.setParent(currMove);
                        openSet.add(neighbourMove);
                    }
                }
            }
            //Mark the current node as visited
            closedSet.add(currMove);
        }
        //Throws an error if no path can be found
        throw new RuntimeException("Cannot find a path");
    }

    /**
     * Checks if a node with the same position as the neighbour in the openSet list,
     * has a lower total than the neighbour.
     * @param neighbour the node we are checking
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
     * @param neighbour the node we are checking
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

    /**
     * Traces the path back from the end to the beginning and reverses the path, so it is in the correct order
     * @param end The end node we are traversing back from
     * @return The path traversed
     */
    public static ArrayList<Move> tracePath(Move end) {
        ArrayList<Move> path = new ArrayList<>();
        Move curr = end;
        while (curr.parent() != null) {
            curr.parent().setNextLngLat(curr.getCurrLngLat());
            curr = curr.parent();
            path.add(curr);
        }
        path.add(curr);

        //Puts the path in the right order
        Collections.reverse(path);
        return path;
    }

    /**
     * Creates a hover move for a given position
     * @param orderNo The order number of the order we are considering
     * @param position The position we want to hover at
     * @return the hover move
     */
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

    /**
     * Calculates all the flight paths in a list of orders
     * @param orders The list of orders
     * @param restaurants The list of restaurants
     * @param noFlyZones The list of no-fly zones
     * @return The list of all flight paths
     */
    public static ArrayList<Move>calculateAllPaths(Order[] orders, Restaurant[] restaurants, NamedRegion[] noFlyZones, NamedRegion centralArea) {
        ArrayList<Move> path = new ArrayList<>();

        //Stores the paths to restaurants we have found
        HashMap<String, ArrayList<Move>> pathsToRestaurants = new HashMap<>();
        ArrayList<Move> pathToRestaurant;

        for (Order order : orders) {
            System.out.println("Finding flightpath for Order No: " + order.getOrderNo());
            //Find restaurant we are delivering to:
            Restaurant restaurant = RestaurantValidator.findRestaurant(order.getPizzasInOrder()[0], restaurants);
            if (pathsToRestaurants.containsKey(restaurant.name())) {
                pathToRestaurant = pathsToRestaurants.get(restaurant.name());
            }
            else {
                pathToRestaurant = calculateFlightPath(order.getOrderNo(), APPLETON_TOWER, restaurant.location(), noFlyZones, centralArea);

                //Adds it to the hashmap of stored routes
                pathsToRestaurants.put(restaurant.name(), pathToRestaurant);
            }
            path.addAll(pathToRestaurant);
            path.add(hover(order.getOrderNo(), restaurant.location()));

            Collections.reverse(pathToRestaurant);
            //Return and deliver
            path.addAll(pathToRestaurant);
            path.add(hover(order.getOrderNo(), APPLETON_TOWER));
            OrderValidator.orderDelivered(order);
        }
        return path;
    }
}
