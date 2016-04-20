package Release1.Sensors;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;

public class Sensor {

    protected int delay = 2000;				// The loop delay (2.5 seconds)
    protected boolean isDone = false;			// Loop termination flag
    protected float driftValue;				// The amount of temperature gained or lost
    private final static String QUEUE_NAME = "hello";
    
    protected Sensor() {}

    protected boolean coinToss() {
        Random r = new Random();
        return (r.nextBoolean());
    } // CoinToss
    
    protected float getRandomNumber() {
        Random r = new Random();
        Float val;
        val = Float.valueOf((float) -1.0);
        while (val < 0.1) {
            val = r.nextFloat();
        }
        return (val.floatValue());
    } // GetRandomNumber

    protected void sendMessage(String message) throws IOException, TimeoutException {
    	System.out.println(message);
    	ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
    
    protected void receiveMessage(){
    	//recibir mensaje por medio de rabbitmq recibir orden
    }
    
}
