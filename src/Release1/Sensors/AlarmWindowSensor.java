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
public class AlarmWindowSensor extends Sensor implements Runnable {

    private boolean windowState = false;

    private int currentWindowState;

    private static AlarmWindowSensor INSTANCE = new AlarmWindowSensor();

    private static final String ID_CHANNEL_AWINDOW_SENSOR = "-6";
    private static final String ID_CHANNEL_AWINDOW_CONTROLLER = "6";

    /**
     *
     */
    private AlarmWindowSensor() {
        super();
    }

    public void receiveMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_AWINDOW_CONTROLLER, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_AWINDOW_CONTROLLER, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = (new String(body, "UTF-8"));
                if (!windowState) {
                    setMessage(new String(body, "UTF-8"));
                    logger.info("Class ALARMWINDOWSENSOR --- RECEIVED From Controller --- Value: " + new String(body, "UTF-8"));
                    checkValuesExecute();
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     *
     */
    public void checkValuesExecute() {
        switch (getMessage()) {
            case "AW1":
                windowState = true;
                break;
            case "AW0":
                windowState = false;
                break;
            default:
        }
        logger.info("Class TemperatureSensor --- NewValues Window: " + windowState);
    }

    /**
     * los valores para la simulación son los siguientes: En un rango de 0 a 9
     * ... si el número generado por random es mayor que 5 entonces se genera la
     * alarma de puerta ... si el numero generado es menor que 5 entonces el
     * sistema actual normal...
     */
    @Override
    public void run() {
        /**
         * try { receiveMessage(); } catch (IOException | TimeoutException ex) {
         * logger.error(ex);
        }*
         */
        currentWindowState = 5;
        while (!isDone) {
            if (coinToss()) {
                driftValue = getRandomNumber() * (float) -1.0;
            } else {
                driftValue = getRandomNumber();
            }
            currentWindowState += Math.round(driftValue);
            try {
                sendMessage(ID_CHANNEL_AWINDOW_SENSOR, String.valueOf(currentWindowState));
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
            synchronized (AlarmWindowSensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AlarmWindowSensor();
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
    public static AlarmWindowSensor getInstance() {
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
        AlarmWindowSensor alarmWindowSensor = AlarmWindowSensor.getInstance();
        logger.info("Class AlarmWindowSensor --- Start ALARM WINDOW ...");
        alarmWindowSensor.run();
    }

}
