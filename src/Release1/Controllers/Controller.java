/**
 *
 */
package Release1.Controllers;

/**
 *
 */
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author luiiislazaro
 */
public class Controller extends Thread {

    private String message;
    protected static final String HOST = "localhost";

    protected static final Logger logger = Logger.getLogger(Controller.class);

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected Controller() {
        super();
        PropertyConfigurator.configure("log4j.properties");
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

        logger.info("Class Controller --- SEND  to Sensor --- Value: " + message);

        channel.exchangeDeclare(ID_CHANNEL_SEND, "fanout");
        channel.basicPublish(ID_CHANNEL_SEND, "", null, message.getBytes("UTF-8"));

        channel.close();
        connection.close();
    }

    protected void receiveMessage(String ID_CHANNEL_RECEIVE) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_RECEIVE, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_RECEIVE, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                setMessage(new String(body, "UTF-8"));
                logger.info("Class TemperatureController --- RECEIVED from Sensor --- Value: " + new String(body, "UTF-8"));
                checkValues();
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    public void checkValues() {

    }
    
    
    public void receiveChangeMaxMinMessage(String ID_CHANNEL_CHANGE_RECEIVED) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_CHANGE_RECEIVED, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_CHANGE_RECEIVED, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                setMessage(new String(body, "UTF-8"));
                logger.info("Class TemperatureController --- RECEIVED from Sensor --- Value: " + new String(body, "UTF-8") + " --- CHANGE TEMPERATURE");
                checkValuesMaxMin();
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
    
    public void checkValuesMaxMin(){
        
    }
    

}
