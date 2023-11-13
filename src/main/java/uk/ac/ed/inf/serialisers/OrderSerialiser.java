package uk.ac.ed.inf.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import uk.ac.ed.inf.ilp.data.Order;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;


public class OrderSerialiser extends StdSerializer<Order> {
    public OrderSerialiser() {
        this(null);
    }

    public OrderSerialiser(Class<Order> t) {
        super(t);
    }


    @Override
    public void serialize(Order order, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        //orderNo
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("orderNo", order.getOrderNo());
        jsonGenerator.writeEndObject();

        //orderStatus
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("orderStatus", String.valueOf(order.getOrderStatus()));
        jsonGenerator.writeEndObject();

        //orderValidationCode
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("orderValidationCode", String.valueOf(order.getOrderValidationCode()));
        jsonGenerator.writeEndObject();

        //costInPence
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("costInPence", String.valueOf(order.getPriceTotalInPence()));
        jsonGenerator.writeEndObject();
    }
}
