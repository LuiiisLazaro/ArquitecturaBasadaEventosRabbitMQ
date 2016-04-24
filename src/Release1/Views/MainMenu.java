package Release1.Views;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

import com.rabbitmq.client.*;

import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.SwingConstants;
import javax.swing.JPanel;

public class MainMenu {

    private JFrame frmConsoleMainMenu;

    private JLabel lblTemperature = new JLabel("TEMPERATURE");
    private JLabel lblTemperaturevalue = new JLabel("Now");
    private JLabel lblTemperaturevaluemin = new JLabel("Minimum");
    private JLabel lblTemperaturevaluemax = new JLabel("Maximum\n");

    private JButton btnUpdatevalues = new JButton("UpdateValues");

    private JTextField txtTemperaturevaluemin;
    private JTextField txtTemperaturevaluemax;
    private final JProgressBar progressBarTemperature = new JProgressBar();
    private final static String ID_CHANNEL_TEMPERATURE = "-5";
    private JTextField txtTemperaturenow;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainMenu window = new MainMenu();
                    window.frmConsoleMainMenu.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     *
     * @throws TimeoutException
     * @throws IOException
     */
    public MainMenu() throws IOException, TimeoutException {
        initialize();
        receiveTemperatureMessage();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmConsoleMainMenu = new JFrame();
        frmConsoleMainMenu.setTitle("Console Main Menu V0.1");
        frmConsoleMainMenu.setBounds(100, 100, 800, 600);
        frmConsoleMainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmConsoleMainMenu.getContentPane().setLayout(null);
        progressBarTemperature.setOrientation(SwingConstants.VERTICAL);

        progressBarTemperature.setForeground(Color.BLACK);
        progressBarTemperature.setValue(70);
        progressBarTemperature.setBounds(442, 167, 51, 215);
        progressBarTemperature.setMinimum(65);
        progressBarTemperature.setMaximum(80);
        frmConsoleMainMenu.getContentPane().add(progressBarTemperature);

        JPanel panelTemperature = new JPanel();
        panelTemperature.setBounds(12, 12, 283, 323);
        frmConsoleMainMenu.getContentPane().add(panelTemperature);
        panelTemperature.setLayout(null);
        lblTemperature.setBounds(80, 12, 100, 30);
        panelTemperature.add(lblTemperature);
        lblTemperaturevalue.setBounds(182, 129, 36, 30);
        panelTemperature.add(lblTemperaturevalue);

        txtTemperaturenow = new JTextField();
        txtTemperaturenow.setBounds(123, 163, 75, 30);
        panelTemperature.add(txtTemperaturenow);
        txtTemperaturenow.setColumns(10);
        lblTemperaturevaluemin.setBounds(12, 53, 75, 15);
        panelTemperature.add(lblTemperaturevaluemin);

        txtTemperaturevaluemin = new JTextField();
        txtTemperaturevaluemin.setBounds(94, 263, 75, 30);
        panelTemperature.add(txtTemperaturevaluemin);
        txtTemperaturevaluemin.setText("70");
        txtTemperaturevaluemin.setColumns(10);

        txtTemperaturevaluemax = new JTextField();
        txtTemperaturevaluemax.setBounds(196, 293, 75, 30);
        panelTemperature.add(txtTemperaturevaluemax);
        txtTemperaturevaluemax.setText("85");
        txtTemperaturevaluemax.setColumns(10);
        lblTemperaturevaluemax.setBounds(12, 141, 75, 30);
        panelTemperature.add(lblTemperaturevaluemax);
        btnUpdatevalues.setBounds(12, 240, 148, 27);
        panelTemperature.add(btnUpdatevalues);

        btnUpdatevalues.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ConnectionFactory factory = new ConnectionFactory();
                factory.setHost("localhost");

                Connection connection = null;
                try {
                    connection = factory.newConnection();
                } catch (IOException | TimeoutException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                Channel channel = null;
                try {
                    channel = connection.createChannel();
                } catch (IOException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }

                String message = "H1";

                try {
                    channel.exchangeDeclare("5", "fanout");
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                try {
                    channel.basicPublish("5", "", null, message.getBytes("UTF-8"));
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                try {
                    channel.close();
                } catch (IOException | TimeoutException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
                try {
                    connection.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
    }

    private void receiveTemperatureMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ID_CHANNEL_TEMPERATURE, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, ID_CHANNEL_TEMPERATURE, "");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("Recibido");
                String message = new String(body, "UTF-8");
                txtTemperaturenow.setText(message);
                progressBarTemperature.setValue(Math.round(Float.parseFloat(message)));
                System.out.println("ValueNOw:" + message);
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}
