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

                LngLat neighbour = lngLatHandler.nextPosition(new LngLat(current.getCurrLong(), current.getCurrLat()), angle);

            }


        }






        return path;
    }

}
