/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Release1.Sensors;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author luiiislazaro
 */
public class HumiditySensor extends Sensor {

    private boolean humidityState = false;
    private boolean deshumidityState =false;
    private float currentHumidity;

    private static HumiditySensor INSTANCE = new HumiditySensor();

    private static final String ID_CHANNEL_HUMIDITY_SENSOR = "-4";
    private static final String ID_CHANNEL_HUMIDITY_CONTROLLER = "4";

    private HumiditySensor() {
        super();
    }

    @Override
    public void checkValues() {
        switch (getMessage()) {
            case "H1":
                humidityState = true;
                break;
            case "H0":
                humidityState = false;
                break;
            case "D1":
                deshumidityState=true;
                break;
            case "D0":
                deshumidityState= false;
                break;
                
            default:
        }
        logger.info("Class HumiditySensor --- NewValues Humidity:" + humidityState+" , Deshumidity: "+deshumidityState);
    }

    @Override
    public void run() {
        try {
            receiveMessage(ID_CHANNEL_HUMIDITY_CONTROLLER);
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        currentHumidity = 47;
        while (!isDone) {
            if (coinToss()) {
                driftValue = getRandomNumber() * (float) -1.0;
            } else {
                driftValue = getRandomNumber();
            }
            currentHumidity += driftValue;
            try {
                sendMessage(ID_CHANNEL_HUMIDITY_SENSOR, String.valueOf(currentHumidity));
            } catch (IOException | TimeoutException e1) {
                logger.error(e1);
            }
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (HumiditySensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HumiditySensor();
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
    public static HumiditySensor getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        HumiditySensor humiditySensor = HumiditySensor.getInstance();
        logger.info("Class HumiditysSensor --- Start Sensor Temperature...");
        humiditySensor.start();
    }

}
