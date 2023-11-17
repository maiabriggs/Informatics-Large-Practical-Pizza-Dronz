package uk.ac.ed.inf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
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


}
