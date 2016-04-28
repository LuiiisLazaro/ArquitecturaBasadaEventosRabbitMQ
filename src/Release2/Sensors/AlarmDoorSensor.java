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
public class AlarmDoorSensor extends Sensor {

    private boolean doorState;
    private int currentDoorState;

    private static AlarmDoorSensor INSTANCE = new AlarmDoorSensor();

    private static final String ID_CHANNEL_ADOOR_SENSOR = "-7"; // id para el canal de comunicacion del sensor de alarmas de puerta
    private static final String ID_CHANNEL_ADOOR_CONTROLLER = "7"; //id para el canal de comunicacion del controlador de alarmas de puerta

    /**
     *constructor
     * inicializacion de valores
     */
    private AlarmDoorSensor() {
        super();
        this.doorState = true;
    }

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

    /**
     * método para tomar acciones dependiendo del mensajes recibido desde el controlador
     * si todo esta normla se represetna por AD0
     * si la alarma se activa se represeta por AD1
     */
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
     * método para correr el hilo de la alarma de puerta 
     * recibe mensjaes del controlador de alarmas
     * envia estado de la alarma de puertas al controlador
     * para la simulacion se crean numeros aleatorios entre 0 y 99 si el numero es mayor a 85 se activa la alarma
     */
    @Override
    public void run() {
        try {
            receiveMessage(ID_CHANNEL_ADOOR_CONTROLLER);//recepcion de mensajes
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        while (!isDone) {
            logger.info(isActive());
            if (isActive()) {//si el sistema de alarmas esta activado 
                if (doorState) {//si la alarma esta trabajando normal
                    currentDoorState = getRandomNumberInt();
                    try {
                        logger.info("Send current DOOR state:" + currentDoorState);
                        sendMessage(ID_CHANNEL_ADOOR_SENSOR, String.valueOf(currentDoorState));//envio de mensajes
                    } catch (IOException | TimeoutException e1) {
                        logger.error(e1);
                    }
                    try {
                        Thread.sleep(delay);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                } else {//si la alarma se activa por un evento de instruso 
                    logger.info("ESPERANDO CLIC DESDE VENTANA DOOR");
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
     * creacion de la instacia
     */
    private static void createInstance() {
        synchronized (AlarmDoorSensor.class) {
            if (INSTANCE == null) {
                INSTANCE = new AlarmDoorSensor();
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

    public static void main(String args[]) {
        AlarmDoorSensor alarmDoorSensor = AlarmDoorSensor.getInstance();
        logger.info("Class AlarmDoorSensor --- Start ALARM DOOR ...");
        alarmDoorSensor.start();
    }
}
