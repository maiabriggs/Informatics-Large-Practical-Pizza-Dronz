package uk.ac.ed.inf;

import uk.ac.ed.inf.constant.Direction;
import uk.ac.ed.inf.data.Move;
import uk.ac.ed.inf.ilp.data.*;
import uk.ac.ed.inf.validation.RestaurantValidator;

import java.sql.Array;
import java.util.*;

public class FlightPathCalculator{

    private static PriorityQueue<Move> openSet;
    private static HashSet<Move> closedSet;
    private static List<Move> path;

    private static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);

    //TODO Drone needs to hover
    public static boolean calculateFlightPath(Order order, LngLat start, LngLat finish, NamedRegion centralArea, NamedRegion[] noFlyZones) {
        LngLatHandler lngLatHandler = new LngLatHandler();
        openSet = new PriorityQueue<>(Comparator.comparingDouble(c -> c.getTotal()));
        closedSet = new HashSet<>();
        //Add the first move
        openSet.add(new Move(order.getOrderNo(), start.lng(), start.lat()));
        System.out.println("First move has been added");
        int count = 0;
        //While there a still cells to visit
        if (!openSet.isEmpty()){
            System.out.println("The open set is not empty");
        }

        while (!openSet.isEmpty()) {
            //Pops the head of the queue (the one with the smallest cost)
            Move current = openSet.poll();
            LngLat currLngLat = new LngLat(current.getCurrLong(), current.getCurrLat());
            System.out.println("Current Lng = " + currLngLat.lng());
            System.out.println("Current Lat = " + currLngLat.lat());
            System.out.println("Finish Lng = " + finish.lng());
            System.out.println("Finish Lat = " + finish.lat());
            closedSet.add(current);


            if ((lngLatHandler.isCloseTo(currLngLat, finish) || count == 10000)) {
                System.out.println("We are at the finish");
                path = new ArrayList<>();
                while(current != null) {
                    System.out.println("Adding to path");
                    path.add(current);
                    current = current.parent();
                }
                Collections.reverse(path);
                return true;
            }
            //needs an update

            for (double angle : Direction.getAllDirections()) {
                System.out.println(angle);

                LngLat neighLngLat = lngLatHandler.nextPosition(currLngLat, angle);
                Move position = new Move(order.getOrderNo(), neighLngLat.lng(), neighLngLat.lat());

                if (!lngLatHandler.isInMultipleRegions(neighLngLat, noFlyZones) && !closedSet.contains(position)) {

                    double g = current.getG() + 1;

                    Move existing_neighbour = checkIfInFrontier(position);

                    if(existing_neighbour != null) {
                        System.out.println("Theres a neighbour we've already visited");
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
                        System.out.println("There's no existing neighbour");
                        Move neighbour = new Move(position.getOrderNo(), position.getCurrLong(), position.getCurrLat());
                        neighbour.setParent(current);
                        neighbour.setG(g);
                        neighbour.setH(heuristic(new LngLat(neighbour.getCurrLong(), neighbour.getCurrLat()), finish));
                        neighbour.setTotal(neighbour.getG() + neighbour.getH());
                        current.setNextLat(neighbour.getCurrLat());
                        current.setNextLong(neighbour.getCurrLong());
                        openSet.add(neighbour);

                    }
                }

            }
            count += 1;
        }

        //No path found
        return false;
    }


    public static Move checkIfInFrontier(Move neighbour) {
        LngLatHandler lngLatHandler = new LngLatHandler();
        if (openSet.isEmpty()) {
            return null;
        }
        Iterator<Move> iterator = openSet.iterator();

        Move find = null;
        while (iterator.hasNext()) {
            Move next = iterator.next();
            if (lngLatHandler.isCloseTo(new LngLat(neighbour.getCurrLong(), neighbour.getCurrLat()), new LngLat(next.getCurrLong(), next.getCurrLat()))) {
                find = next;
                break;
            }
        }
        return find;
    }

    public static double heuristic (LngLat a, LngLat b) {
        LngLatHandler lngLatHandler = new LngLatHandler();
        return lngLatHandler.distanceTo(a, b);
    }

    public static List<Move> calculateAllFlightPaths(Order[] orders, NamedRegion centralArea, NamedRegion[] noFlyZones, Restaurant[] restaurants){
        RestaurantValidator restaurantValidator = new RestaurantValidator();
        for (Order order : orders) {
            Restaurant restaurant = restaurantValidator.findRestaurant(order.getPizzasInOrder()[0], restaurants);
            calculateFlightPath(order, APPLETON_TOWER, restaurant.location(), centralArea, noFlyZones);
            calculateFlightPath(order, restaurant.location(), APPLETON_TOWER, centralArea, noFlyZones);
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
