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
public class AlarmDoorSensor extends Sensor {

    private boolean doorState = true;

    private int currentDoorState;

    public boolean isDoorState() {
        return doorState;
    }

    public void setDoorState(boolean doorState) {
        this.doorState = doorState;
    }

    public int getCurrentDoorState() {
        return currentDoorState;
    }

    public void setCurrentDoorState(int currentDoorState) {
        this.currentDoorState = currentDoorState;
    }

    private static AlarmDoorSensor INSTANCE = new AlarmDoorSensor();

    private static final String ID_CHANNEL_ADOOR_SENSOR = "-7";
    private static final String ID_CHANNEL_ADOOR_CONTROLLER = "7";

    /**
     *
     */
    private AlarmDoorSensor() {
        super();
    }

    @Override
    public void checkValues() {
        switch (getMessage()) {
            case "AD1":
                setDoorState(false);
                break;
            case "AD0":
                setDoorState(true);
                setCurrentDoorState(3);
                break;
            default:
        }
        logger.info("Class: ALARM WINDOWS --- SET VALUES --- windowstate: " + doorState);
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            receiveMessage(ID_CHANNEL_ADOOR_CONTROLLER);
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        while (!isDone) {
            if (doorState) {
                currentDoorState = getRandomNumberInt();
                 try {
                    logger.info("Send current DOOR state:" + currentDoorState);
                    sendMessage(ID_CHANNEL_ADOOR_SENSOR, String.valueOf(currentDoorState));
                } catch (IOException | TimeoutException e1) {
                    logger.error(e1);
                }
                try {
                    Thread.sleep(delay);
                } catch (Exception e) {
                    logger.error(e);
                }
            } else {
                logger.info("ESPERANDO CLIC DESDE VENTANA DOOR");
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ex) {
                    logger.error(ex);
                }
            }
        }
    }
    
    /**
     *
     */
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (AlarmDoorSensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AlarmDoorSensor();
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
    public static AlarmDoorSensor getInstance() {
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
        AlarmDoorSensor alarmDoorSensor = AlarmDoorSensor.getInstance();
        logger.info("Class AlarmDoorSensor --- Start ALARM DOOR ...");
        alarmDoorSensor.start();
    }
}
