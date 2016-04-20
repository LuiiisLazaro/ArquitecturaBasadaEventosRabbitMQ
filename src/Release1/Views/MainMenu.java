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

public class MainMenu {

	private JFrame frmConsoleMainMenu;
	private JTextField txtTemperaturevaluemin;
	private JTextField txtTemperaturevaluemax;
	private final JProgressBar progressBarTemperature = new JProgressBar();
	private final static String QUEUE_NAME = "hello";
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
		
		JLabel lblTemperature = new JLabel("TEMPERATURE");
		lblTemperature.setBounds(60, 30, 100, 30);
		frmConsoleMainMenu.getContentPane().add(lblTemperature);
		
		JLabel lblTemperaturevalue = new JLabel("TemperatureValue");
		lblTemperaturevalue.setBounds(47, 49, 135, 30);
		frmConsoleMainMenu.getContentPane().add(lblTemperaturevalue);
		
		JLabel lblTemperaturevaluemin = new JLabel("TemperatureValueMin");
		lblTemperaturevaluemin.setBounds(41, 161, 163, 15);
		frmConsoleMainMenu.getContentPane().add(lblTemperaturevaluemin);
		
		txtTemperaturevaluemin = new JTextField();
		txtTemperaturevaluemin.setText("70");
		txtTemperaturevaluemin.setBounds(23, 179, 200, 30);
		frmConsoleMainMenu.getContentPane().add(txtTemperaturevaluemin);
		txtTemperaturevaluemin.setColumns(10);
		
		JLabel lblTemperaturevaluemax = new JLabel("TemperatureValueMax");
		lblTemperaturevaluemax.setBounds(41, 206, 163, 30);
		frmConsoleMainMenu.getContentPane().add(lblTemperaturevaluemax);
		
		txtTemperaturevaluemax = new JTextField();
		txtTemperaturevaluemax.setText("85");
		txtTemperaturevaluemax.setBounds(23, 236, 200, 30);
		frmConsoleMainMenu.getContentPane().add(txtTemperaturevaluemax);
		txtTemperaturevaluemax.setColumns(10);
		
		progressBarTemperature.setForeground(Color.BLACK);
		progressBarTemperature.setValue(70);
		progressBarTemperature.setBounds(23, 72, 200, 30);
		progressBarTemperature.setMinimum(65);
		progressBarTemperature.setMaximum(80);
		frmConsoleMainMenu.getContentPane().add(progressBarTemperature);
		
		JButton btnUpdatevalues = new JButton("UpdateValues");
		btnUpdatevalues.setBounds(23, 266, 200, 27);
		frmConsoleMainMenu.getContentPane().add(btnUpdatevalues);
		
		txtTemperaturenow = new JTextField();
		txtTemperaturenow.setBounds(23, 102, 200, 30);
		frmConsoleMainMenu.getContentPane().add(txtTemperaturenow);
		txtTemperaturenow.setColumns(10);
	}
	
	private void receiveTemperatureMessage() throws IOException, TimeoutException{
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	    Consumer consumer = new DefaultConsumer(channel) {
	      @Override
	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
	          throws IOException {
	        String message = new String(body, "UTF-8");
	        txtTemperaturenow.setText(message);
	        progressBarTemperature.setValue(Math.round(Float.parseFloat(message)));
	      }
	    };
	    channel.basicConsume(QUEUE_NAME, true, consumer);
	}
}
