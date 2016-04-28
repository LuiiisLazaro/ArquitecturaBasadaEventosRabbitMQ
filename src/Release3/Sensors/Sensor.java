/**
 * sensor generico para las demas clases
 */
package Release3.Sensors;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * class sensor to simulate the real sensor in this program
 *
 * @author Faleg, Daniel, Luis
 */
public class Sensor extends Thread {

    private String message;         //message with value to send
    protected int delay;            //tiempo para enviar mensajes al controlador
    protected boolean active;       //avirable para saber si esta activado el sistema de alarmas
    protected boolean isDone;       //para correr el hilo en el método run
    protected float driftValue;     // The amount of temperature gained or lost

    protected static final String HOST = "localhost";   //
    protected static final Logger logger = Logger.getLogger(Sensor.class);

    /**
     *constructor
     * inicializacion de valores 
     * configuracion de logger
     */
    protected Sensor() {
        super();
        this.isDone = false;
        this.active = true;
        this.delay = 5000;
        PropertyConfigurator.configure("log4j.properties");
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected boolean coinToss() {
        Random r = new Random();
        return (r.nextBoolean());
    }

    /**
     * to create numbers and simulate the temperature
     * @return
     */
    protected float getRandomNumber() {
        Random r = new Random();
        Float val;
        val = (float) -1.0;
        while (val < 0.1) {
            val = r.nextFloat();
        }
        return (val);
    }

    /**
     *para generar numeros y simular las alarmas
     * @return
     */
    protected int getRandomNumberInt() {
        Random r = new Random();
        return r.nextInt(99 - 0 + 1) + 0;
    }

    /**
     * método para enviar mensajes a los controladores
     * @param ID_CHANNEL_SEND --- canal de comunicacion
     * @param message --- mensaje
     * @throws IOException
     * @throws TimeoutException
     */
    protected void sendMessage(String ID_CHANNEL_SEND, String message) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        logger.info("Class Sensor --- SEND to Controller --- Value:" + message);

        channel.exchangeDeclare(ID_CHANNEL_SEND, "fanout");
        channel.basicPublish(ID_CHANNEL_SEND, "", null, message.getBytes("UTF-8"));

        channel.close();
        connection.close();
    }

    /**
     * método para recibir mensajes de los controladores
     * @param ID_CHANNEL_TEMPERATURE_CONTROLLER --- canal de comunicacion
     * @throws IOException
     * @throws TimeoutException
     */
    protected synchronized void receiveMessage(String ID_CHANNEL_TEMPERATURE_CONTROLLER) throws IOException, TimeoutException {
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
                checkValues();
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * metodo para decidir acciones despues de recibir un mensaje de los controladores
     * cada sensor lo sobre escribe 
     */
    public synchronized void checkValues() {

    }
}
