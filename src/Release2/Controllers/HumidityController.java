/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Release2.Controllers;

import static Release2.Controllers.Controller.logger;
import Release1.Sensors.HumiditySensor;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Faleg, Daniel, Luis
 */
public class HumidityController extends Controller implements Runnable {

    private int maxHumidity;
    private int minHumidity;

    private static HumidityController INSTANCE = new HumidityController();

    private static final String ID_CHANNEL_HUMIDITY_CONTROLLER = "4";//id del canal de comunicacion para el controlador de humedad
    private static final String ID_CHANNEL_HUMIDITY_SENSOR = "-4";//id del canal de comunicacion para el sensor de humedad
    private static final String ID_CHANNEL_CHANGE_HUMIDITY = "CH"; //id del canal de comunicacion para el cambio de valores maximo y minimo de la humedad
    
    private static final String ID_HUMIDITY_ON = "H1";//clave para encender el humidificador
    private static final String ID_HUMIDITY_OFF = "H0";//clave para apagar el humidificador

    private static final String ID_DESHUMIDITY_ON = "D1";//clave para encender el deshumidificador
    private static final String ID_DESHUMIDITY_OFF = "D0";//clave para apagar el humidificador


    /**
     *constructor
     * inicializacion de valores
     */
    private HumidityController() {
        super();
        this.minHumidity = 45;
        this.maxHumidity = 55;
    }

    public int getMaxHumidity() {
        return maxHumidity;
    }

    public void setMaxHumidity(int maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public int getMinHumidity() {
        return minHumidity;
    }

    public void setMinHumidity(int minHumidity) {
        this.minHumidity = minHumidity;
    }

    /**
     *método para toamr acciones dependiendo el mensaje enviado por el controlador
     * dependiendo de la humedad se envia la orden de encender o aapagar algun dispostivo
     */
    @Override
    public void checkValues() {
        logger.info("Class HumidityController --- SEND to Controller ...");
        if (Math.round(Float.parseFloat(getMessage())) > maxHumidity) {//si la humedad es actual es mayor a la humedad permitida
            try {
                sendMessage(ID_CHANNEL_HUMIDITY_CONTROLLER, ID_HUMIDITY_OFF);
                sendMessage(ID_CHANNEL_HUMIDITY_CONTROLLER, ID_DESHUMIDITY_ON);
            } catch (IOException | TimeoutException ex) {
                logger.error(ex);
            }
        } else if (Math.round(Float.parseFloat(getMessage())) < minHumidity) {//si la humedad actual es menor a la humedad permitida
            try {
                sendMessage(ID_CHANNEL_HUMIDITY_CONTROLLER, ID_HUMIDITY_ON);
                sendMessage(ID_CHANNEL_HUMIDITY_CONTROLLER, ID_DESHUMIDITY_OFF);
            } catch (IOException | TimeoutException ex) {
                logger.error(ex);
            }
        }
    }

    /**
     * método para actualizar los valores  de temperatura minima y maxima 
     * estos mensajes se reciben desde GUI
     */
    @Override
    public void checkValuesMaxMin() {
        String[] values = getMessage().split(":");
        setMaxHumidity(Integer.parseInt(values[0]));
        setMinHumidity(Integer.parseInt(values[1]));
        logger.info("Class HumidityController  UPDATE VALUES  MAX=" + getMaxHumidity() + " Min=" + getMinHumidity());
    }

    /**
     * creacion de la instancia
     */
    private static void createInstance() {
        synchronized (HumidityController.class) {
            if (INSTANCE == null) {
                INSTANCE = new HumidityController();
            }
        }
    }

    /**
     * This method calls createInstance method to creates and ensure that only
     * one instance of this class is created. Singleton design pattern.
     *
     * @return The instance of this class.
     */
    public static HumidityController getInstance() {
        if (INSTANCE == null) {
            createInstance();
        }
        return INSTANCE;
    }

    /**
     * método para correr el hilo
     * se inicia la recepcion de mensajes de cambio de valor de humedad desde GUI
     * se inicia la recepcion  de mensajes de humedad desde el sensor de humedad
     */
    @Override
    public void run() {
        try {
            receiveMessage(ID_CHANNEL_HUMIDITY_SENSOR);//recepcion de menajes de valores de humedad
            receiveChangeMaxMinMessage(ID_CHANNEL_CHANGE_HUMIDITY);//recepcion de mensajes de cambio de maximo y minimo de humedad
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
        
        //inicio del hilo del sensor de humedad
        HumiditySensor humiditySensor = HumiditySensor.getInstance();
        logger.info("Class HumiditysSensor --- Start Sensor Temperature...");
        humiditySensor.start();
    }

    /**
     *
     * @param args
     */
    public static void main(String args[]) {
        HumidityController humidityController = HumidityController.getInstance();
        logger.info("Class HumidityController --- Start Controller Humidity...");
        humidityController.start();
    }
}