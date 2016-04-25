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
import javax.imageio.IIOException;
import org.omg.CORBA.TIMEOUT;

/**
 *
 * @author luiiislazaro
 */
public class AlarmDoorSensor extends Sensor implements Runnable{
    
    private boolean doorState = false;
    
    private int currentDoorState; 
    
    private static AlarmDoorSensor INSTANCE = new AlarmDoorSensor();
    
    private static final String ID_CHANNEL_ADOOR_SENSOR="-7";
    private static final String ID_CHANNEL_ADOOR_CONTROLLER="7";
    
    /**
     * 
     */
    private AlarmDoorSensor(){
        super();
    }
    
    public void receiveMessage() throws IIOException, TimeoutException{
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
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}