/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Release2.Sensors;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Faleg, Daniel, Luis
 */
public class HumiditySensor extends Sensor {

    private boolean humidityState;
    private boolean deshumidityState;
    private float currentHumidity;

    private static HumiditySensor INSTANCE = new HumiditySensor();

    private static final String ID_CHANNEL_HUMIDITY_SENSOR = "-4"; //id para el canal de comunicacion de sensor de humedad
    private static final String ID_CHANNEL_HUMIDITY_CONTROLLER = "4";//id para el canal de comunicacion del controlador de humedad

    /**
     *contructor para inicializar valores
     */
    private HumiditySensor() {
        super();
        this.deshumidityState = false;
        this.humidityState = false;
        this.currentHumidity = 47;
    }

    /**
     * método para decidir acciones de acuerdo al mensaje recibido del controlador
     */
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
                deshumidityState = true;
                break;
            case "D0":
                deshumidityState = false;
                break;

            default:
        }
        logger.info("Class HumiditySensor --- NewValues Humidity:" + humidityState + " , Deshumidity: " + deshumidityState);
    }

    /**
     * método para correr el hilo
     * recibe mensajes del controlador de humedada
     * envia valor de humedad al controlador
     */
    @Override
    public void run() {
        try {
            receiveMessage(ID_CHANNEL_HUMIDITY_CONTROLLER);//recepcion de mensajes
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        while (!isDone) {
            if (coinToss()) {
                driftValue = getRandomNumber() * (float) -1.0;
            } else {
                driftValue = getRandomNumber();
            }
            currentHumidity += driftValue;
            try {
                sendMessage(ID_CHANNEL_HUMIDITY_SENSOR, String.valueOf(currentHumidity));//envio de mensajes
            } catch (IOException | TimeoutException e1) {
                logger.error(e1);
            }
            if (humidityState) {
                currentHumidity += getRandomNumber();
            }
            if (!humidityState && !deshumidityState) {
                currentHumidity += driftValue;
            }
            if (deshumidityState) {
                currentHumidity -= getRandomNumber();
            }
            try {
                Thread.sleep(delay);
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    /**
     * crear instancia
     */
    private static void createInstance() {
        synchronized (HumiditySensor.class) {
            if (INSTANCE == null) {
                INSTANCE = new HumiditySensor();
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

    public static void main(String args[]) {
        HumiditySensor humiditySensor = HumiditySensor.getInstance();
        logger.info("Class HumiditysSensor --- Start Sensor Temperature...");
        humiditySensor.start();
    }
}
