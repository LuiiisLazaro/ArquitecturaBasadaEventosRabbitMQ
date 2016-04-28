/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Release3.Controllers;

import static Release3.Controllers.Controller.HOST;
import static Release3.Controllers.Controller.logger;
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
 *@author Faleg, Daniel, Luis
 */
public class AlarmController extends Controller {

    private boolean active;

    private static AlarmController INSTANCE = new AlarmController();

    private static final String ID_CHANNEL_AWINDOW_CONTROLLER = "6";//id del canal de comunicacion para el controlador de alarmas de ventanas
    private static final String ID_CHANNEL_AWINDOW_SENSOR = "-6";//id del canal de comunicacion para el sensor de la alarma de ventanas
    private static final int MAX_VALUE_ALARMS = 90;//valor para determinar si la alarma se activa

    private static final String ID_AWINDOW_ON = "AW1";//clave para encender alarma de ventana
    private static final String ID_AWINDOW_OFF = "AW0";//clave para apagar alarma de ventana

    private static final String ID_CHANNEL_ADOOR_CONTROLLER = "7";//id del canal de comunicacion para el controlador de alarmas de puertas
    private static final String ID_CHANNEL_ADOOR_SENSOR = "-7";//id del canal de comunicacion para el sensor de alarmas de puerta

    private static final String ID_ADOOR_ON = "AD1";//clave para encender la alarma de puerta
    private static final String ID_ADOOR_OFF = "AD0";//clave para apagar la alarma de puerta

    private static final String ID_CHANNEL_AMOVE_CONTROLLER = "8";//id del canal de comunicacion para el controlador de alarmas de movimiento
    private static final String ID_CHANNEL_AMOVE_SENSOR = "-8";//id del canal de comunicacion para el sensor de alamas de movimiento

    private static final String ID_AMOVE_ON = "AM1";//clave para encender alarma de movimiento
    private static final String ID_AMOVE_OFF = "AM0";//clave para apagar alarma de movimiento

    //hilo para los sensores de alarmas
    private final AlarmWindowSensor alarmWindowSensor = AlarmWindowSensor.getInstance();
    private final AlarmDoorSensor alarmDoorSensor = AlarmDoorSensor.getInstance();
    private final AlarmMoveSensor alarmMoveSensor = AlarmMoveSensor.getInstance();

    /**
     *constructor 
     * inicializacion de valores
     */
    private AlarmController() {
        super();
        this.active = true;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * método para recibir mensajes de los sensores de alarmas de ventana
     * dependiendo del mensaje recibido se envia la respuesta a los sensores
     * @throws IOException
     * @throws TimeoutException 
     */
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
                if (Integer.parseInt(getMessage()) > MAX_VALUE_ALARMS) {//si el valor obtenido es mayor al permitido para simular las alarmas se activa la alarma
                    try {
                        sendMessage(ID_CHANNEL_AWINDOW_CONTROLLER, ID_AWINDOW_ON);//envio de mensaje para encender alarma la alarma de ventnaa
                        logger.info("Class: ALARM CONTROLLER --- SEND --- ACTIVE ALARM WINDOW");
                    } catch (TimeoutException ex) {
                        logger.error(ex);
                    }
                }else {
                    try {
                        sendMessage(ID_CHANNEL_AWINDOW_CONTROLLER, ID_AWINDOW_OFF);//envio de mensaje apra apagar la alarma de ventana
                    } catch (TimeoutException ex) {
                        logger.error(ex);
                    }
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * método para recibir mensajes de los sensores de alarmas de puerta
     * dependiendo del mensaje recibido se envia la respuesta a los sensores
     * @throws IOException
     * @throws TimeoutException 
     */
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
                logger.info("Class Alarm DOOR Controller --- RECEIVED from Sensor --- Value: " + new String(body, "UTF-8"));
                if (Integer.parseInt(getMessage()) > MAX_VALUE_ALARMS) {
                    try {
                        sendMessage(ID_CHANNEL_ADOOR_CONTROLLER, ID_ADOOR_ON);//envio de mensaje para encender la alarma
                    } catch (TimeoutException ex) {
                        logger.error(ex);
                    }
                } else {
                    try {
                        sendMessage(ID_CHANNEL_ADOOR_CONTROLLER, ID_ADOOR_OFF);//envio de mensajes para apagar la alarma
                    } catch (TimeoutException ex) {
                        logger.error(ex);
                    }
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * * método para recibir mensajes de los sensores de alarmas de movimiento
     * dependiendo del mensaje recibido se envia la respuesta a los sensores
     * @throws IOException
     * @throws TimeoutException 
     */
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
                if (Integer.parseInt(getMessage()) > MAX_VALUE_ALARMS) {
                    try {
                        sendMessage(ID_CHANNEL_AMOVE_CONTROLLER, ID_AMOVE_ON);//envio de mensajes para encender la alarma de movimiento
                        logger.info("Class: ALARM CONTROLLER --- SEND --- ACTIVE ALARM WINDOW");
                    } catch (TimeoutException ex) {
                        logger.error(ex);
                    }
                } 
                else {
                    try {
                        sendMessage(ID_CHANNEL_AMOVE_CONTROLLER, ID_AMOVE_OFF);//envio de mensajes para apagar la alarma de movimientos
                    } catch (TimeoutException ex) {
                        logger.error(ex);
                    }
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     *creacion de instancia
     */
    private static void createInstance() {
        synchronized (AlarmController.class) {
            if (INSTANCE == null) {
                INSTANCE = new AlarmController();
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
     * metodo para activar el envio de mensajes de los hilos de las alarmas de los sensores 
     */
    public void resumeThread() {
        setActive(!isActive());
        alarmDoorSensor.setActive(isActive());
        alarmMoveSensor.setActive(isActive());
        alarmWindowSensor.setActive(isActive());
    }

    /**
     *metodo para correr el hilo
     * inicia preparando la recepcion de mensajes de los sensores 
     * e inicia los hilos de los sensores de alarmas
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
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            logger.error(ex);
        }
        logger.info("Class AlarmWindowSensor --- Start ALARM WINDOW ...");
        logger.info("Class AlarmDoorSensor --- Start ALARM WINDOW ...");
        logger.info("Class AlarmMoveSensor --- Start ALARM WINDOW ...");
        alarmWindowSensor.start();
        alarmDoorSensor.start();
        alarmMoveSensor.start();
    }

    public static void main(String args[]) {
        AlarmController alarmWindowController = AlarmController.getInstance();
        logger.info("Class AlarmController --- Start Controller Alarms...");
        alarmWindowController.start();
    }
}
