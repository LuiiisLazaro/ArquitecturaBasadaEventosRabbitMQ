/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Release1.Sensors;

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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author luiiislazaro
 */
public class HumiditySensor extends Sensor implements Runnable {

    private boolean humidityState = false;
    private boolean deshumidityState =false;
    private float currentHumidity;

    private static HumiditySensor INSTANCE = new HumiditySensor();

    private static final String ID_CHANNEL_HUMIDITY_SENSOR = "-4";
    private static final String ID_CHANNEL_HUMIDITY_CONTROLLER = "4";

    private HumiditySensor() {
        super();
    }

    public void receiveMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_HUMIDITY_CONTROLLER, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_HUMIDITY_CONTROLLER, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                setMessage(new String(body, "UTF-8"));
                logger.info("Class HumiditySensor --- RECEIVED From Controller --- Value" + new String(body, "UTF-8"));
                checkValuesExecute();

            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    public void checkValuesExecute() {
        switch (getMessage()) {
            case "H1":
                humidityState = true;
                break;
            case "H0":
                humidityState = false;
                break;
            case "D1":
                deshumidityState=true;
                break;
            case "D0":
                deshumidityState= false;
                break;
                
            default:
        }
        logger.info("Class HumiditySensor --- NewValues Humidity:" + humidityState+" , Deshumidity: "+deshumidityState);
    }

    @Override
    public void run() {
        try {
            receiveMessage();
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        currentHumidity = 45;

        while (!isDone) {
            if (coinToss()) {
                driftValue = getRandomNumber() * (float) -1.0;
            } else {
                driftValue = getRandomNumber();
            }
            currentHumidity += driftValue;
            try {
                sendMessage(ID_CHANNEL_HUMIDITY_SENSOR, String.valueOf(currentHumidity));
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

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (TemperatureSensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HumiditySensor();
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
    public static HumiditySensor getInstance() {
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
        HumiditySensor humiditySensor = HumiditySensor.getInstance();
        logger.info("Class HumiditysSensor --- Start Sensor Temperature...");
        humiditySensor.run();
    }

}
