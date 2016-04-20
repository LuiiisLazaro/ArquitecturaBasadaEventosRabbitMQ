package Release1.Sensors;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TemperatureSensor extends Sensor implements Runnable {

    private boolean heaterState = false;	// Heater state: false == off, true == on
    private boolean chillerState = false;	// Chiller state: false == off, true == on
    private float currentTemperature;		// Current simulated ambient room temperature

    private static TemperatureSensor INSTANCE = new TemperatureSensor();

    private TemperatureSensor() {
        super();
    }

    public void run() {
		currentTemperature = (float) 70.00;
		while (!isDone) {
			
			if (coinToss()) {
				driftValue = getRandomNumber() * (float) -1.0;
			} else {
				driftValue = getRandomNumber();
			} 
			currentTemperature += driftValue;
			try {
				sendMessage(String.valueOf(currentTemperature));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (TimeoutException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Thread.sleep(delay);
			} // try
			catch (Exception e) {
			} // catch
		} // while
	}

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
	 * Start this sensor
	 * 
	 * @param args
	 *            IP address of the event manager (on command line). If blank,
	 *            it is assumed that the event manager is on the local machine.
	 */
	public static void main(String args[]) {		
		TemperatureSensor sensor = TemperatureSensor.getInstance();
		sensor.run();
	}

}
