/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Release3.Controllers;

import static Release3.Controllers.Controller.HOST;
import Release2.Sensors.AlarmFireSensor;
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
public class AlarmFireController extends Controller {

    private boolean active;

    private static AlarmFireController INSTANCE = new AlarmFireController();

    private static final String ID_CHANNEL_AFIRE_CONTROLLER = "9";
    private static final String ID_CHANNEL_AFIRE_SENSOR = "-9";

    private static final int MAX_VALUE_ALARMS = 50;

    private static final String ID_AFIRE_ON = "AF1";
    private static final String ID_AFIRE_OFF = "AF0";

    private final AlarmFireSensor alarmFireSensor = AlarmFireSensor.getInstance();

    private AlarmFireController() {
        super();
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private synchronized void receivedAlarmFireSensorMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_AFIRE_SENSOR, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_AFIRE_SENSOR, "");

        Consumer consumer;
        consumer = new DefaultConsumer(channel) {
            @Override
            public synchronized void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                setMessage(new String(body, "UTF-8"));
                logger.info("Class ALARM FIRE Controller --- RECEIVED from Sensor --- Value: " + new String(body, "UTF-8"));
                if (Integer.parseInt(getMessage()) > MAX_VALUE_ALARMS) {//si el valor obtenido es mayor al permitido para simular las alarmas se activa la alarma
                    try {
                        sendMessage(ID_CHANNEL_AFIRE_CONTROLLER, ID_AFIRE_ON);//envio de mensaje para encender alarma la alarma de ventnaa
                        logger.info("Class: ALARM CONTROLLER --- SEND --- ACTIVE ALARM WINDOW");
                    } catch (TimeoutException ex) {
                        logger.error(ex);
                    }
                } else {
                    try {
                        sendMessage(ID_CHANNEL_AFIRE_CONTROLLER, ID_AFIRE_OFF);//envio de mensaje apra apagar la alarma de ventana
                    } catch (TimeoutException ex) {
                        logger.error(ex);
                    }
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * creacion de instancia
     */
    private static void createInstance() {
        synchronized (AlarmFireController.class) {
            if (INSTANCE == null) {
                INSTANCE = new AlarmFireController();
            }
        }
    }

    /**
     * This method calls createInstance method to creates and ensure that only
     * one instance of this class is created. Singleton design pattern.
     *
     * @return The instance of this class.
     */
    public static AlarmFireController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    /**
     *
    public void resumeThread() {
        setActive(!isActive());
        alarmFireSensor.setActive(isActive());
    }
    */
    public void run(){
        try {
            receivedAlarmFireSensorMessage();
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            logger.error(ex);
        }
        logger.info("Class AlarmFIRESensor --- Start ALARM FIRE ...");
        alarmFireSensor.start();
    }
    
     public static void main(String args[]) {
        AlarmFireController alarmFireController = AlarmFireController.getInstance();
        logger.info("Class TemperatureController --- Start Controller ALARM FIRE...");
        alarmFireController.start();
    }
}
