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
        int count = 0;

        while (!openSet.isEmpty()){
            Move currMove = openSet.poll();
            LngLat curr = currMove.getCurrLngLat();
            System.out.println("This is out current lowest cost move");
            System.out.println(curr.lng());
            System.out.println(curr.lat());
            System.out.println("Total: " + currMove.getTotal());

            count += 1;
            for (Double direction : Direction.getAllDirections()) {
                System.out.println("Direction: " + direction);
                LngLat neighbour = lngLatHandler.nextPosition(curr, direction);
                System.out.println("Neighbour position lng: " + neighbour.lng() + " lat: " + neighbour.lat());
                Move neighbourMove = new Move(orderNo, neighbour);
                //Check if neighbour is the destination
                if (lngLatHandler.isCloseTo(end, neighbour)) {
                    System.out.println("We've reached the end");
                    //Do all the things that should be done if we've found the path
                    neighbourMove.setParent(currMove);
                    return tracePath(neighbourMove);
                }

                //Check that there is not a better path option
                if ((!isOnClosedSet(neighbourMove) && !inNoFly(noFly, neighbour) && !lngLatHandler.isInRegion(neighbour, centralArea)) || count == 5) {
                    System.out.println("Not on closed set or FORBIDDEN");
                    if (isLowerOnOpenSet(neighbourMove)) {
                        System.out.println("There's a better move");
                        neighbourMove.setParent(betterMove);
                        System.out.println("Better Move lng: " + betterMove.getCurrLngLat().lng() + " lat: " + betterMove.getCurrLngLat().lat());
                        neighbourMove.setG(betterMove.getG() + 1);
                        neighbourMove.setH(lngLatHandler.distanceTo(end, neighbour));
                        neighbourMove.setTotal(neighbourMove.getG() + neighbourMove.getH());
                    }
                    else {
                        System.out.println("Theres no better move, we are adding this neighbour to the open set");
                        neighbourMove.setParent(currMove);
                        neighbourMove.setG(currMove.getG() + 1);
                        neighbourMove.setH(lngLatHandler.distanceTo(end, neighbour));
                        neighbourMove.setTotal(neighbourMove.getG() + neighbourMove.getH());
                        openSet.add(neighbourMove);
                    }
                }
            }
            closedSet.add(currMove);
            System.out.println("Current gets marked as visited and we repeat !!!!!!!!!!!");
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
        System.out.println(curr.getCurrLngLat().lng());
        System.out.println(curr.getCurrLngLat().lat());
        while (curr.parent() != null) {
            path.add(curr);
            curr = curr.parent();
            System.out.println("Prev node");
            System.out.println(curr.getCurrLngLat().lng());
            System.out.println(curr.getCurrLngLat().lat());
        }
        path.add(curr);

        //Puts the path in the right order
        System.out.println("Before reverse");
        System.out.println(path.get(0).getCurrLngLat().lng());
        System.out.println(path.get(0).getCurrLngLat().lat());
        Collections.reverse(path);
        System.out.println("After reverse");
        System.out.println(path.get(0).getCurrLngLat().lng());
        System.out.println(path.get(0).getCurrLngLat().lat());
        return path;
    }

    public static Move hover(String orderNo, LngLat position) {
        Move hover = new Move(orderNo, position);
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

}
