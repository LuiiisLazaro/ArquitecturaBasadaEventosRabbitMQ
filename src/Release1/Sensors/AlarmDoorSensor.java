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

/**
 *
 * @author luiiislazaro
 */
public class AlarmDoorSensor extends Sensor implements Runnable {

    private boolean doorState = false;

    private int currentDoorState;

    private static AlarmDoorSensor INSTANCE = new AlarmDoorSensor();

    private static final String ID_CHANNEL_ADOOR_SENSOR = "-7";
    private static final String ID_CHANNEL_ADOOR_CONTROLLER = "7";

    /**
     *
     */
    private AlarmDoorSensor() {
        super();
    }

    public void receiveMessage() throws TimeoutException, IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_ADOOR_CONTROLLER, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_ADOOR_CONTROLLER, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = (new String(body, "UTF-8"));
                if (!doorState) {
                    setMessage(new String(body, "UTF-8"));
                    logger.info("Class alarm DOOR Sensor --- RECEIVED From Controller --- Value: " + new String(body, "UTF-8"));
                    checkValuesExecute();
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    public void checkValuesExecute() {
        switch (getMessage()) {
            case "AW1":
                doorState = true;
                break;
            case "AW0":
                doorState = false;
                break;
            default:
        }
        logger.info("Class alarm DOOR sensor --- NewValues Door: " + doorState);
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
        currentDoorState = 5;
        while (!isDone) {
            if (coinToss()) {
                driftValue = getRandomNumber() * (float) -1.0;
            } else {
                driftValue = getRandomNumber();
            }
            currentDoorState += Math.round(driftValue);
            try {
                sendMessage(ID_CHANNEL_ADOOR_SENSOR, String.valueOf(currentDoorState));
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
            synchronized (AlarmDoorSensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AlarmDoorSensor();
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
    public static AlarmDoorSensor getInstance() {
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
        AlarmDoorSensor alarmDoorSensor = AlarmDoorSensor.getInstance();
        logger.info("Class AlarmDoorSensor --- Start ALARM DOOR ...");
        alarmDoorSensor.run();
    }
}