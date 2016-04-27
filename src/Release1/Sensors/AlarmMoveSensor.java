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

    private boolean moveState = true;

    public boolean isMoveState() {
        return moveState;
    }

    public void setMoveState(boolean moveState) {
        this.moveState = moveState;
    }

    public int getCurrentMoveState() {
        return currentMoveState;
    }

    public void setCurrentMoveState(int currentMoveState) {
        this.currentMoveState = currentMoveState;
    }

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
            case "AM1":
                setMoveState(false);
                break;
            case "AM0":
                setMoveState(true);
                setCurrentMoveState(3);
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
        while (!isDone) {
            if (moveState){
                currentMoveState= getRandomNumberInt();
                try{
                    logger.info("Send current MOVE state:" + currentMoveState);
                    sendMessage(ID_CHANNEL_AMOVE_SENSOR, String.valueOf(currentMoveState));
                } catch (IOException | TimeoutException ex) {
                    logger.error(ex);
                }
                try {
                    Thread.sleep(delay);
                } catch (Exception e) {
                    logger.error(e);
                }
            }else {
                logger.info("ESPERANDO CLIC MOVE");
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
