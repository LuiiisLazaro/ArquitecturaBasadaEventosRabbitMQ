/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Release1.Sensors;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Faleg, Daniel, Luis
 */
public class AlarmWindowSensor extends Sensor {

    private boolean windowState;
    private int currentWindowState;

    private static AlarmWindowSensor INSTANCE = new AlarmWindowSensor();

    private static final String ID_CHANNEL_AWINDOW_SENSOR = "-6"; //id para el canal de comunicacion del sensor de alarmas de ventana
    private static final String ID_CHANNEL_AWINDOW_CONTROLLER = "6"; //id para el canal de comunicacion del controlador de alarmas de ventana

    /**
     *constructor
     * inicializacion de valores
     */
    private AlarmWindowSensor() {
        super();
        this.windowState = true;
    }

    public boolean isWindowState() {
        return windowState;
    }

    public void setWindowState(boolean windowState) {
        this.windowState = windowState;
    }

    public int getCurrentWindowState() {
        return currentWindowState;
    }

    public void setCurrentWindowState(int currentWindowState) {
        this.currentWindowState = currentWindowState;
    }

    /**
     * método para tomar acciones dependiendo del mensaje recibido desde el controlador
     * si todo esta normal se representa por AW0
     * si la alarma se activa se represeta por AW1
     */
    @Override
    public synchronized void checkValues() {
        switch (getMessage()) {
            case "AW1":
                setWindowState(false);
                break;
            case "AW0":
                setWindowState(true);
                setCurrentWindowState(3);
                break;
            default:
        }
        logger.info("Class: ALARM WINDOWS --- SET VALUES --- windowstate: " + windowState);

    }

    /**
     * método para correr el hilo de alarma de ventanad
     * receibe mensajes del controlador de alarmas
     * envia estado de la alarma de ventanas al controlador
     * para la simulacion se crean numeros aleatorios entre 0 y 99 si el numero es mayor a 85 se activa la alarma
     */
    public void run() {
        try {
            receiveMessage(ID_CHANNEL_AWINDOW_CONTROLLER);//recepcion de mensajes
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        while (!isDone) {
            logger.info(isActive());
            if (isActive()) {//si el sistema de alarmas esta activado
                if (windowState) {//si la alarma esta trabajando normal 
                    currentWindowState = getRandomNumberInt();
                    try {
                        logger.info("Send current WINDOW state:" + currentWindowState);
                        sendMessage(ID_CHANNEL_AWINDOW_SENSOR, String.valueOf(currentWindowState));//envio de mensajes
                    } catch (IOException | TimeoutException e1) {
                        logger.error(e1);
                    }
                    try {
                        Thread.sleep(delay);
                    } catch (Exception e) {
                        logger.error(e);
                    }
                } else {//si la alarma esta activada de un evento de instruso
                    logger.info("ESPERANDO CLIC WINDOWS WINDOW");
                    try {
                        Thread.sleep(delay);
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
        synchronized (AlarmWindowSensor.class) {
            if (INSTANCE == null) {
                INSTANCE = new AlarmWindowSensor();
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

    public static void main(String args[]) {
        AlarmWindowSensor alarmWindowSensor = AlarmWindowSensor.getInstance();
        logger.info("Class AlarmWindowSensor --- Start ALARM WINDOW ...");
        alarmWindowSensor.start();
    }
}
