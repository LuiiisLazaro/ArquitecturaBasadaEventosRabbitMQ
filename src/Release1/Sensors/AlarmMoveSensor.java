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
public class AlarmMoveSensor extends Sensor{

    private boolean moveState = false;

    private int currentMoveState;

    private static AlarmMoveSensor INSTANCE = new AlarmMoveSensor();

    private static final String ID_CHANNEL_AMOVE_SENSOR = "-8";
    private static final String ID_CHANNEL_AMOVE_CONTROLLER = "8";

    /**
     *
     */
    private AlarmMoveSensor() {
        super();
    }

    @Override
    public void checkValues() {
        switch (getMessage()) {
            case "AW1":
                moveState = true;
                break;
            case "AW0":
                moveState = false;
                break;
            default:
        }
        logger.info("Class alarm MOVE sensor --- NewValues Move: " + moveState);
    }

    /**
     *
     */
    @Override
    public void run() {
        try {
            receiveMessage(ID_CHANNEL_AMOVE_CONTROLLER);
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        currentMoveState = 5;
        while (!isDone) {
            if (coinToss()) {
                driftValue = getRandomNumber() * (float) -1.0;
            } else {
                driftValue = getRandomNumber();
            }
            currentMoveState += Math.round(driftValue);
            try {
                sendMessage(ID_CHANNEL_AMOVE_SENSOR, String.valueOf(currentMoveState));
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
            synchronized (AlarmMoveSensor.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AlarmMoveSensor();
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
    public static AlarmMoveSensor getInstance() {
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
        AlarmMoveSensor alarmMoveSensor = AlarmMoveSensor.getInstance();
        logger.info("Class AlarmMoveSensor --- Start ALARM MOVE ...");
        alarmMoveSensor.start();
    }
}
