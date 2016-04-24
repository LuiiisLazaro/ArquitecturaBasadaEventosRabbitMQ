package Release1.Controllers;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.awt.SystemColor;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Controller {

    protected int delay = 2000;				// The loop delay (2 seconds)
    protected boolean isDone = false;			// Loop termination flag
    private String message;
    protected static final String HOST="localhost";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected Controller() {
        super();
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

        System.out.println("Send Message: "+message);

        channel.exchangeDeclare(ID_CHANNEL_SEND, "fanout");
        channel.basicPublish(ID_CHANNEL_SEND, "", null, message.getBytes("UTF-8"));

        channel.close();
        connection.close();
    }
}
