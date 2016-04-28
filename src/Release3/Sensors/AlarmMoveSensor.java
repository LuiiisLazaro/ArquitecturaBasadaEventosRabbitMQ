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
public class AlarmMoveSensor extends Sensor {

    private boolean moveState;
    private int currentMoveState;

    private static AlarmMoveSensor INSTANCE = new AlarmMoveSensor();

    private static final String ID_CHANNEL_AMOVE_SENSOR = "-8"; //id para el canal de cuminicacion de sensor de alarmas de movimiento
    private static final String ID_CHANNEL_AMOVE_CONTROLLER = "8"; //id para el canal de comunicacion del controlador de alamas de movimiento

    

    /**
     *constructor
     * inicializacion de valores para la clase 
     */
    private AlarmMoveSensor() {
        super();
        this.moveState = true;
    }

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

    /**
     *método para tomar acciones dependiendo el menssaje recibido desde el controlador
     * si todo esta normal se representa ppor AM0
     * si la alarma se activa se representa por AM1
     */
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
     *método para correr el hilo de alarma de movimiento
     * recibe mensajes del controlar de alarmas
     * envia estado de la alarma de movimiento al controlador
     */
    @Override
    public void run() {
        try {
            receiveMessage(ID_CHANNEL_AMOVE_CONTROLLER);//recepcion de menssjes
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        while (!isDone) {
            logger.info(isActive());
            if (isActive()) {//si el sistema de alarmas esta activado
                if (moveState) {//si la alarma estra trabajando normal 
                    currentMoveState = getRandomNumberInt();
                    try {
                        logger.info("Send current MOVE state:" + currentMoveState);
                        sendMessage(ID_CHANNEL_AMOVE_SENSOR, String.valueOf(currentMoveState));//envio de mensajes
                    } catch (IOException | TimeoutException ex) {
                        logger.error(ex);
                    }
                    try {
                        Thread.sleep(delay);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                } else { //si la alarma esta activada de un evento de instruso
                    logger.info("ESPERANDO CLIC MOVE ALERTA");
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ex) {
                        logger.error(ex);
                    }
                }
            } else {//si el sistema de alarmas esta apagado
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
     *creacion de la instacia
     */
    private static void createInstance() {
        synchronized (AlarmMoveSensor.class) {
            if (INSTANCE == null) {
                INSTANCE = new AlarmMoveSensor();
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

    public static void main(String args[]) {
        AlarmMoveSensor alarmMoveSensor = AlarmMoveSensor.getInstance();
        logger.info("Class AlarmMoveSensor --- Start ALARM MOVE ...");
        alarmMoveSensor.start();
    }
}