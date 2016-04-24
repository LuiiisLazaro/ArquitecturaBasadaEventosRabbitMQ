package Release1.Sensors;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * class sensor to simulate the real sensor in this program
 *
 * @author luiiislazaro
 */
public class Sensor {

    protected int delay = 4000;         //the time to update data and send to console
    protected boolean isDone = false;   //to control de life of thred
    protected float driftValue;				// The amount of temperature gained or lost
    private String message;           //message with value to send

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    protected static final String HOST  = "localhost";

    protected Sensor() {
    }

    protected boolean coinToss() {
        Random r = new Random();
        return (r.nextBoolean());
    }

    /**
     * to create numbers and simulate the temperature
     *
     * @return
     */
    protected float getRandomNumber() {
        Random r = new Random();
        Float val;
        val = (float) -1.0;
        while (val < 0.1) {
            val = r.nextFloat();
        }
        return (val);
    }

    /**
     * to send the message to the console with rabbitMQ
     *
     * @param ID_CHANNEL_SEND
     * @param message
     * @throws IOException
     * @throws TimeoutException
     */
    protected void sendMessage(String ID_CHANNEL_SEND, String message) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        System.out.println("Value send: " + message);

        channel.exchangeDeclare(ID_CHANNEL_SEND, "fanout");
        channel.basicPublish(ID_CHANNEL_SEND, "", null, message.getBytes("UTF-8"));

        channel.close();
        connection.close();
    }

    /**
     *
     * @param ID_CHANNEL_CONTROLLER
     * @return
     * @throws java.io.IOException
     * @throws java.util.concurrent.TimeoutException
     */
    /**
     * protected String receiveMessage(String ID_CHANNEL_CONTROLLER) throws
     * IOException, TimeoutException { ConnectionFactory factory = new
     * ConnectionFactory(); factory.setHost("localhost"); Connection connection
     * = factory.newConnection(); Channel channel = connection.createChannel();
     * message = "";
     *
     * channel.exchangeDeclare(ID_CHANNEL_CONTROLLER, "fanout"); String
     * queueName = channel.queueDeclare().getQueue();
     * channel.queueBind(queueName, ID_CHANNEL_CONTROLLER, "");
     *
     * Consumer consumer = new DefaultConsumer(channel) {
     *
     * @Override public void handleDelivery(String consumerTag, Envelope
     * envelope, AMQP.BasicProperties properties, byte[] body) throws
     * IOException { message = new String(body, "UTF-8");
     * System.out.println("[x]VALUE receive: " + message); } };
     * channel.basicConsume(queueName, true, consumer); return message;
    }*
     */
}
