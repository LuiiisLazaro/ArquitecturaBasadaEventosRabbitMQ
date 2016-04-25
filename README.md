# ArquitecturaBasadaEventosRabbitMQ
# MUSEUM ENVIRONMENTAL CONTROL SYSTEM CONSOLE

##Presentation
This is the repository for the evaluation of the "event-driven architecture" in the software architecture course.
Team members are: Daniel Mendez Cruz <daniel.mendez@cimat.mx>, Faleg Alejandro Peralta Martinez <faleg.peralta@cimat.mx> and Luis Angel Lazaro Hernandez <luis.hernandez@cimat.mx>.  
The repository is organized in three releases in the "src" folder.

## Before to continue
There are some requirements to start the install...  
First make sure that your computer has install Java (JRE to be exactly)  [see](http://openjdk.java.net/install/)  
Second make sure that your computer has install RabbitMQ and erlang [see](https://www.rabbitmq.com/)

## Guide to run Release 1

### First Make Sure ...
Make sure that you have already all packages and files to compile the project. Check the packages:  
1: Controllers  
2: Sensors  
3: View  

### Second Compile ...
Now we are going to compile all files:  
To compile all files you can use the file `compileAllFiles.sh`, execute the file and if all it is OK then continue to Thrid step, however if you have a problem with the file follow the next instructions.  
1: Check if you have all libraries to compile the files.  
2: Open a terminal  
3: Type the command  `javac -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release1/Controllers/*.java` to compile the Controllers.  
4: Type the command `javac -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release1/Sensors/*.java` to compile the Sensors.  

### Thrid Run Controllers ...
To run all controllers you can use the file `runControllers.sh`, if you have a problem with the file try type the commands:  
1: `xterm -T "TemperatureController" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release1.Controllers.TemperatureController" &`  
2: `xterm -T "HumidityController" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release1.Controllers.HumidityController" &`  
3: ... to all controllers coming soon ...  
### Forth Run Views ...
To run the GUI Console execute the file ArquitecturaBasadaEventosRabbitMQ.jar

### Fifth Run Sensors ...
To run all sensors you can use the file `runSensors.sh`, if you have a problem with the file try type the commands:  
1: `xterm -T "TemperatureSensor" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release1.Sensors.TemperatureSensor" &`  
2: `xterm -T "HumiditySensor" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release1.Sensors.TemperatureSensor" &`  
3: ... to all controllers coming soon ...
