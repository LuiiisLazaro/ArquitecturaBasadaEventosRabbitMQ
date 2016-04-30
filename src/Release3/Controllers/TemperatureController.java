/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Release3.Controllers;

import Release1.Sensors.TemperatureSensor;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Faleg, Daniel, Luis
 */
public class TemperatureController extends Controller {

    private int maxTemperature;
    private int minTemperature;

    private static TemperatureController INSTANCE = new TemperatureController();

    private static final String ID_CHANNEL_TEMPERATURE_CONTROLLER = "5"; //id del canal de comunicacion para el controlador de temperatura 
    private static final String ID_CHANNEL_TEMPERATURE_SENSOR = "-5"; //id del canal de comunicacion para el sensor de temperatura
    private static final String ID_CHANNEL_CHANGE_TEMPERATURE = "CT"; //id del canal de comunicacion para el cambio de valores maximo y minimo de la temperatura

    private static final String ID_HEATER_ON = "H1"; //clave para encender el calentador
    private static final String ID_HEATER_OFF = "H0";//clave para apagar el calentador

    private static final String ID_CHILLER_ON = "C1";//clave para encender el enfriador
    private static final String ID_CHILLER_OFF = "C0";//clave para apagar el enfriador

    /**
     *constructor
     * inicializacion de valores
     */
    private TemperatureController() {
        super();
        this.maxTemperature = 75;
        this.minTemperature = 70;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }

    /**
     *método para tomar acciones dependiendo del mensaje envia por el controlador 
     * dependiendo de la temperatura se envia la orden de encender o apagar algun dispositivo
     */
    @Override
    public void checkValues() {
        logger.info("Class TemperatureController --- SEND to Controller ...");
        if (Math.round(Float.parseFloat(getMessage())) > maxTemperature) {//si la temperatura actual es mayor
            try {
                sendMessage(ID_CHANNEL_TEMPERATURE_CONTROLLER, ID_CHILLER_ON);
                sendMessage(ID_CHANNEL_TEMPERATURE_CONTROLLER, ID_HEATER_OFF);
            } catch (IOException | TimeoutException ex) {
                logger.error(ex);
            }
        } else if (Math.round(Float.parseFloat(getMessage())) < minTemperature) {//si la temperatura actual es menor
            try {
                sendMessage(ID_CHANNEL_TEMPERATURE_CONTROLLER, ID_CHILLER_OFF);
                sendMessage(ID_CHANNEL_TEMPERATURE_CONTROLLER, ID_HEATER_ON);
            } catch (IOException | TimeoutException ex) {
                logger.error(ex);
            }
        }
    }

    /**
     * método para actualizar los valores de temperatura minima y maxima
     * estos mensjaes se reciben desde GUI
     */
    @Override
    public void checkValuesMaxMin() {
        String[] values = getMessage().split(":");
        setMaxTemperature(Integer.parseInt(values[0]));
        setMinTemperature(Integer.parseInt(values[1]));
        logger.info("Class TemperatureController --- UPDATE VALUES --- MAX=" + getMaxTemperature() + " Min=" + getMinTemperature());
    }

    /**
     *creacion de la instancia
     */
    private static void createInstance() {
        if (INSTANCE == null) {
            synchronized (TemperatureController.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TemperatureController();
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
    public static TemperatureController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    /**
     *método para correr el hilo, 
     * se inicia la recepcion de mensajes desde el cambio de valores GUI
     * se inicia la recepcion de mensajes desde el sensor de temperatura
     */
    @Override
    public void run() {
        try {
            receiveChangeMaxMinMessage(ID_CHANNEL_CHANGE_TEMPERATURE);//listener para los cambios de valores de temperaturamaxima o minima
            receiveMessage(ID_CHANNEL_TEMPERATURE_SENSOR);//listener para recibir la temperatura actual desde el sensor
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            logger.error(ex);
        }
        //inicio del hilo se sensor de temperatura 
        
        TemperatureSensor temperatureSensor = TemperatureSensor.getInstance();
        logger.info("Class TemperatureSensot Start Sensor Temperature...");
        temperatureSensor.start();

    }

    public static void main(String args[]) {
        TemperatureController temperatureController = TemperatureController.getInstance();
        logger.info("Class TemperatureController --- Start Controller Temperature...");
        temperatureController.start();
    }
}