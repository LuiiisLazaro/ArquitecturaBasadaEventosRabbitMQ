/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author luiiislazaro
 */
public class TemperatureController extends Controller implements Runnable {

    private static TemperatureController INSTANCE = new TemperatureController();

    private static final String ID_CHANNEL_TEMPERATURE_CONTROLLER = "5";
    private static final String ID_CHANNEL_TEMPERATURE_SENSOR = "-5";
    private static final String ID_CHANNEL_CHANGE_TEMPERATURE = "CT";

    private int maxTemperature;
    private int minTemperature;

    private static final String ID_HEATER_ON = "H1";
    private static final String ID_HEATER_OFF = "H0";

    private static final String ID_CHILLER_ON = "C1";
    private static final String ID_CHILLER_OFF = "C0";

    /**
     *
     */
    private TemperatureController() {
        super();
        this.maxTemperature = 75;
        this.minTemperature = 70;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }

    private void receiveChangeTemperatureMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_CHANGE_TEMPERATURE, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_CHANGE_TEMPERATURE, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                String[] values = message.split(":");
                setMaxTemperature(Integer.parseInt(values[0]));
                setMinTemperature(Integer.parseInt(values[1]));
                logger.info("Class TemperatureController --- RECEIVED from Sensor --- Value: " + new String(body, "UTF-8") + " --- CHANGE TEMPERATURE");
                logger.info("Class TemperatureController --- UPDATE VALUES --- MAX="+getMaxTemperature()+" Min="+getMinTemperature());
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

        channel.exchangeDeclare(ID_CHANNEL_TEMPERATURE_SENSOR, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_TEMPERATURE_SENSOR, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                setMessage(new String(body, "UTF-8"));
                logger.info("Class TemperatureController --- RECEIVED from Sensor --- Value: " + new String(body, "UTF-8"));
                try {
                    checkValuesTemperature();
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
    private void checkValuesTemperature() throws IOException, TimeoutException {
        logger.info("Class TemperatureController --- SEND to Controller ...");
        if (Math.round(Float.parseFloat(getMessage())) > maxTemperature) {
            sendMessage(ID_CHANNEL_TEMPERATURE_CONTROLLER, ID_CHILLER_ON);
            sendMessage(ID_CHANNEL_TEMPERATURE_CONTROLLER, ID_HEATER_OFF);
        } else if (Math.round(Float.parseFloat(getMessage())) < minTemperature) {
            sendMessage(ID_CHANNEL_TEMPERATURE_CONTROLLER, ID_CHILLER_OFF);
            sendMessage(ID_CHANNEL_TEMPERATURE_CONTROLLER, ID_HEATER_ON);
        }
    }

    /**
     *
     */
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (TemperatureController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TemperatureController();
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
    public static TemperatureController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    /**
     *
     */
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
        TemperatureController temperatureController = TemperatureController.getInstance();
        logger.info("Class TemperatureController --- Start Controller Temperature...");
        temperatureController.run();
    }
}
