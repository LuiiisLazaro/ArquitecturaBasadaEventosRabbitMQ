/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Release1.Controllers;

/**
 *
 */
import Release1.Sensors.TemperatureSensor;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author luiiislazaro
 */
public class TemperatureController extends Controller {

    private static TemperatureController INSTANCE = new TemperatureController();

    private static final String ID_CHANNEL_TEMPERATURE_CONTROLLER = "5";
    private static final String ID_CHANNEL_TEMPERATURE_SENSOR = "-5";
    private static final String ID_CHANNEL_CHANGE_TEMPERATURE = "CT";

    private int maxTemperature;
    private int minTemperature;

    private static final String ID_HEATER_ON = "H1";
    private static final String ID_HEATER_OFF = "H0";

    private static final String ID_CHILLER_ON = "C1";
    private static final String ID_CHILLER_OFF = "C0";

    /**
     *
     */
    private TemperatureController() {
        super();
        this.maxTemperature = 75;
        this.minTemperature = 70;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }

    /**
     *
     * @throws IOException
     * @throws TimeoutException
     */
    @Override
    public void checkValues() {
        logger.info("Class TemperatureController --- SEND to Controller ...");
        if (Math.round(Float.parseFloat(getMessage())) > maxTemperature) {
            try {
                sendMessage(ID_CHANNEL_TEMPERATURE_CONTROLLER, ID_CHILLER_ON);
                sendMessage(ID_CHANNEL_TEMPERATURE_CONTROLLER, ID_HEATER_OFF);
            } catch (IOException | TimeoutException ex) {
                logger.error(ex);
            }
        } else if (Math.round(Float.parseFloat(getMessage())) < minTemperature) {
            try {
                sendMessage(ID_CHANNEL_TEMPERATURE_CONTROLLER, ID_CHILLER_OFF);
                sendMessage(ID_CHANNEL_TEMPERATURE_CONTROLLER, ID_HEATER_ON);
            } catch (IOException | TimeoutException ex) {
                logger.error(ex);
            }
        }
    }

    /**
     *
     */
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (TemperatureController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TemperatureController();
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
    public static TemperatureController getInstance() {
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
            receiveMessage(ID_CHANNEL_TEMPERATURE_SENSOR);
            receiveMessage(ID_CHANNEL_CHANGE_TEMPERATURE);
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            logger.error(ex);
        }
        
        
        //para fines de debug dejar las siguientes lineas de codigo
        TemperatureSensor temperatureSensor = TemperatureSensor.getInstance();
        logger.info("Class TemperatureSensot --- Start Sensor Temperature...");
        temperatureSensor.start();
        
    }

    /**
     *
     * @param args
     */
    public static void main(String args[]) {

    }
}
