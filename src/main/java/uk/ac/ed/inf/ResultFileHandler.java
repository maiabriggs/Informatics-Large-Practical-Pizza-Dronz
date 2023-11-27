package uk.ac.ed.inf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import uk.ac.ed.inf.data.Move;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.serialisers.MoveSerialiser;
import uk.ac.ed.inf.serialisers.OrderSerialiser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ResultFileHandler {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Creates the result directory
     */
    public static void createResultDirectory(){
        File resultDirectory = new File("resultfiles");

        if (!resultDirectory.exists()) {
            if (resultDirectory.mkdir()) {
                System.out.println("Result directory created!");
            } else {
                System.err.println("Failed to create result directory.");
            }
        }
    }


    /**
     * Creates the deliveries json string
     */
    public static void createDeliveriesFile(Order[] orders, String date){
        SimpleModule module =
                new SimpleModule("OrderSerialiser");
        module.addSerializer(Order.class, new OrderSerialiser());
        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());

        String json = null;

        try {
            json = mapper.writeValueAsString(Arrays.stream(orders).toList());
            }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        createFile(json, "resultfiles/deliveries-" + date + ".json");
        System.out.println("Deliveries file created");
    }

    /**
     * Writes the file
     */
    public static void createFile(String json, String filePath){
        try {
            mapper.writeValue(new File(filePath), json);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create all files
     */
    public static void createAllResults(Order[] orders, List<Move> moves, String date){
        createResultDirectory();
        createDeliveriesFile(orders, date);
        createFlightPathFile(moves, date);
    }

    public static void createFlightPathFile(List<Move> moves, String date){
        SimpleModule module =
                new SimpleModule("MoveSerialiser");
        module.addSerializer(Move.class, new MoveSerialiser());
        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());

        String json = null;

        try {
            json = mapper.writeValueAsString(moves);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        createFile(json, "resultfiles/flightpath-" + date + ".json");
        System.out.println("flightpath file created");
    }

    public static void createGeoJSONFile(List<Move> path, String date) {
        ObjectNode properties = mapper.createObjectNode();
        properties.put("name", "Flight Path");
        properties.put("fill", "none");

        ObjectNode geoJSON = mapper.createObjectNode();
        geoJSON.put("type", "FeatureCollection");

        ArrayNode features = geoJSON.putArray("features");

        ObjectNode feature = mapper.createObjectNode();
        feature.put("type", "Feature");

        ObjectNode geometry = mapper.createObjectNode();
        geometry.put("type", "LineString");

        ArrayNode coords = mapper.createArrayNode();

        for (Move move : path) {
            coords.add(move.getCurrLong());
            coords.add(move.getCurrLat());
        }

        ArrayNode pairs = mapper.createArrayNode();
        for (int i = 0; i < coords.size(); i += 2) {
            ArrayNode pair = mapper.createArrayNode();
            pair.add(coords.get(i));
            pair.add(coords.get(i +1));
            pairs.add(pair);
        }

        geometry.set("coordinates", pairs);
        feature.set("geometry", geometry);
        feature.set("properties", properties);
        features.add(feature);

        try {
            mapper.writeValue(new File ("resultfiles/flightpath-" + date + ".geojson"), geoJSON);
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }


}
