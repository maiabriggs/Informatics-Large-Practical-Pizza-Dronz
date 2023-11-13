package uk.ac.ed.inf;

import uk.ac.ed.inf.data.Move;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.Order;

import java.util.Comparator;
import java.util.PriorityQueue;

public class FlightPathCalculator{
    private final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);


    public Move[] calculateFlightPath(Order order) {
        Move[] moves;
        PriorityQueue<LngLat> openSet = new PriorityQueue<>(Comparator.comparingDouble(this::calculateCost));





        return moves;
    }

    private LngLat[] findNeighbours(LngLat currentPos) {
        for (int i = 0; )
    }

}
