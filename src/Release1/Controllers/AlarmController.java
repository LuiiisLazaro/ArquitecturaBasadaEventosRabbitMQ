/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Release1.Controllers;

import static Release1.Controllers.Controller.HOST;
import static Release1.Controllers.Controller.logger;
import Release1.Sensors.AlarmDoorSensor;
import Release1.Sensors.AlarmMoveSensor;
import Release1.Sensors.AlarmWindowSensor;
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
public class AlarmController extends Controller {

    private static AlarmController INSTANCE = new AlarmController();

    private static final String ID_CHANNEL_AWINDOW_CONTROLLER = "6";
    private static final String ID_CHANNEL_AWINDOW_SENSOR = "-6";

    private static final String ID_AWINDOW_ON = "AW1";
    private static final String ID_AWINDOW_OFF = "AW0";

    private static final String ID_CHANNEL_ADOOR_CONTROLLER = "7";
    private static final String ID_CHANNEL_ADOOR_SENSOR = "-7";

    private static final String ID_ADOOR_ON = "AD1";
    private static final String ID_ADOOR_OFF = "AD0";

    private static final String ID_CHANNEL_AMOVE_CONTROLLER = "8";
    private static final String ID_CHANNEL_AMOVE_SENSOR = "-8";

    private static final String ID_AMOVE_ON = "AM1";
    private static final String ID_AMOVE_OFF = "AM0";

    /**
     *
     */
    private AlarmController() {
        super();
    }

    private synchronized void receiveAlamrWindowSensorMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_AWINDOW_SENSOR, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_AWINDOW_SENSOR, "");

        Consumer consumer;
        consumer = new DefaultConsumer(channel) {
            @Override
            public synchronized void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                setMessage(new String(body, "UTF-8"));
                logger.info("Class ALARM WINDOW Controller --- RECEIVED from Sensor --- Value: " + new String(body, "UTF-8"));
                if (Integer.parseInt(getMessage()) > 5) {
                    try {
                        sendMessage(ID_CHANNEL_AWINDOW_CONTROLLER, ID_AWINDOW_ON);
                        logger.info("Class: ALARM CONTROLLER --- SEND --- ACTIVE ALARM WINDOW");
                    } catch (TimeoutException ex) {
                        logger.error(ex);
                    }
                } else {
                    try {
                        sendMessage(ID_CHANNEL_AWINDOW_CONTROLLER, ID_AWINDOW_OFF);
                    } catch (TimeoutException ex) {
                        logger.error(ex);
                    }
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    private void receiveAlarmDoorMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_ADOOR_SENSOR, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_ADOOR_SENSOR, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                setMessage(new String(body, "UTF-8"));
                logger.info("Class Alarm DOOR Controller --- RECEIVED from Sensor --- Value: "+ new String(body, "UTF-8"));
                if (Integer.parseInt(getMessage())>5){
                    try{
                        sendMessage(ID_CHANNEL_ADOOR_CONTROLLER, ID_ADOOR_ON);
                    } catch (TimeoutException ex) {
                        logger.error(ex);
                    }
                }else{
                    try {
                        sendMessage(ID_CHANNEL_ADOOR_CONTROLLER, ID_ADOOR_OFF);
                    } catch (TimeoutException ex) {
                        logger.error(ex);
                    }
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    private void receiveMoveMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_AMOVE_SENSOR, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_AMOVE_SENSOR, "");

        Consumer consumer;
        consumer = new DefaultConsumer(channel) {
            @Override
            public synchronized void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                setMessage(new String(body, "UTF-8"));
                logger.info("Class ALARM MOVE Controller --- RECEIVED from Sensor --- Value: " + new String(body, "UTF-8"));
                if (Integer.parseInt(getMessage()) > 5) {
                    try {
                        sendMessage(ID_CHANNEL_AMOVE_CONTROLLER, ID_AMOVE_ON);
                        logger.info("Class: ALARM CONTROLLER --- SEND --- ACTIVE ALARM WINDOW");
                    } catch (TimeoutException ex) {
                        logger.error(ex);
                    }
                } else {
                    try {
                        sendMessage(ID_CHANNEL_AMOVE_CONTROLLER, ID_AMOVE_OFF);
                    } catch (TimeoutException ex) {
                        logger.error(ex);
                    }
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     *
     */
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (AlarmController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AlarmController();
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
    public static AlarmController getInstance() {
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
            receiveAlamrWindowSensorMessage();
            receiveAlarmDoorMessage();
            receiveMoveMessage();
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        
        AlarmWindowSensor alarmWindowSensor = AlarmWindowSensor.getInstance();
        logger.info("Class AlarmWindowSensor --- Start ALARM WINDOW ...");
        alarmWindowSensor.start();
        
        AlarmDoorSensor alarmDoorSensor = AlarmDoorSensor.getInstance();
        logger.info("Class AlarmDoorSensor --- Start ALARM WINDOW ...");
        alarmDoorSensor.start();
        
        AlarmMoveSensor alarmMoveSensor = AlarmMoveSensor.getInstance();
        logger.info("Class AlarmMoveSensor --- Start ALARM WINDOW ...");
        alarmMoveSensor.start();
        
    }

    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        AlarmController alarmWindowController = AlarmController.getInstance();
        logger.info("Class TemperatureController --- Start Controller Temperature...");
        alarmWindowController.start();
    }

}
