import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ed.inf.OrderValidator;
import uk.ac.ed.inf.RestClient;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.*;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class OrderValidatorTest {
    private Order order;
    private OrderValidator orderValidator;
    private Restaurant[] restaurants = {new Restaurant("Civerinos Slice",new LngLat(-3.1912869215011597,55.945535152517735),
            new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
            new Pizza[]{new Pizza("Margarita",1000), new Pizza("Calzone",1400)}),

            new Restaurant("Sora Lella Vegan Restaurant", new LngLat(-3.202541470527649,55.943284737579376),
                    new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY},
                    new Pizza[]{new Pizza("Meat Lover",1400), new Pizza("Vegan Delight",1100)}),

            new Restaurant("Domino's Pizza - Edinburgh - Southside",new LngLat(-3.1838572025299072,55.94449876875712),
                    new DayOfWeek[]{DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                    new Pizza[]{new Pizza("Super Cheese",1400), new Pizza("All Shrooms",900)}),

            new Restaurant("Sodeberg Pavillion", new LngLat(-3.1940174102783203,55.94390696616939),
                    new DayOfWeek[]{DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY},
                    new Pizza[]{new Pizza("Proper Pizza",1400), new Pizza("Pineapple & Ham & Cheese",900)})};

    private final String ORDERS_LIST = "[{\"orderNo\":\"7E814B50\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Super Cheese\",\"priceInPence\":1400},{\"name\":\"All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"699903\",\"creditCardExpiry\":\"09/28\",\"cvv\":\"243\"}},{\"orderNo\":\"10F2D265\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Proper Pizza\",\"priceInPence\":1400},{\"name\":\"Pineapple & Ham & Cheese\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4426099525502381\",\"creditCardExpiry\":\"08/07\",\"cvv\":\"563\"}},{\"orderNo\":\"50D76A69\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"Margarita\",\"priceInPence\":1000},{\"name\":\"Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"5144573725813085\",\"creditCardExpiry\":\"01/28\",\"cvv\":\"58\"}},{\"orderNo\":\"011D3821\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":3431,\"pizzasInOrder\":[{\"name\":\"Meat Lover\",\"priceInPence\":1400},{\"name\":\"Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"4115273793353795\",\"creditCardExpiry\":\"09/25\",\"cvv\":\"435\"}},{\"orderNo\":\"1AC411A5\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Super Cheese\",\"priceInPence\":1400},{\"name\":\"All Shrooms\",\"priceInPence\":900},{\"name\":\"Pizza-Surprise \",\"priceInPence\":-498702880}],\"creditCardInformation\":{\"creditCardNumber\":\"5135201244025921\",\"creditCardExpiry\":\"03/26\",\"cvv\":\"395\"}},{\"orderNo\":\"20EF9422\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":-1994809120,\"pizzasInOrder\":[{\"name\":\"Proper Pizza\",\"priceInPence\":1400},{\"name\":\"Pineapple & Ham & Cheese\",\"priceInPence\":900},{\"name\":\"Pizza-Surprise \",\"priceInPence\":-498702880},{\"name\":\"Pizza Extra2 \",\"priceInPence\":-498702880},{\"name\":\"Pizza Extra3 \",\"priceInPence\":-498702880},{\"name\":\"Pizza Extra4 \",\"priceInPence\":-498702880}],\"creditCardInformation\":{\"creditCardNumber\":\"4090102973544676\",\"creditCardExpiry\":\"02/28\",\"cvv\":\"522\"}},{\"orderNo\":\"721972A2\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":3900,\"pizzasInOrder\":[{\"name\":\"Margarita\",\"priceInPence\":1000},{\"name\":\"Calzone\",\"priceInPence\":1400},{\"name\":\"Meat Lover\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4041834542174557\",\"creditCardExpiry\":\"11/24\",\"cvv\":\"712\"}},{\"orderNo\":\"3DF5A55B\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":1500,\"pizzasInOrder\":[{\"name\":\"Meat Lover\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4663147775819971\",\"creditCardExpiry\":\"01/28\",\"cvv\":\"676\"}},{\"orderNo\":\"603344AC\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"Margarita\",\"priceInPence\":1000},{\"name\":\"Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4809913624320825\",\"creditCardExpiry\":\"11/26\",\"cvv\":\"753\"}},{\"orderNo\":\"58143EA6\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"Meat Lover\",\"priceInPence\":1400},{\"name\":\"Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"4925675594090220\",\"creditCardExpiry\":\"07/24\",\"cvv\":\"145\"}},{\"orderNo\":\"33728C92\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Super Cheese\",\"priceInPence\":1400},{\"name\":\"All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"2720447625761059\",\"creditCardExpiry\":\"06/25\",\"cvv\":\"444\"}},{\"orderNo\":\"6BB84C12\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Proper Pizza\",\"priceInPence\":1400},{\"name\":\"Pineapple & Ham & Cheese\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4862525932481539\",\"creditCardExpiry\":\"01/27\",\"cvv\":\"861\"}},{\"orderNo\":\"72AB194E\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"Margarita\",\"priceInPence\":1000},{\"name\":\"Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"5559470233804334\",\"creditCardExpiry\":\"11/25\",\"cvv\":\"356\"}},{\"orderNo\":\"7E659886\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"Meat Lover\",\"priceInPence\":1400},{\"name\":\"Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"4653508961968462\",\"creditCardExpiry\":\"07/28\",\"cvv\":\"419\"}},{\"orderNo\":\"0ADADC0F\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Super Cheese\",\"priceInPence\":1400},{\"name\":\"All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"2221726073924630\",\"creditCardExpiry\":\"04/27\",\"cvv\":\"081\"}},{\"orderNo\":\"3B49498B\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Proper Pizza\",\"priceInPence\":1400},{\"name\":\"Pineapple & Ham & Cheese\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4022101749543007\",\"creditCardExpiry\":\"10/24\",\"cvv\":\"475\"}},{\"orderNo\":\"7D5B9671\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"Margarita\",\"priceInPence\":1000},{\"name\":\"Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"5567355224140055\",\"creditCardExpiry\":\"01/28\",\"cvv\":\"884\"}},{\"orderNo\":\"5E6437DA\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"Meat Lover\",\"priceInPence\":1400},{\"name\":\"Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"4091171168452093\",\"creditCardExpiry\":\"04/27\",\"cvv\":\"427\"}},{\"orderNo\":\"083ADEB0\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Super Cheese\",\"priceInPence\":1400},{\"name\":\"All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5129080598953856\",\"creditCardExpiry\":\"04/25\",\"cvv\":\"506\"}},{\"orderNo\":\"5A82F0CD\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Proper Pizza\",\"priceInPence\":1400},{\"name\":\"Pineapple & Ham & Cheese\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5347052602635916\",\"creditCardExpiry\":\"07/24\",\"cvv\":\"723\"}},{\"orderNo\":\"3B3373FA\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"Margarita\",\"priceInPence\":1000},{\"name\":\"Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4570288439218614\",\"creditCardExpiry\":\"08/28\",\"cvv\":\"389\"}},{\"orderNo\":\"3D084FC6\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"Meat Lover\",\"priceInPence\":1400},{\"name\":\"Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"4885673509620940\",\"creditCardExpiry\":\"08/27\",\"cvv\":\"911\"}},{\"orderNo\":\"22FF7FEE\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Super Cheese\",\"priceInPence\":1400},{\"name\":\"All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4920220427261584\",\"creditCardExpiry\":\"10/27\",\"cvv\":\"701\"}},{\"orderNo\":\"4B7BFACA\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Proper Pizza\",\"priceInPence\":1400},{\"name\":\"Pineapple & Ham & Cheese\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4972323957862867\",\"creditCardExpiry\":\"08/25\",\"cvv\":\"307\"}},{\"orderNo\":\"08828002\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"Margarita\",\"priceInPence\":1000},{\"name\":\"Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4094810349700600\",\"creditCardExpiry\":\"07/28\",\"cvv\":\"559\"}},{\"orderNo\":\"2238789D\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"Meat Lover\",\"priceInPence\":1400},{\"name\":\"Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"4545901479411293\",\"creditCardExpiry\":\"08/26\",\"cvv\":\"730\"}},{\"orderNo\":\"47503445\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Super Cheese\",\"priceInPence\":1400},{\"name\":\"All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5175240888653813\",\"creditCardExpiry\":\"03/28\",\"cvv\":\"747\"}},{\"orderNo\":\"6205C656\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Proper Pizza\",\"priceInPence\":1400},{\"name\":\"Pineapple & Ham & Cheese\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"2720976032406981\",\"creditCardExpiry\":\"09/24\",\"cvv\":\"487\"}},{\"orderNo\":\"5ED21275\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"Margarita\",\"priceInPence\":1000},{\"name\":\"Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4566154212160140\",\"creditCardExpiry\":\"01/26\",\"cvv\":\"100\"}},{\"orderNo\":\"324ACA43\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"Meat Lover\",\"priceInPence\":1400},{\"name\":\"Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"2221893683389362\",\"creditCardExpiry\":\"11/24\",\"cvv\":\"087\"}},{\"orderNo\":\"4CFACE94\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Super Cheese\",\"priceInPence\":1400},{\"name\":\"All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4717244428810063\",\"creditCardExpiry\":\"02/28\",\"cvv\":\"782\"}},{\"orderNo\":\"25A259FC\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Proper Pizza\",\"priceInPence\":1400},{\"name\":\"Pineapple & Ham & Cheese\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5575464167313347\",\"creditCardExpiry\":\"06/27\",\"cvv\":\"808\"}},{\"orderNo\":\"16EDBEBC\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"Margarita\",\"priceInPence\":1000},{\"name\":\"Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4249815251957241\",\"creditCardExpiry\":\"10/24\",\"cvv\":\"524\"}},{\"orderNo\":\"6D8377D8\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"Meat Lover\",\"priceInPence\":1400},{\"name\":\"Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"4267916283975438\",\"creditCardExpiry\":\"11/28\",\"cvv\":\"576\"}},{\"orderNo\":\"4280AF77\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Super Cheese\",\"priceInPence\":1400},{\"name\":\"All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5583082499268375\",\"creditCardExpiry\":\"06/28\",\"cvv\":\"974\"}},{\"orderNo\":\"6B135CFC\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Proper Pizza\",\"priceInPence\":1400},{\"name\":\"Pineapple & Ham & Cheese\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4559741735095321\",\"creditCardExpiry\":\"06/28\",\"cvv\":\"279\"}},{\"orderNo\":\"767E7BB7\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"Margarita\",\"priceInPence\":1000},{\"name\":\"Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"5139262851631837\",\"creditCardExpiry\":\"07/27\",\"cvv\":\"040\"}},{\"orderNo\":\"26286120\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"Meat Lover\",\"priceInPence\":1400},{\"name\":\"Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"2221946522990830\",\"creditCardExpiry\":\"05/26\",\"cvv\":\"533\"}},{\"orderNo\":\"2C2A7B7D\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Super Cheese\",\"priceInPence\":1400},{\"name\":\"All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4810174693611704\",\"creditCardExpiry\":\"11/26\",\"cvv\":\"205\"}},{\"orderNo\":\"3E268761\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Proper Pizza\",\"priceInPence\":1400},{\"name\":\"Pineapple & Ham & Cheese\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4615094776142392\",\"creditCardExpiry\":\"06/25\",\"cvv\":\"768\"}},{\"orderNo\":\"156B7837\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"Margarita\",\"priceInPence\":1000},{\"name\":\"Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4852892142904508\",\"creditCardExpiry\":\"01/27\",\"cvv\":\"515\"}},{\"orderNo\":\"354D4E47\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"Meat Lover\",\"priceInPence\":1400},{\"name\":\"Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"4681861768011267\",\"creditCardExpiry\":\"01/24\",\"cvv\":\"888\"}},{\"orderNo\":\"40858A78\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Super Cheese\",\"priceInPence\":1400},{\"name\":\"All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4100295865257403\",\"creditCardExpiry\":\"06/24\",\"cvv\":\"575\"}},{\"orderNo\":\"0D116C36\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Proper Pizza\",\"priceInPence\":1400},{\"name\":\"Pineapple & Ham & Cheese\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4143406457759792\",\"creditCardExpiry\":\"03/27\",\"cvv\":\"492\"}},{\"orderNo\":\"5DF89740\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"Margarita\",\"priceInPence\":1000},{\"name\":\"Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4196919272344125\",\"creditCardExpiry\":\"06/27\",\"cvv\":\"967\"}},{\"orderNo\":\"34E660F3\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"Meat Lover\",\"priceInPence\":1400},{\"name\":\"Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"5112179846503858\",\"creditCardExpiry\":\"06/26\",\"cvv\":\"378\"}},{\"orderNo\":\"64F64270\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Super Cheese\",\"priceInPence\":1400},{\"name\":\"All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5550171119121244\",\"creditCardExpiry\":\"01/28\",\"cvv\":\"827\"}},{\"orderNo\":\"3573D953\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Proper Pizza\",\"priceInPence\":1400},{\"name\":\"Pineapple & Ham & Cheese\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5113807085808690\",\"creditCardExpiry\":\"05/26\",\"cvv\":\"844\"}},{\"orderNo\":\"5FFF420A\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"Margarita\",\"priceInPence\":1000},{\"name\":\"Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"5401647656527985\",\"creditCardExpiry\":\"07/27\",\"cvv\":\"198\"}},{\"orderNo\":\"5E3EDDE3\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"Meat Lover\",\"priceInPence\":1400},{\"name\":\"Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"4396539406406778\",\"creditCardExpiry\":\"04/24\",\"cvv\":\"394\"}},{\"orderNo\":\"16B81847\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Super Cheese\",\"priceInPence\":1400},{\"name\":\"All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5333885262372973\",\"creditCardExpiry\":\"10/25\",\"cvv\":\"991\"}},{\"orderNo\":\"16F67F33\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Proper Pizza\",\"priceInPence\":1400},{\"name\":\"Pineapple & Ham & Cheese\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4574428871142189\",\"creditCardExpiry\":\"02/26\",\"cvv\":\"972\"}},{\"orderNo\":\"52CDF09A\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"Margarita\",\"priceInPence\":1000},{\"name\":\"Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"4853647729725562\",\"creditCardExpiry\":\"01/28\",\"cvv\":\"668\"}},{\"orderNo\":\"2D39AFE4\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"Meat Lover\",\"priceInPence\":1400},{\"name\":\"Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"2221451016149480\",\"creditCardExpiry\":\"05/25\",\"cvv\":\"143\"}},{\"orderNo\":\"29C0C31B\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Super Cheese\",\"priceInPence\":1400},{\"name\":\"All Shrooms\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"4136720573499912\",\"creditCardExpiry\":\"11/27\",\"cvv\":\"145\"}},{\"orderNo\":\"040269D2\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2400,\"pizzasInOrder\":[{\"name\":\"Proper Pizza\",\"priceInPence\":1400},{\"name\":\"Pineapple & Ham & Cheese\",\"priceInPence\":900}],\"creditCardInformation\":{\"creditCardNumber\":\"5170318573503082\",\"creditCardExpiry\":\"01/27\",\"cvv\":\"024\"}},{\"orderNo\":\"4D4A1256\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2500,\"pizzasInOrder\":[{\"name\":\"Margarita\",\"priceInPence\":1000},{\"name\":\"Calzone\",\"priceInPence\":1400}],\"creditCardInformation\":{\"creditCardNumber\":\"5136844206331495\",\"creditCardExpiry\":\"06/26\",\"cvv\":\"522\"}},{\"orderNo\":\"5D236203\",\"orderDate\":\"2023-11-12\",\"orderStatus\":\"UNDEFINED\",\"orderValidationCode\":\"UNDEFINED\",\"priceTotalInPence\":2600,\"pizzasInOrder\":[{\"name\":\"Meat Lover\",\"priceInPence\":1400},{\"name\":\"Vegan Delight\",\"priceInPence\":1100}],\"creditCardInformation\":{\"creditCardNumber\":\"4153838693274276\",\"creditCardExpiry\":\"07/24\",\"cvv\":\"373\"}}]";
    Order[] actualOrders;

    @Before
    public void setUp() {
        orderValidator = new OrderValidator();
        RestClient restClient = new RestClient();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            actualOrders = mapper.readValue(ORDERS_LIST, Order[].class);
            System.out.println("read all actual orders");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    public void testUndefined() {
        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)}, new CreditCardInformation("13499472696504",
                "06/28", "952"));

        assertEquals(OrderValidationCode.UNDEFINED, order.getOrderValidationCode());
    }

    @Test
    public void testNoError() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.NO_ERROR, order.getOrderValidationCode());
    }

    @Test
    public void cardTooShortInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cardTooLongInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("13499472696505543534534435","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cardNotNumInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1111mmmmmm222222","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.CARD_NUMBER_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void expiryDateInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/21", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void expiryDateMonthInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","09/23", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.EXPIRY_DATE_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cvvInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/21", "9524"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.CVV_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cvvTooShortInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/21", "95"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.CVV_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void cvvNotNumInvalid() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/21", "95e"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.CVV_INVALID, order.getOrderValidationCode());
    }

    @Test
    public void totalIncorrect() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 0, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.TOTAL_INCORRECT, order.getOrderValidationCode());
    }

    @Test
    public void testPizzaUndefined() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Pizza Chives", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.PIZZA_NOT_DEFINED, order.getOrderValidationCode());
    }

    @Test
    public void tooManyPizza() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 5600, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900), new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED, order.getOrderValidationCode());
    }

    @Test
    public void testPizzaMultipleRestaurants() {

        order = new Order("19514FE0", LocalDate.of(2023, 9, 01), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("Pineapple & Ham & Cheese", 900)},
                new CreditCardInformation("1349947269650466","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS, order.getOrderValidationCode());
    }

    @Test
    public void restaurantClosed() {

        order = new Order("19514FE0", LocalDate.of(2023, 10, 16), OrderStatus.UNDEFINED,
                OrderValidationCode.UNDEFINED, 2400, new Pizza[]{new Pizza("Super Cheese", 1400), new Pizza("All Shrooms", 900)},
                new CreditCardInformation("1349947269650466","06/28", "952"));

        orderValidator.validateOrder(order, restaurants);

        assertEquals(OrderValidationCode.RESTAURANT_CLOSED, order.getOrderValidationCode());
    }

    @Test
    public void testGetValidatedOrders() {
        Order[] validatedOrders = orderValidator.getValidatedOrders(actualOrders, restaurants);

        for (int i = 0; i < validatedOrders.length; i++) {
            assertEquals(OrderValidationCode.NO_ERROR, validatedOrders[i].getOrderValidationCode());
            assertEquals(OrderStatus.VALID_BUT_NOT_DELIVERED, validatedOrders[i].getOrderStatus());
        }
    }


}
