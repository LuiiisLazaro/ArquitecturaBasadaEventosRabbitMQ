/**
 * MAIN MENU GUI para verificar los eventos generadoss por los sensores
 */
package Release3.Views;

import Release3.Controllers.AlarmController;
import Release3.Controllers.HumidityController;
import Release3.Controllers.TemperatureController;
import Release3.Controllers.AlarmFireController;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author Faleg, Daniel, Luis
 */
public class MainMenuConsoleView extends javax.swing.JFrame {

    private int MAX_TEMPERATURE_VIEW;
    private int MIN_TEMPERATURE_VIEW;

    private int MAX_HUMIDITY_VIEW;
    private int MIN_HUMIDITY_VIEW;

    private AlarmController alarmWindowController;
    private AlarmFireController alarmFireController;

    private static final String HOST = "localhost";

    private static final String ID_CHANNEL_TEMPERATURE_SENSOR = "-5";       //canal de comunicacion para mandar y recibir mensajes al sensor de temperatura
    private static final String ID_CHANNEL_TEMPERATURE_CONTROLLER = "5";    //canal de comunicacion para mandar y recibir mensajes al controlador de temperatura

    private static final String ID_CHANNEL_CHANGE_TEMPERATURE = "CT";       //canal de comunicacion para mandar y recibir mensajes al controlador de temperatura para cambios de valor
    private static final String ID_CHANNEL_CHANGE_HUMIDITY = "CH";          //canal de comunicacion para mandar y recibir mensajes al controlador de humedad para cambios de valor

    private static final int MAX_TEMPERATURE = 75;
    private static final int MIN_TEMPERATURE = 70;

    private static final String ID_CHANNEL_HUMIDITY_SENSOR = "-4";          //canal de comunicacion para mandar y recibir mensajes al sensor de humedad
    private static final String ID_CHANNEL_HUMIDITY_CONTROLLER = "4";       //canal de comunicacion para mandar y recibir mensajes al controlador de humedad

    private static final int MAX_HUMIDITY = 55;
    private static final int MIN_HUMIDITY = 45;

    private static final String ID_CHANNEL_AWINDOW_CONTROLLER = "6";        //canal de comunicacion para mandar y recibir mensajes al controlador de alarma de ventana
    private static final String ID_CHANNEL_AWINDOW_SENSOR = "-6";           //canal de comunicacion para mandar y recibir mensajes al sensor de alarma de ventana

    private static final String ID_CHANNEL_ADOOR_CONTROLLER = "7";          //canal de comunicacion para mandar y recibir mensajes al controlador de alarma de puerta
    private static final String ID_CHANNEL_ADOOR_SENSOR = "-7";             //canal de comunicacion para mandar y recibir mensajes al sensor de alarma de puerta

    private static final String ID_CHANNEL_AMOVE_CONTROLLER = "8";          //canal de comunicacion para mandar y recibir mensajes al controlador de alarma de movimiento
    private static final String ID_CHANNEL_AMOVE_SENSOR = "-8";             //canal de comunicacion para mandar y recibir mensajes al sensor de alarma de movimiento

    private static final String ID_CHANNEL_AFIRE_CONTROLLER = "9";
    private static final String ID_CHANNEL_AFIRE_SENSOR = "-9";

    private static final String ID_CHANNEL_AFIRE_SPRINKLERS = "AFS";

    private static final Logger logger = Logger.getLogger(MainMenuConsoleView.class);   //logger para eventos del sistema

    /**
     * constructor que inicializa los componentes GUI y los controladores
     */
    public MainMenuConsoleView() {
        this.alarmWindowController = AlarmController.getInstance();
        this.alarmFireController = AlarmFireController.getInstance();
        this.MIN_HUMIDITY_VIEW = MIN_HUMIDITY - 5;
        this.MAX_HUMIDITY_VIEW = MAX_HUMIDITY + 5;
        this.MIN_TEMPERATURE_VIEW = MIN_TEMPERATURE - 5;
        this.MAX_TEMPERATURE_VIEW = MAX_TEMPERATURE + 5;
        PropertyConfigurator.configure("log4j.properties");
        try {
            initComponents();
            initValuesGeneral();

            receiveTemperatureControllerMessage();
            receiveTemperatureSensorMessage();

            receiveHumidityControllerMessage();
            receiveHumiditySensorMessage();

            receiveAlarmWindowStateMessage();
            receiveAlarmDoorStateMessage();
            receiveAlarmMoveStateMessage();

            receiveAlarmFireStateMessage();
            receiveAlarmSprinklersMessage();

            runThreadsControllers();
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
    }

    /**
     * método para iniciar los hilos de temperatura, humedad y alarmas
     */
    public final void runThreadsControllers() {

        TemperatureController temperatureController = TemperatureController.getInstance();
        logger.info("Class MAINMENU--- Start Controller Temperature...");
        temperatureController.start();

        HumidityController humidityController = HumidityController.getInstance();
        logger.info("Class MAIN MENU--- Start Controller Humidity...");
        humidityController.start();

        logger.info("Class MAINMENU --- Start Controller alarms...");
        alarmWindowController.start();

        logger.info("Class MAINMENU--- Start Controller alarm fire...");
        alarmFireController.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpSensorsControlPanel = new javax.swing.JPanel();
        jPanelTemperature = new javax.swing.JPanel();
        lblTemperatureTitle = new javax.swing.JLabel();
        lblMaxBarTemperature = new javax.swing.JLabel();
        lblMinBarTemperature = new javax.swing.JLabel();
        lblMinimumTemperature = new javax.swing.JLabel();
        lblMaximunTemperature = new javax.swing.JLabel();
        lblTemperatureNow = new javax.swing.JLabel();
        lblSetValuesTemperature = new javax.swing.JLabel();
        txtMinimunTemperature = new javax.swing.JTextField();
        txtMaximumTemperature = new javax.swing.JTextField();
        txtTemperatureNow = new javax.swing.JTextField();
        btnUpdateValuesTemperature = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        lblHeaterState = new javax.swing.JLabel();
        lblChillerState = new javax.swing.JLabel();
        jpChillerValueNow = new javax.swing.JPanel();
        jpHeaterValueNow = new javax.swing.JPanel();
        jProgressBarTemperature = new javax.swing.JProgressBar();
        jPanelHumiduty = new javax.swing.JPanel();
        lblHumidityTitle = new javax.swing.JLabel();
        lblMaxBarHumidity = new javax.swing.JLabel();
        lblMinBarHumidity = new javax.swing.JLabel();
        lblMinimumHumidity = new javax.swing.JLabel();
        lblMaximunHumidity = new javax.swing.JLabel();
        lblHumidityNow = new javax.swing.JLabel();
        lblSetValuesHumidity = new javax.swing.JLabel();
        txtMinimunHumidity = new javax.swing.JTextField();
        txtMaximumHumidity = new javax.swing.JTextField();
        txtHumidityNow = new javax.swing.JTextField();
        btnUpdateValuesHumidity = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        lblHumidityState = new javax.swing.JLabel();
        jpHumidityValueNow = new javax.swing.JPanel();
        jpDeshumidityValueNow = new javax.swing.JPanel();
        lblDeshumidityState = new javax.swing.JLabel();
        jProgressBarHumidity = new javax.swing.JProgressBar();
        lblSensorControlPanelTitle = new javax.swing.JLabel();
        jpSystemAlarm = new javax.swing.JPanel();
        lblTitleAlarms = new javax.swing.JLabel();
        btnStateSystemAlarm = new javax.swing.JToggleButton();
        jpAWindow = new javax.swing.JPanel();
        lblAWindowTitle = new javax.swing.JLabel();
        lblAWindowOff = new javax.swing.JLabel();
        lblAWindowValue = new javax.swing.JLabel();
        jpAWindowNow = new javax.swing.JPanel();
        btnAWindowOff = new javax.swing.JToggleButton();
        jsAWindow = new javax.swing.JSeparator();
        jpADoor = new javax.swing.JPanel();
        lblADoorTitle = new javax.swing.JLabel();
        lblADoorOff = new javax.swing.JLabel();
        lblADoorValue = new javax.swing.JLabel();
        jpADoorNow = new javax.swing.JPanel();
        btnADoorOff = new javax.swing.JToggleButton();
        jsADoor = new javax.swing.JSeparator();
        jpAMove = new javax.swing.JPanel();
        lblAMoveTitle = new javax.swing.JLabel();
        lblAMoveOff = new javax.swing.JLabel();
        lblAMoveValue = new javax.swing.JLabel();
        jpAMoveNow = new javax.swing.JPanel();
        btnAMoveOff = new javax.swing.JToggleButton();
        jsAMove = new javax.swing.JSeparator();
        jpAFire = new javax.swing.JPanel();
        lblAFireTitle = new javax.swing.JLabel();
        lblAFireOff = new javax.swing.JLabel();
        lblAFireValue = new javax.swing.JLabel();
        jpAFireNow = new javax.swing.JPanel();
        btnAFireOff = new javax.swing.JToggleButton();
        jsAFire = new javax.swing.JSeparator();
        jpAFireSprinklersNow = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jpSensorsControlPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanelTemperature.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelTemperature.setPreferredSize(new java.awt.Dimension(200, 250));
        jPanelTemperature.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTemperatureTitle.setText("TEMPERATURE");
        jPanelTemperature.add(lblTemperatureTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 12, -1, -1));
        jPanelTemperature.add(lblMaxBarTemperature, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 40, -1, -1));
        jPanelTemperature.add(lblMinBarTemperature, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 20, 20));

        lblMinimumTemperature.setText("Minimum:");
        jPanelTemperature.add(lblMinimumTemperature, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, -1, -1));

        lblMaximunTemperature.setText("Maximum:");
        jPanelTemperature.add(lblMaximunTemperature, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, -1, -1));

        lblTemperatureNow.setText("Now:");
        jPanelTemperature.add(lblTemperatureNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, -1, -1));

        lblSetValuesTemperature.setText("Set Values");
        jPanelTemperature.add(lblSetValuesTemperature, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 180, -1, -1));

        txtMinimunTemperature.setText("70");
        jPanelTemperature.add(txtMinimunTemperature, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, 130, -1));

        txtMaximumTemperature.setText("75");
        jPanelTemperature.add(txtMaximumTemperature, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 130, -1));
        jPanelTemperature.add(txtTemperatureNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 130, -1));

        btnUpdateValuesTemperature.setText("Update Values");
        btnUpdateValuesTemperature.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateValuesTemperatureActionPerformed(evt);
            }
        });
        jPanelTemperature.add(btnUpdateValuesTemperature, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 310, 130, -1));
        jPanelTemperature.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 181, 10));

        lblHeaterState.setText("Heater");
        jPanelTemperature.add(lblHeaterState, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, -1, -1));

        lblChillerState.setText("Chiller");
        jPanelTemperature.add(lblChillerState, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, -1, -1));

        jpChillerValueNow.setBackground(new java.awt.Color(255, 0, 0));
        jpChillerValueNow.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpChillerValueNowLayout = new javax.swing.GroupLayout(jpChillerValueNow);
        jpChillerValueNow.setLayout(jpChillerValueNowLayout);
        jpChillerValueNowLayout.setHorizontalGroup(
            jpChillerValueNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
        );
        jpChillerValueNowLayout.setVerticalGroup(
            jpChillerValueNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        jPanelTemperature.add(jpChillerValueNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 40, 30));

        jpHeaterValueNow.setBackground(new java.awt.Color(0, 255, 0));
        jpHeaterValueNow.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpHeaterValueNowLayout = new javax.swing.GroupLayout(jpHeaterValueNow);
        jpHeaterValueNow.setLayout(jpHeaterValueNowLayout);
        jpHeaterValueNowLayout.setHorizontalGroup(
            jpHeaterValueNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
        );
        jpHeaterValueNowLayout.setVerticalGroup(
            jpHeaterValueNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        jPanelTemperature.add(jpHeaterValueNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, 40, 30));

        jProgressBarTemperature.setOrientation(1);
        jPanelTemperature.add(jProgressBarTemperature, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 30, 240));

        jPanelHumiduty.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelHumiduty.setPreferredSize(new java.awt.Dimension(200, 250));
        jPanelHumiduty.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblHumidityTitle.setText("HUMIDITY");
        jPanelHumiduty.add(lblHumidityTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 10, 70, -1));
        jPanelHumiduty.add(lblMaxBarHumidity, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 40, 20, 20));
        jPanelHumiduty.add(lblMinBarHumidity, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 320, 20, 20));

        lblMinimumHumidity.setText("Minimum:");
        jPanelHumiduty.add(lblMinimumHumidity, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, -1, -1));

        lblMaximunHumidity.setText("Maximum:");
        jPanelHumiduty.add(lblMaximunHumidity, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, -1, -1));

        lblHumidityNow.setText("Now:");
        jPanelHumiduty.add(lblHumidityNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, -1, -1));

        lblSetValuesHumidity.setText("Set Values");
        jPanelHumiduty.add(lblSetValuesHumidity, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 180, -1, -1));

        txtMinimunHumidity.setText("45");
        jPanelHumiduty.add(txtMinimunHumidity, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, 130, -1));

        txtMaximumHumidity.setText("55");
        jPanelHumiduty.add(txtMaximumHumidity, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, 130, -1));
        jPanelHumiduty.add(txtHumidityNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 130, -1));

        btnUpdateValuesHumidity.setText("Update Values");
        btnUpdateValuesHumidity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateValuesHumidityActionPerformed(evt);
            }
        });
        jPanelHumiduty.add(btnUpdateValuesHumidity, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 310, 130, -1));
        jPanelHumiduty.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, -1, -1));
        jPanelHumiduty.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 181, 10));

        lblHumidityState.setText("Humidity");
        jPanelHumiduty.add(lblHumidityState, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 110, -1, -1));

        jpHumidityValueNow.setBackground(new java.awt.Color(0, 255, 0));
        jpHumidityValueNow.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpHumidityValueNowLayout = new javax.swing.GroupLayout(jpHumidityValueNow);
        jpHumidityValueNow.setLayout(jpHumidityValueNowLayout);
        jpHumidityValueNowLayout.setHorizontalGroup(
            jpHumidityValueNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
        );
        jpHumidityValueNowLayout.setVerticalGroup(
            jpHumidityValueNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        jPanelHumiduty.add(jpHumidityValueNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, 40, 30));

        jpDeshumidityValueNow.setBackground(new java.awt.Color(255, 0, 0));
        jpDeshumidityValueNow.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpDeshumidityValueNowLayout = new javax.swing.GroupLayout(jpDeshumidityValueNow);
        jpDeshumidityValueNow.setLayout(jpDeshumidityValueNowLayout);
        jpDeshumidityValueNowLayout.setHorizontalGroup(
            jpDeshumidityValueNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
        );
        jpDeshumidityValueNowLayout.setVerticalGroup(
            jpDeshumidityValueNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        jPanelHumiduty.add(jpDeshumidityValueNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, -1, -1));

        lblDeshumidityState.setText("Deshumi");
        jPanelHumiduty.add(lblDeshumidityState, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, -1, -1));

        jProgressBarHumidity.setOrientation(1);
        jPanelHumiduty.add(jProgressBarHumidity, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 61, 30, 240));

        lblSensorControlPanelTitle.setText("SENSORS CONTROL PANEL");

        javax.swing.GroupLayout jpSensorsControlPanelLayout = new javax.swing.GroupLayout(jpSensorsControlPanel);
        jpSensorsControlPanel.setLayout(jpSensorsControlPanelLayout);
        jpSensorsControlPanelLayout.setHorizontalGroup(
            jpSensorsControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSensorsControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpSensorsControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelTemperature, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSensorControlPanelTitle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jPanelHumiduty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jpSensorsControlPanelLayout.setVerticalGroup(
            jpSensorsControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSensorsControlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSensorControlPanelTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpSensorsControlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelHumiduty, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelTemperature, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpSystemAlarm.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTitleAlarms.setText("ALARMS CONTROL PANEL");

        btnStateSystemAlarm.setText("ALARMS SYSTEM OFF");
        btnStateSystemAlarm.setPreferredSize(new java.awt.Dimension(152, 15));
        btnStateSystemAlarm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStateSystemAlarmActionPerformed(evt);
            }
        });

        jpAWindow.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jpAWindow.setPreferredSize(new java.awt.Dimension(200, 250));
        jpAWindow.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblAWindowTitle.setText("ALARM WINDOW");
        jpAWindow.add(lblAWindowTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 120, -1));

        lblAWindowOff.setText("Resume Alarm");
        jpAWindow.add(lblAWindowOff, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        lblAWindowValue.setText("State Alarm Window");
        jpAWindow.add(lblAWindowValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        jpAWindowNow.setBackground(new java.awt.Color(0, 255, 0));
        jpAWindowNow.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpAWindowNowLayout = new javax.swing.GroupLayout(jpAWindowNow);
        jpAWindowNow.setLayout(jpAWindowNowLayout);
        jpAWindowNowLayout.setHorizontalGroup(
            jpAWindowNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );
        jpAWindowNowLayout.setVerticalGroup(
            jpAWindowNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
        );

        jpAWindow.add(jpAWindowNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 140, 40));

        btnAWindowOff.setText("RESUME");
        btnAWindowOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAWindowOffActionPerformed(evt);
            }
        });
        jpAWindow.add(btnAWindowOff, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 140, -1));
        jpAWindow.add(jsAWindow, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 181, 10));

        jpADoor.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jpADoor.setPreferredSize(new java.awt.Dimension(200, 250));
        jpADoor.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblADoorTitle.setText("ALARM DOOR");
        jpADoor.add(lblADoorTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, 120, -1));

        lblADoorOff.setText("Resume Alarm");
        jpADoor.add(lblADoorOff, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        lblADoorValue.setText("State Alarm Door");
        jpADoor.add(lblADoorValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        jpADoorNow.setBackground(new java.awt.Color(0, 255, 0));
        jpADoorNow.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpADoorNowLayout = new javax.swing.GroupLayout(jpADoorNow);
        jpADoorNow.setLayout(jpADoorNowLayout);
        jpADoorNowLayout.setHorizontalGroup(
            jpADoorNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );
        jpADoorNowLayout.setVerticalGroup(
            jpADoorNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
        );

        jpADoor.add(jpADoorNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 140, 40));

        btnADoorOff.setText("RESUME");
        btnADoorOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnADoorOffActionPerformed(evt);
            }
        });
        jpADoor.add(btnADoorOff, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 140, -1));
        jpADoor.add(jsADoor, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 181, 10));

        jpAMove.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jpAMove.setPreferredSize(new java.awt.Dimension(200, 250));
        jpAMove.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblAMoveTitle.setText("ALARM MOVE");
        jpAMove.add(lblAMoveTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 100, -1));

        lblAMoveOff.setText("Resume Alarm");
        jpAMove.add(lblAMoveOff, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, -1, -1));

        lblAMoveValue.setText("State Alarm Move");
        jpAMove.add(lblAMoveValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        jpAMoveNow.setBackground(new java.awt.Color(0, 255, 0));
        jpAMoveNow.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpAMoveNowLayout = new javax.swing.GroupLayout(jpAMoveNow);
        jpAMoveNow.setLayout(jpAMoveNowLayout);
        jpAMoveNowLayout.setHorizontalGroup(
            jpAMoveNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );
        jpAMoveNowLayout.setVerticalGroup(
            jpAMoveNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 38, Short.MAX_VALUE)
        );

        jpAMove.add(jpAMoveNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 140, 40));

        btnAMoveOff.setText("RESUME");
        btnAMoveOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAMoveOffActionPerformed(evt);
            }
        });
        jpAMove.add(btnAMoveOff, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 140, -1));
        jpAMove.add(jsAMove, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 181, 10));

        jpAFire.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jpAFire.setPreferredSize(new java.awt.Dimension(200, 250));
        jpAFire.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblAFireTitle.setText("ALARM FIRE");
        jpAFire.add(lblAFireTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 10, 100, -1));

        lblAFireOff.setText("State Sprinklers Fire");
        jpAFire.add(lblAFireOff, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        lblAFireValue.setText("State Alarm Fire");
        jpAFire.add(lblAFireValue, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        jpAFireNow.setBackground(new java.awt.Color(0, 255, 0));
        jpAFireNow.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpAFireNowLayout = new javax.swing.GroupLayout(jpAFireNow);
        jpAFireNow.setLayout(jpAFireNowLayout);
        jpAFireNowLayout.setHorizontalGroup(
            jpAFireNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );
        jpAFireNowLayout.setVerticalGroup(
            jpAFireNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

        jpAFire.add(jpAFireNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 140, 20));

        btnAFireOff.setText("RESUME");
        btnAFireOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAFireOffActionPerformed(evt);
            }
        });
        jpAFire.add(btnAFireOff, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 140, -1));
        jpAFire.add(jsAFire, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 181, 10));

        jpAFireSprinklersNow.setBackground(new java.awt.Color(255, 0, 0));
        jpAFireSprinklersNow.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jpAFireSprinklersNowLayout = new javax.swing.GroupLayout(jpAFireSprinklersNow);
        jpAFireSprinklersNow.setLayout(jpAFireSprinklersNowLayout);
        jpAFireSprinklersNowLayout.setHorizontalGroup(
            jpAFireSprinklersNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 138, Short.MAX_VALUE)
        );
        jpAFireSprinklersNowLayout.setVerticalGroup(
            jpAFireSprinklersNowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 18, Short.MAX_VALUE)
        );

        jpAFire.add(jpAFireSprinklersNow, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 140, 20));
        jpAFireSprinklersNow.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout jpSystemAlarmLayout = new javax.swing.GroupLayout(jpSystemAlarm);
        jpSystemAlarm.setLayout(jpSystemAlarmLayout);
        jpSystemAlarmLayout.setHorizontalGroup(
            jpSystemAlarmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSystemAlarmLayout.createSequentialGroup()
                .addGroup(jpSystemAlarmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpSystemAlarmLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jpSystemAlarmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jpSystemAlarmLayout.createSequentialGroup()
                                .addComponent(jpAWindow, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jpAMove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jpSystemAlarmLayout.createSequentialGroup()
                                .addComponent(lblTitleAlarms)
                                .addGap(58, 58, 58)
                                .addComponent(btnStateSystemAlarm, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jpSystemAlarmLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jpADoor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jpAFire, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpSystemAlarmLayout.setVerticalGroup(
            jpSystemAlarmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpSystemAlarmLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jpSystemAlarmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitleAlarms)
                    .addComponent(btnStateSystemAlarm, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpSystemAlarmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpAWindow, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                    .addComponent(jpAMove, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpSystemAlarmLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpADoor, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jpAFire, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jpSensorsControlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jpSystemAlarm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpSensorsControlPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpSystemAlarm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * inicalizacion de valores en elementos de GUI
     */
    private void initValuesGeneral() {
        lblMaxBarTemperature.setText(String.valueOf(MAX_TEMPERATURE_VIEW));
        lblMinBarTemperature.setText(String.valueOf(MIN_TEMPERATURE_VIEW));

        jProgressBarTemperature.setMaximum(MAX_TEMPERATURE_VIEW);
        jProgressBarTemperature.setMinimum(MIN_TEMPERATURE_VIEW);

        lblMaxBarHumidity.setText(String.valueOf(MAX_HUMIDITY_VIEW));
        lblMinBarHumidity.setText(String.valueOf(MIN_HUMIDITY_VIEW));

        jProgressBarHumidity.setMaximum(MAX_HUMIDITY);
        jProgressBarHumidity.setMinimum(MIN_HUMIDITY_VIEW);
    }

    /**
     * Método para enviar mensajes a los controladores
     *
     * @param ID_CHANNEL_SEND --- identificador del canal de comunicacion del
     * controlador
     * @param message --- mensaje enviado
     * @throws IOException
     * @throws TimeoutException
     */
    private void sendMessageComponents(String ID_CHANNEL_SEND, String message) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        logger.info("Class MainMenuConsoleView --- SEND to Controller --- Value:" + message);

        channel.exchangeDeclare(ID_CHANNEL_SEND, "fanout");
        channel.basicPublish(ID_CHANNEL_SEND, "", null, message.getBytes("UTF-8"));

        channel.close();
        connection.close();
    }

    /**
     * Método para actualizar los valores de la temperatura en el controlador
     *
     * @param evt
     */
    private void btnUpdateValuesTemperatureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateValuesTemperatureActionPerformed
        if (Integer.parseInt(txtMaximumTemperature.getText()) > Integer.parseInt(txtMinimunTemperature.getText())) {
            try {
                logger.info("SEND NEW TEMPERATURE:" + txtMaximumTemperature.getText() + ":" + txtMinimunTemperature.getText());
                sendMessageComponents(ID_CHANNEL_CHANGE_TEMPERATURE, txtMaximumTemperature.getText() + ":" + txtMinimunTemperature.getText());
                jProgressBarTemperature.setMaximum(Integer.parseInt(txtMaximumTemperature.getText()) + 5);
                jProgressBarTemperature.setMinimum(Integer.parseInt(txtMinimunTemperature.getText()) - 5);
                lblMaxBarTemperature.setText(String.valueOf(Integer.parseInt(txtMaximumTemperature.getText()) + 5));
                lblMinBarTemperature.setText(String.valueOf(Integer.parseInt(txtMinimunTemperature.getText()) - 5));
            } catch (IOException | TimeoutException ex) {
                logger.error(ex);
            }
        } else {
            JOptionPane.showConfirmDialog(this, "Information", "La temperatura Máxima debe ser mayor que la temperatura mínima", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnUpdateValuesTemperatureActionPerformed

    /**
     * Método para actualizar los valores de la humedad en el controlador
     *
     * @param evt
     */
    private void btnUpdateValuesHumidityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateValuesHumidityActionPerformed
        if (Integer.parseInt(txtMaximumHumidity.getText()) > Integer.parseInt(txtMinimunHumidity.getText())) {
            try {
                logger.info("SEND NEW HUMIDITY:" + txtMaximumHumidity.getText() + ":" + txtMinimunHumidity.getText());
                sendMessageComponents(ID_CHANNEL_CHANGE_HUMIDITY, txtMaximumHumidity.getText() + ":" + txtMinimunHumidity.getText());
                jProgressBarHumidity.setMaximum(Integer.parseInt(txtMaximumHumidity.getText()) + 5);
                jProgressBarHumidity.setMinimum(Integer.parseInt(txtMinimunHumidity.getText()) - 5);
                lblMaxBarHumidity.setText(String.valueOf(Integer.parseInt(txtMaximumHumidity.getText()) + 5));
                lblMinBarHumidity.setText(String.valueOf(Integer.parseInt(txtMinimunHumidity.getText()) - 5));
            } catch (IOException | TimeoutException ex) {
                logger.error(ex);
            }
        } else {
            JOptionPane.showConfirmDialog(this, "Information", "La Humedad Máxima debe ser mayor que la humedad mínima", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnUpdateValuesHumidityActionPerformed

    /**
     * método para reanudar la actividad de la alarma de puerta, es decir que
     * vuelva a generar mensajes el sensor
     *
     * @param evt
     */
    private void btnADoorOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnADoorOffActionPerformed
        try {
            logger.info("Enviado resumen al HILO AL CONTROLLER DOOR");
            sendMessageComponents(ID_CHANNEL_ADOOR_SENSOR, "1");
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
    }//GEN-LAST:event_btnADoorOffActionPerformed

    /**
     * método para reanudar la actividad de la alarma de ventana, es decir que
     * vuelva a generar mensajes el sensor
     *
     * @param evt
     */
    private void btnAWindowOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAWindowOffActionPerformed
        try {
            logger.info("Enviado resumen al HILO CONTROLLER WINDOW");
            sendMessageComponents(ID_CHANNEL_AWINDOW_SENSOR, "1");
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
    }//GEN-LAST:event_btnAWindowOffActionPerformed

    /**
     * método para reanudar la actividad de la alarma de movimiento, es decir
     * que vuelva a generar mensajes el sensor
     *
     * @param evt
     */
    private void btnAMoveOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAMoveOffActionPerformed
        try {
            logger.info("Enviado resumen al HILO CONTROLLER WINDOW");
            sendMessageComponents(ID_CHANNEL_AMOVE_SENSOR, "1");
        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
    }//GEN-LAST:event_btnAMoveOffActionPerformed

    /**
     * método para activar/desactivar de eventos de los controladores y sensores
     *
     * @param evt
     */
    private void btnStateSystemAlarmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStateSystemAlarmActionPerformed
        alarmWindowController.resumeThread();
        if (alarmWindowController.isActive()) {
            btnStateSystemAlarm.setText("ALARMS SYSTEM ON");
        } else {
            btnStateSystemAlarm.setText("ALARMS SYSTEM OFF");
        }
    }//GEN-LAST:event_btnStateSystemAlarmActionPerformed

    private void btnAFireOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAFireOffActionPerformed
        try {
            logger.info("Enviado resumen al HILO CONTROLLER FIRE");
            sendMessageComponents(ID_CHANNEL_AFIRE_SENSOR, "1");

        } catch (IOException | TimeoutException ex) {
            logger.error(ex);
        }
    }//GEN-LAST:event_btnAFireOffActionPerformed

    /**
     * método para recibir mensajes del controlador de temperatura para mostrar
     * el estado de los dispositivos de heater y chiller
     *
     * @throws IOException
     * @throws TimeoutException
     */
    private void receiveTemperatureControllerMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setRequestedHeartbeat(5);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_TEMPERATURE_CONTROLLER, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_TEMPERATURE_CONTROLLER, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                logger.info("Class MainMenuConsoleView --- RECEIVED from Temperature Controller --- Value: " + message);
                switch (message) {
                    case "H1":
                        jpHeaterValueNow.setBackground(new java.awt.Color(0, 255, 0));
                        break;
                    case "H0":
                        jpHeaterValueNow.setBackground(new java.awt.Color(255, 0, 0));
                        break;
                    case "C1":
                        jpChillerValueNow.setBackground(new java.awt.Color(0, 255, 0));
                        break;
                    case "C0":
                        jpChillerValueNow.setBackground(new java.awt.Color(255, 0, 0));
                        break;
                    default:
                }

            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * método para recibir mensajes del controlador de Humedad para mostrar el
     * estado de los dispositivos humidity y deshumidity
     *
     * @throws IOException
     * @throws TimeoutException
     */
    private void receiveHumidityControllerMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_HUMIDITY_CONTROLLER, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_HUMIDITY_CONTROLLER, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                logger.info("Class MainMenuConsoleView --- RECEIVED from Humidity Controller --- Value: " + message);
                switch (message) {
                    case "H1":
                        jpHumidityValueNow.setBackground(new java.awt.Color(0, 255, 0));
                        break;
                    case "H0":
                        jpHumidityValueNow.setBackground(new java.awt.Color(255, 0, 0));
                        break;
                    case "D1":
                        jpDeshumidityValueNow.setBackground(new java.awt.Color(0, 255, 0));
                        break;
                    case "D0":
                        jpDeshumidityValueNow.setBackground(new java.awt.Color(255, 0, 0));
                        break;
                    default:
                }

            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * método para recibir mensajes del controlador de alarma de ventana, para
     * mostrar el estado de la alarma
     *
     * @throws IOException
     * @throws TimeoutException
     */
    private synchronized void receiveAlarmWindowStateMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_AWINDOW_CONTROLLER, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_AWINDOW_CONTROLLER, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public synchronized void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                logger.info("Class MAIN MENUCONSOLE VIEW  --- RECEIVED from ALARM WINDOW --- Value: " + new String(body, "UTF-8"));
                if (message.equals("AW1")) {
                    jpAWindowNow.setBackground(new java.awt.Color(255, 0, 0));
                    logger.info("CAMBIO DE COLOR");
                } else {
                    jpAWindowNow.setBackground(new java.awt.Color(0, 255, 0));
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * método para recibir mensajes del controlador de alarma de puerta, para
     * mostrar el estado de la alarma
     *
     * @throws IOException
     * @throws TimeoutException
     */
    private synchronized void receiveAlarmDoorStateMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_ADOOR_CONTROLLER, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_ADOOR_CONTROLLER, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public synchronized void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                logger.info("Class MAIN MENUCONSOLE VIEW  --- RECEIVED from ALARM DOOR --- Value: " + new String(body, "UTF-8"));
                if (message.equals("AD1")) {
                    jpADoorNow.setBackground(new java.awt.Color(255, 0, 0));
                    logger.info("CAMBIO DE COLOR");
                } else {
                    jpADoorNow.setBackground(new java.awt.Color(0, 255, 0));
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * método para recibir mensajes del controlador de alarma de movimiento,
     * para mostrar el estado de la alarma
     *
     * @throws IOException
     * @throws TimeoutException
     */
    private synchronized void receiveAlarmMoveStateMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_AMOVE_CONTROLLER, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_AMOVE_CONTROLLER, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public synchronized void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                logger.info("Class MAIN MENUCONSOLE VIEW  --- RECEIVED from ALARM MOVE --- Value: " + new String(body, "UTF-8"));
                if (message.equals("AM1")) {
                    jpAMoveNow.setBackground(new java.awt.Color(255, 0, 0));
                    logger.info("CAMBIO DE COLOR");
                } else {
                    jpAMoveNow.setBackground(new java.awt.Color(0, 255, 0));
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    private synchronized void receiveAlarmFireStateMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setRequestedHeartbeat(10);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_AFIRE_CONTROLLER, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_AFIRE_CONTROLLER, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public synchronized void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                logger.info("Class MAIN MENUCONSOLE VIEW  --- RECEIVED from ALARM FIRE --- Value: " + new String(body, "UTF-8"));
                if (message.equals("AF1")) {
                    jpAFireNow.setBackground(new java.awt.Color(255, 0, 0));
                    logger.info("CAMBIO DE COLOR");
                } else {
                    jpAFireNow.setBackground(new java.awt.Color(0, 255, 0));
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    private synchronized void receiveAlarmSprinklersMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_AFIRE_SPRINKLERS, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_AFIRE_SPRINKLERS, "");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public synchronized void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                logger.info("Class MAIN MENUCONSOLE VIEW  --- RECEIVED from ALARM SPRINKELERS --- Value: " + new String(body, "UTF-8"));
                if (message.equals("true")) {
                    jpAFireSprinklersNow.setBackground(new java.awt.Color(0, 255, 0));
                    logger.info("CAMBIO DE COLOR");
                } else {
                    jpAFireSprinklersNow.setBackground(new java.awt.Color(255, 0, 0));
                }
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("GTK+".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.error(ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainMenuConsoleView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnADoorOff;
    private javax.swing.JToggleButton btnAFireOff;
    private javax.swing.JToggleButton btnAMoveOff;
    private javax.swing.JToggleButton btnAWindowOff;
    private javax.swing.JToggleButton btnStateSystemAlarm;
    private javax.swing.JButton btnUpdateValuesHumidity;
    private javax.swing.JButton btnUpdateValuesTemperature;
    private javax.swing.JPanel jPanelHumiduty;
    private javax.swing.JPanel jPanelTemperature;
    private javax.swing.JProgressBar jProgressBarHumidity;
    private javax.swing.JProgressBar jProgressBarTemperature;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JPanel jpADoor;
    private javax.swing.JPanel jpADoorNow;
    private javax.swing.JPanel jpAFire;
    private javax.swing.JPanel jpAFireNow;
    private javax.swing.JPanel jpAFireSprinklersNow;
    private javax.swing.JPanel jpAMove;
    private javax.swing.JPanel jpAMoveNow;
    private javax.swing.JPanel jpAWindow;
    private javax.swing.JPanel jpAWindowNow;
    private javax.swing.JPanel jpChillerValueNow;
    private javax.swing.JPanel jpDeshumidityValueNow;
    private javax.swing.JPanel jpHeaterValueNow;
    private javax.swing.JPanel jpHumidityValueNow;
    private javax.swing.JPanel jpSensorsControlPanel;
    private javax.swing.JPanel jpSystemAlarm;
    private javax.swing.JSeparator jsADoor;
    private javax.swing.JSeparator jsAFire;
    private javax.swing.JSeparator jsAMove;
    private javax.swing.JSeparator jsAWindow;
    private javax.swing.JLabel lblADoorOff;
    private javax.swing.JLabel lblADoorTitle;
    private javax.swing.JLabel lblADoorValue;
    private javax.swing.JLabel lblAFireOff;
    private javax.swing.JLabel lblAFireTitle;
    private javax.swing.JLabel lblAFireValue;
    private javax.swing.JLabel lblAMoveOff;
    private javax.swing.JLabel lblAMoveTitle;
    private javax.swing.JLabel lblAMoveValue;
    private javax.swing.JLabel lblAWindowOff;
    private javax.swing.JLabel lblAWindowTitle;
    private javax.swing.JLabel lblAWindowValue;
    private javax.swing.JLabel lblChillerState;
    private javax.swing.JLabel lblDeshumidityState;
    private javax.swing.JLabel lblHeaterState;
    private javax.swing.JLabel lblHumidityNow;
    private javax.swing.JLabel lblHumidityState;
    private javax.swing.JLabel lblHumidityTitle;
    private javax.swing.JLabel lblMaxBarHumidity;
    private javax.swing.JLabel lblMaxBarTemperature;
    private javax.swing.JLabel lblMaximunHumidity;
    private javax.swing.JLabel lblMaximunTemperature;
    private javax.swing.JLabel lblMinBarHumidity;
    private javax.swing.JLabel lblMinBarTemperature;
    private javax.swing.JLabel lblMinimumHumidity;
    private javax.swing.JLabel lblMinimumTemperature;
    private javax.swing.JLabel lblSensorControlPanelTitle;
    private javax.swing.JLabel lblSetValuesHumidity;
    private javax.swing.JLabel lblSetValuesTemperature;
    private javax.swing.JLabel lblTemperatureNow;
    private javax.swing.JLabel lblTemperatureTitle;
    private javax.swing.JLabel lblTitleAlarms;
    private javax.swing.JTextField txtHumidityNow;
    private javax.swing.JTextField txtMaximumHumidity;
    private javax.swing.JTextField txtMaximumTemperature;
    private javax.swing.JTextField txtMinimunHumidity;
    private javax.swing.JTextField txtMinimunTemperature;
    private javax.swing.JTextField txtTemperatureNow;
    // End of variables declaration//GEN-END:variables

    /**
     * método para recibir mensajes del sensor de temperatura, para mostrar en
     * la barra de progreso
     *
     * @throws IOException
     * @throws TimeoutException
     */
    private void receiveTemperatureSensorMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_TEMPERATURE_SENSOR, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_TEMPERATURE_SENSOR, "");

        Consumer consumer;
        consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                logger.info("Class MainMenuConsoleView --- RECEIVED from Temperature Sensor --- Value: " + new String(body, "UTF-8"));
                txtTemperatureNow.setText(new String(body, "UTF-8"));
                jProgressBarTemperature.setValue(Math.round(Float.valueOf(new String(body, "UTF-8"))));

            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * método para recibir mensajes del sensor de humedad, para mostrar en la
     * barra de progreso
     *
     * @throws IOException
     * @throws TimeoutException
     */
    private void receiveHumiditySensorMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_HUMIDITY_SENSOR, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_HUMIDITY_SENSOR, "");

        Consumer consumer;
        consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                logger.info("Class MainMenuConsoleView --- RECEIVED from Humidity Sensor --- Value: " + new String(body, "UTF-8"));
                txtHumidityNow.setText(new String(body, "UTF-8"));
                jProgressBarHumidity.setValue(Math.round(Float.valueOf(new String(body, "UTF-8"))));
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}
