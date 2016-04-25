/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Release1.Controllers;

/**
 *
 */
import static Release1.Controllers.Controller.logger;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author luiiislazaro
 */
public class HumidityController extends Controller implements Runnable {

    private static HumidityController INSTANCE = new HumidityController();

    private static final String ID_CHANNEL_HUMIDITY_CONTROLLER = "4";
    private static final String ID_CHANNEL_HUMIDITY_SENSOR = "-4";
    private static final String ID_CHANNEL_CHANGE_HUMIDITY ="CH";
    
    private int maxHumidity;
    private int minHumidity;

    private static final String ID_HUMIDITY_ON = "H1";
    private static final String ID_HUMIDITY_OFF = "H0";

    private static final String ID_DESHUMIDITY_ON = "D1";
    private static final String ID_DESHUMIDITY_OFF = "D0";

    /**
     *
     */
    private HumidityController() {
        super();
        this.minHumidity = 45;
        this.maxHumidity = 55;
    }
    public int getMaxHumidity() {
        return maxHumidity;
    }

    public void setMaxHumidity(int maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public int getMinHumidity() {
        return minHumidity;
    }

    public void setMinHumidity(int minHumidity) {
        this.minHumidity = minHumidity;
    }
    
        private void receiveChangeTemperatureMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_CHANGE_HUMIDITY, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_CHANGE_HUMIDITY, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                String[] values = message.split(":");
                setMaxHumidity(Integer.parseInt(values[0]));
                setMinHumidity(Integer.parseInt(values[1]));
                logger.info("Class HumidityController --- RECEIVED from Sensor --- Value: " + new String(body, "UTF-8") + " --- CHANGE TEMPERATURE");
                logger.info("Class HumidityController --- UPDATE VALUES --- MAX="+getMaxHumidity()+" Min="+getMinHumidity());
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     *
     * @throws IOException
     * @throws TimeoutException
     */
    private void receiveMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_HUMIDITY_SENSOR, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_HUMIDITY_SENSOR, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                setMessage(new String(body, "UTF-8"));
                logger.info("Class HumidityController --- RECEIVED from Sensor --- Value: " + new String(body, "UTF-8"));
                try {
                    checkValuesHumidity();
                } catch (TimeoutException ex) {
                    logger.error(ex);
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     *
     * @throws IOException
     * @throws TimeoutException
     */
    private void checkValuesHumidity() throws IOException, TimeoutException {

        logger.info("Class HumidityController --- SEND to Controller ...");

        if (Math.round(Float.parseFloat(getMessage())) > maxHumidity) {
            sendMessage(ID_CHANNEL_HUMIDITY_CONTROLLER, ID_HUMIDITY_OFF);
            sendMessage(ID_CHANNEL_HUMIDITY_CONTROLLER, ID_DESHUMIDITY_ON);
        } else if (Math.round(Float.parseFloat(getMessage())) < minHumidity){
            sendMessage(ID_CHANNEL_HUMIDITY_CONTROLLER, ID_HUMIDITY_ON);
            sendMessage(ID_CHANNEL_HUMIDITY_CONTROLLER, ID_DESHUMIDITY_OFF);
        }
    }

    /**
     *
     */
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (HumidityController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HumidityController();
                }
            }
        }
    }

    /**
     * This method calls createInstance method to creates and ensure that only
     * one instance of this class is created. Singleton design pattern.
     *
     * @return The instance of this class.
     */
    public static HumidityController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    @Override
    public void run() {
        try {
            receiveMessage();
            receiveChangeTemperatureMessage();
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        HumidityController humidityController = HumidityController.getInstance();
        logger.info("Class HumidityController --- Start Controller Humidity...");
        humidityController.run();
    }
}
