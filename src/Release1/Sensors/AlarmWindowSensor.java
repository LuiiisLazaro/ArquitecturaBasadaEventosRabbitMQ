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
public class AlarmWindowSensor extends Sensor {

    private boolean windowState = false;

    private int currentWindowState;

    private static AlarmWindowSensor INSTANCE = new AlarmWindowSensor();

    private static final String ID_CHANNEL_AWINDOW_SENSOR = "-6";
    private static final String ID_CHANNEL_AWINDOW_CONTROLLER = "6";

    /**
     *
     */
    private AlarmWindowSensor() {
        super();
    }
    
    /**
     *
     */
    @Override
    public void checkValues() {
        switch (getMessage()) {
            case "AW1":
                windowState = true;
                break;
            case "AW0":
                windowState = false;
                break;
            default:
        }
        logger.info("Class Alarm WINDOW --- NewValues WINDOW: " + windowState);
    }

    /**
     * los valores para la simulación son los siguientes: En un rango de 0 a 9
     * ... si el número generado por random es mayor que 5 entonces se genera la
     * alarma de puerta ... si el numero generado es menor que 5 entonces el
     * sistema actual normal...
     */
    public void run() {
        try {
            receiveMessage(ID_CHANNEL_AWINDOW_CONTROLLER);
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        currentWindowState = 6;
        while (!isDone) {
            if (coinToss()) {
                currentWindowState += Math.round(getRandomNumber() * (float) -1.0);
            } else {
                currentWindowState += Math.round(getRandomNumber());
            }
            try {
                sendMessage(ID_CHANNEL_AWINDOW_SENSOR, String.valueOf(currentWindowState));
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

    /**
     *
     */
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (AlarmWindowSensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AlarmWindowSensor();
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
    public static AlarmWindowSensor getInstance() {
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
        AlarmWindowSensor alarmWindowSensor = AlarmWindowSensor.getInstance();
        logger.info("Class AlarmWindowSensor --- Start ALARM WINDOW ...");
        alarmWindowSensor.start();
    }

}
