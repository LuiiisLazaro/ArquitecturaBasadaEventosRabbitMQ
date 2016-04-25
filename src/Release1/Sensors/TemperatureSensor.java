/**
 *
 */
package Release1.Sensors;

/**
 *
 */
import static Release1.Sensors.Sensor.HOST;
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
public class TemperatureSensor extends Sensor implements Runnable {

    private boolean heaterState = false;	// Heater state: false == off, true == on
    private boolean chillerState = false;	// Chiller state: false == off, true == on
    private float currentTemperature;		// Current simulated ambient room temperature

    private static TemperatureSensor INSTANCE = new TemperatureSensor();

    private static final String ID_CHANNEL_TEMPERATURE_SENSOR = "-5";       //channel ID to send messages
    private static final String ID_CHANNEL_TEMPERATURE_CONTROLLER = "5";    //channel ID to receive messages
    
    

    /**
     *
     */
    private TemperatureSensor() {
        super();
    }

    /**
     *
     * @throws IOException
     * @throws TimeoutException
     */
    public void receiveMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_TEMPERATURE_CONTROLLER, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_TEMPERATURE_CONTROLLER, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                setMessage(new String(body, "UTF-8"));
                logger.info("Class Sensor --- RECEIVED From Controller --- Value: " + new String(body, "UTF-8"));
                checkValuesExecute();
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     *
     */
    public void checkValuesExecute() {
        switch (getMessage()) {
            case "H1":
                heaterState = true;
                break;
            case "H0":
                heaterState = false;
                break;
            case "C1":
                chillerState = true;
                break;
            case "C0":
                chillerState = false;
                break;
            default:
        }
        logger.info("Class TemperatureSensor --- NewValues Heater: " + heaterState + " , Chiller: " + chillerState);
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            receiveMessage();
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        currentTemperature = (float) 70.00;
        while (!isDone) {

            if (coinToss()) {
                driftValue = getRandomNumber() * (float) -1.0;
            } else {
                driftValue = getRandomNumber();
            }
            currentTemperature += driftValue;
            try {
                sendMessage(ID_CHANNEL_TEMPERATURE_SENSOR, String.valueOf(currentTemperature));
            } catch (IOException | TimeoutException e1) {
                logger.error(e1);
            }
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    /**
     *
     */
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (TemperatureSensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TemperatureSensor();
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
    public static TemperatureSensor getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        TemperatureSensor temperatureSensor = TemperatureSensor.getInstance();
        logger.info("Class TemperatureSensot --- Start Sensor Temperature...");
        temperatureSensor.run();
    }

}
