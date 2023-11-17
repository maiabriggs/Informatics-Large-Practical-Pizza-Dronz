package uk.ac.ed.inf;

import uk.ac.ed.inf.constant.Direction;
import uk.ac.ed.inf.data.Move;
import uk.ac.ed.inf.ilp.data.*;

import java.util.*;

public class FlightPathCalculator{

    private static PriorityQueue<Move> openSet;
    private static HashSet<Move> closedSet;
    private static List<Move> path;

    private static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);


    public static boolean calculateFlightPath(Order order, LngLat start, LngLat finish, NamedRegion centralArea, NamedRegion[] noFlyZones) {

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
                return true;
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
            return false;
        }

        return true;
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

    public static List<Move> calculateAllFlightPaths(Order[] orders, NamedRegion centralArea, NamedRegion[] noFlyZones, Restaurant[] restaurants){
        for (Order order : orders) {
            List<Restaurant> restaurantsToGo = getRestaurantsInOrder(order, restaurants);
            List<LngLat> coOrds = null;
            for (Restaurant restaurant : restaurantsToGo) {
                coOrds.add(restaurant.location());
            }
            calculateFlightPath(order, APPLETON_TOWER, restaurantsToGo.get(0).location(), centralArea, noFlyZones);
            for (int i = 1; i < restaurantsToGo.size(); i++) {
                calculateFlightPath(order, APPLETON_TOWER, restaurantsToGo.get(i).location(), centralArea, noFlyZones);
            }
            calculateFlightPath(order, restaurantsToGo.get(-1).location(), APPLETON_TOWER, centralArea, noFlyZones);
        }
        return path;
    }

    public static List<Restaurant> getRestaurantsInOrder(Order order, Restaurant[] restaurants) {
        List<Restaurant> restaurantsInOrder = null;
        Pizza[] pizzas = order.getPizzasInOrder();
        for (Pizza pizza : pizzas) {
            for (Restaurant restaurant : restaurants) {
                for (Pizza item : restaurant.menu()) {
                    if ((item.name() == pizza.name())) {
                        restaurantsInOrder.add(restaurant);
                    }
                }
            }
        }
        return restaurantsInOrder;
    }

}
