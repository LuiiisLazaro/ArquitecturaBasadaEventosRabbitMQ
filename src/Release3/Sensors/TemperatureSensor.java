/**
 * TEMPERATURESENSOR. simiulador de dispositivo de temperatura
 */
package Release3.Sensors;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Faleg, Daniel, Luis
 */
public class TemperatureSensor extends Sensor {

    private boolean heaterState = false;	// Heater state: false == off, true == on
    private boolean chillerState = false;	// Chiller state: false == off, true == on
    private float currentTemperature;		// Current simulated ambient room temperature

    private static TemperatureSensor INSTANCE = new TemperatureSensor();

    private static final String ID_CHANNEL_TEMPERATURE_SENSOR = "-5";       //channel ID to send messages
    private static final String ID_CHANNEL_TEMPERATURE_CONTROLLER = "5";    //channel ID to receive messages

    /**
     * constructors
     */
    private TemperatureSensor() {
        super();
    }

    /**
     * sobre escritura del método checkvalues para controlar los dispositivos
     * heater y chiller
     */
    @Override
    public void checkValues() {
        switch (getMessage()) {
            case "H1":
                heaterState = true;
                break;
            case "H0":
                heaterState = false;
                break;
            case "C1":
                chillerState = true;
                break;
            case "C0":
                chillerState = false;
                break;
            default:
        }
        logger.info("Class TemperatureSensor --- NewValues Heater: " + heaterState + " , Chiller: " + chillerState);
    }

    /**
     * aqui se reciben mensajes del controlador y se envia la temperatura al controlador
     */
    @Override
    public void run() {
        try {
            receiveMessage(ID_CHANNEL_TEMPERATURE_CONTROLLER);//recepcion de mensaje
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        currentTemperature = (float) 70.00;
        while (!isDone) {
            if (coinToss()) {
                driftValue = getRandomNumber() * (float) -1.0;
            } else {
                driftValue = getRandomNumber();
            }
            currentTemperature += driftValue;
            try {
                sendMessage(ID_CHANNEL_TEMPERATURE_SENSOR, String.valueOf(currentTemperature));//envio de mensaje
            } catch (IOException | TimeoutException e1) {
                logger.error(e1);
            }
            if (heaterState) {
                currentTemperature += getRandomNumber();
            }
            if (!heaterState && !chillerState) {
                currentTemperature += driftValue;
            }
            if (chillerState) {
                currentTemperature -= getRandomNumber();
            }
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    /**
     * creacion de la instacia para un objeto temperaturesensor
     */
    private static void createInstance() {
        synchronized (TemperatureSensor.class) {
            if (INSTANCE == null) {
                INSTANCE = new TemperatureSensor();
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

    public static void main(String args[]) {
        TemperatureSensor temperatureSensor = TemperatureSensor.getInstance();
        logger.info("Class TemperatureSensot --- Start Sensor Temperature...");
        temperatureSensor.start();
    }
}
