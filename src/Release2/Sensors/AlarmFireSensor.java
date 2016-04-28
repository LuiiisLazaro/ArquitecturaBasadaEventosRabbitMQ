/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Release2.Sensors;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Faleg, Daniel. Luis
 */
public class AlarmFireSensor extends Sensor {

    private boolean alarmFireState;
    private boolean sprinklersState;
    private int currentFireState;

    private static AlarmFireSensor INSTANCE = new AlarmFireSensor();

    private static final String ID_CHANNEL_AFIRE_SENSOR = "-9";
    private static final String ID_CHANNEL_AFIRE_CONTROLLER = "9";
    private static final String ID_CHANNEL_AFIRE_SPRINKLERS = "AFS";
    private static final int MAX_TIME = 10;//el tiempo de retraso aproximado es de 5seg

    /**
     *
     */
    public AlarmFireSensor() {
        this.alarmFireState = true;
        this.sprinklersState = false;
    }

    public boolean isAlarmFireState() {
        return alarmFireState;
    }

    public void setAlarmFireState(boolean alarmFireState) {
        this.alarmFireState = alarmFireState;
    }

    public int getCurrentFireState() {
        return currentFireState;
    }

    public void setCurrentFireState(int currentFireState) {
        this.currentFireState = currentFireState;
    }

    public boolean isSprinklersState() {
        return sprinklersState;
    }

    public void setSprinklersState(boolean sprinklersState) {
        this.sprinklersState = sprinklersState;
    }

    @Override
    public synchronized void checkValues() {
        switch (getMessage()) {
            case "AF1":
                setAlarmFireState(false);
                setSprinklersState(false);
                break;
            case "AF0":
                setAlarmFireState(true);
                setCurrentFireState(3);
                 {
                    try {
                        sendMessage(ID_CHANNEL_AFIRE_SPRINKLERS, String.valueOf(isSprinklersState()));
                    } catch (IOException | TimeoutException ex) {
                        logger.error(ex);
                    }
                }
                break;
            default:
        }
        logger.info("Class: ALARM FIRE --- SET VALUES --- FIREstate: " + alarmFireState);
    }

    public void run() {
        try {
            receiveMessage(ID_CHANNEL_AFIRE_CONTROLLER);
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        while (!isDone) {
            try {
                sendMessage(ID_CHANNEL_AFIRE_SPRINKLERS, String.valueOf(isSprinklersState()));
            } catch (IOException | TimeoutException ex) {
                logger.error(ex);
            }
            if (isActive()) {
                if (alarmFireState) {
                    currentFireState = getRandomNumberInt();
                    try {
                        logger.info("Send current FIRE state:" + alarmFireState);
                        sendMessage(ID_CHANNEL_AFIRE_SENSOR, String.valueOf(currentFireState));//envio de mensajes

                    } catch (IOException | TimeoutException e1) {
                        logger.error(e1);
                    }
                    try {
                        Thread.sleep(delay);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                } else {//si la alarma esta activada de un evento de instruso
                    logger.info("ESPERANDO CLIC WINDOWS FIRE");
                    try {
                        int i = 0;
                        if (!isSprinklersState()) {
                            while (i < MAX_TIME && !isAlarmFireState()) {
                                Thread.sleep(1000);
                                logger.info("Esperando confirmación :" + i + " --- de " + MAX_TIME);
                                i++;
                            }
                            logger.info("valores finales:" + i + " --- de " + MAX_TIME);
                            if (i == MAX_TIME) {
                                logger.info("INICIAR ROCIADORES");
                                setSprinklersState(true);
                            } else {
                                logger.info("CANCELACION DE ROCIADORES");
                                setSprinklersState(false);
                            }
                        }
                    } catch (InterruptedException ex) {
                        logger.error(ex);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        logger.error(ex);
                    }
                }
            } else {// si el sistema de alarmas esta apagado
                logger.info("ESPERANDO CLIC MOVE SYSTEM OFF");
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ex) {
                    logger.error(ex);
                }
            }
        }
    }

    /**
     * creacion de la instacia
     */
    private static void createInstance() {
        synchronized (AlarmFireSensor.class) {
            if (INSTANCE == null) {
                INSTANCE = new AlarmFireSensor();
            }
        }
    }

    /**
     * This method calls createInstance method to creates and ensure that only
     * one instance of this class is created. Singleton design pattern.
     *
     * @return The instance of this class.
     */
    public static AlarmFireSensor getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    public static void main(String args[]) {
        AlarmFireSensor alarmFireSensor = AlarmFireSensor.getInstance();
        logger.info("Class AlarmWindowSensor --- Start ALARM WINDOW ...");
        alarmFireSensor.start();
    }

}
