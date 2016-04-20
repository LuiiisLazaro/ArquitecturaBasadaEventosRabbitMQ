package Release1.Controllers;

public class Controller {
    protected int delay = 2000;				// The loop delay (2 seconds)
    protected boolean isDone = false;			// Loop termination flag
    
    protected Controller() {
        super();
    }

    protected void sendMessage(String message) {
        //enviar mensaje por medio de rabbitmq envio de orden 
    }
    
    protected void receiveMessage(){
    	//recibir mensaje por medio de rabbitmq recibir valores
    }
}