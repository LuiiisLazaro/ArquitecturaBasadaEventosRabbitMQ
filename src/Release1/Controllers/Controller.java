package Release1.Controllers;

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
 * @author Faleg, Daniel, Luis
 */
public class Controller extends Thread {

    private String message;
    
    protected static final String HOST = "localhost";
    protected static final Logger logger = Logger.getLogger(Controller.class);//logger para el registro de eventos

    /**
     * constructor 
     * configuraciones para el logger
     */
    protected Controller() {
        super();
        PropertyConfigurator.configure("log4j.properties");
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * method to send the message to the console with rabbitMQ
     *
     * @param ID_CHANNEL_SEND id channel 
     * @param message message to send
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

    /**
     * method to receive messages from sensors
     * @param ID_CHANNEL_RECEIVE id channel to listen
     * @throws IOException
     * @throws TimeoutException 
     */
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

    /**
     * metodo para tomar acciones dependiendo del mensaje recibido desde el sensor
     * este método se va a sobre escribir por cada controlador
     */
    public void checkValues() {

    }
    
    /**
     * métedo para recibir los cambio de valores de maximo y minimo de temperatura y humeada
     * @param ID_CHANNEL_CHANGE_RECEIVED id del canal de conunicación para recibir mensajes
     * @throws IOException
     * @throws TimeoutException 
     */
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
    
    /**
     * metodo para tomar acciones dependiendo del mensaje recibido desde GUI para valores max y min
     * este método se va a sobre escribir por cada controlador
     */
    public void checkValuesMaxMin(){
        
    }
}