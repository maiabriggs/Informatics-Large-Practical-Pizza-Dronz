package uk.ac.ed.inf.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import uk.ac.ed.inf.data.Move;
import uk.ac.ed.inf.ilp.data.Order;

import java.io.IOException;

public class MoveSerialiser extends StdSerializer<Move> {


    public MoveSerialiser() {
        this(null);
    }

    public MoveSerialiser(Class<Move> t) {
        super(t);
    }

    @Override
    public void serialize(Move move, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        //orderNo
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("orderNo", move.getOrderNo());
        jsonGenerator.writeEndObject();

        //fromLongitude
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("fromLongitude", String.valueOf(move.getCurrLong()));
        jsonGenerator.writeEndObject();

        //fromLatitude
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("fromLatitude", String.valueOf(move.getCurrLat()));
        jsonGenerator.writeEndObject();

        //angle
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("angle", String.valueOf(move.getAngle()));
        jsonGenerator.writeEndObject();

        //toLongitude
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("toLongitude", String.valueOf(move.getNextLong()));
        jsonGenerator.writeEndObject();

        //toLatitude
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("toLatitude", String.valueOf(move.getNextLat()));
        jsonGenerator.writeEndObject();


    }
}
