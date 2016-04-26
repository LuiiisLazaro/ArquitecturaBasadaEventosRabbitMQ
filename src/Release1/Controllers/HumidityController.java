/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Release1.Controllers;

/**
 *
 */
import static Release1.Controllers.Controller.logger;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author luiiislazaro
 */
public class HumidityController extends Controller implements Runnable {

    private static HumidityController INSTANCE = new HumidityController();

    private static final String ID_CHANNEL_HUMIDITY_CONTROLLER = "4";
    private static final String ID_CHANNEL_HUMIDITY_SENSOR = "-4";
    private static final String ID_CHANNEL_CHANGE_HUMIDITY = "CH";

    private int maxHumidity;
    private int minHumidity;

    private static final String ID_HUMIDITY_ON = "H1";
    private static final String ID_HUMIDITY_OFF = "H0";

    private static final String ID_DESHUMIDITY_ON = "D1";
    private static final String ID_DESHUMIDITY_OFF = "D0";

    /**
     *
     */
    private HumidityController() {
        super();
        this.minHumidity = 45;
        this.maxHumidity = 55;
    }

    public int getMaxHumidity() {
        return maxHumidity;
    }

    public void setMaxHumidity(int maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public int getMinHumidity() {
        return minHumidity;
    }

    public void setMinHumidity(int minHumidity) {
        this.minHumidity = minHumidity;
    }

    /**
     *
     * @throws IOException
     * @throws TimeoutException
     */
    @Override
    public void checkValues() {
        logger.info("Class HumidityController --- SEND to Controller ...");

        if (Math.round(Float.parseFloat(getMessage())) > maxHumidity) {
            try {
                sendMessage(ID_CHANNEL_HUMIDITY_CONTROLLER, ID_HUMIDITY_OFF);
                sendMessage(ID_CHANNEL_HUMIDITY_CONTROLLER, ID_DESHUMIDITY_ON);
            } catch (IOException | TimeoutException ex) {
                logger.error(ex);
            }
        } else if (Math.round(Float.parseFloat(getMessage())) < minHumidity) {
            try {
                sendMessage(ID_CHANNEL_HUMIDITY_CONTROLLER, ID_HUMIDITY_ON);
                sendMessage(ID_CHANNEL_HUMIDITY_CONTROLLER, ID_DESHUMIDITY_OFF);
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
            synchronized (HumidityController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HumidityController();
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
    public static HumidityController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    @Override
    public void run() {
        try {
            receiveMessage(ID_CHANNEL_HUMIDITY_SENSOR);
            receiveMessage(ID_CHANNEL_CHANGE_HUMIDITY);
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        HumidityController humidityController = HumidityController.getInstance();
        logger.info("Class HumidityController --- Start Controller Humidity...");
        humidityController.start();
    }
}
