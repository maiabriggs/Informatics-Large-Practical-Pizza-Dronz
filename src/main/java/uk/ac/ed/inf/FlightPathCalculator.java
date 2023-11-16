package uk.ac.ed.inf;

import uk.ac.ed.inf.constant.Direction;
import uk.ac.ed.inf.data.Move;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;

import java.util.*;

public class FlightPathCalculator{

    private static PriorityQueue<Move> openSet;
    private static HashSet<Move> closedSet;
    private static List<Move> path;

    private final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);


    public List<Move> calculateFlightPath(Order order, LngLat start, LngLat finish, NamedRegion centralArea, NamedRegion[] noFlyZones) {

        //Add the first move
        openSet.add(new Move(order.getOrderNo(), start.lng(), start.lat()));

        //While there a still cells to visit
        while (!openSet.isEmpty()) {
            //Pops the head of the queue (the one with the smallest cost)
            Move current = openSet.poll();
            LngLat currLngLat = new LngLat(current.getCurrLong(), current.getCurrLat());


            closedSet.add(current);

            if (current.getCurrLong() == finish.lng() && current.getCurrLat() == finish.lat()) {
                path = new ArrayList<>();
                while(current != null) {
                    path.add(current);
                    current = current.parent();
                }
                Collections.reverse(path);
                return path;
            }

            LngLatHandler lngLatHandler = new LngLatHandler();
            for (double angle : Direction.getAllDirections()) {

                LngLat neighLngLat = lngLatHandler.nextPosition(currLngLat, angle);
                Move position = new Move(order.getOrderNo(), neighLngLat.lng(), neighLngLat.lat());

                if (lngLatHandler.isInMultipleRegions(currLngLat, noFlyZones) && !closedSet.contains(position)) {

                    double g = current.getG() + 1;

                    Move existing_neighbour = checkIfInFrontier(position);

                    if(existing_neighbour != null) {
                        if (g < existing_neighbour.getG()) {
                            existing_neighbour.setParent(current);
                            existing_neighbour.setG(g);
                            existing_neighbour.setH(heuristic(new LngLat(existing_neighbour.getCurrLong(), existing_neighbour.getCurrLat()), finish));
                            current.setNextLat(existing_neighbour.getCurrLat());
                            current.setNextLong(existing_neighbour.getNextLong());
                            position.setTotal(position.getG() + position.getH());
                        }
                    }

                    else {
                        Move neighbour = new Move(position.getOrderNo(), position.getCurrLong(), position.getCurrLat());
                        neighbour.setParent(current);
                        neighbour.setG(g);
                        neighbour.setH(heuristic(new LngLat(neighbour.getCurrLong(), neighbour.getCurrLat()), finish));
                        neighbour.setTotal(neighbour.getG() + neighbour.getH());
                        current.setNextLat(neighbour.getCurrLat());
                        current.setNextLong(neighbour.getNextLong());

                    }
                }

            }

            throw new RuntimeException("Path not found");
        }






        return path;
    }


    public static Move checkIfInFrontier(Move neighbour) {
        if(openSet.isEmpty()) {
            return null;
        }
        Iterator<Move> iterator = openSet.iterator();

        Move find = null;
        while (iterator.hasNext()) {
            Move next = iterator.next();
            if (next.getCurrLong() == neighbour.getCurrLong() && next.getCurrLat() == neighbour.getCurrLat()) {
                find = next;
                break;
            }
        }
        return find;
    }

    public static double heuristic (LngLat a, LngLat b) {
        return Math.abs(a.lng() - b.lng()) + Math.abs(a.lat() - b.lat());
    }

}
