# ArquitecturaBasadaEventosRabbitMQ
# MUSEUM ENVIRONMENTAL CONTROL SYSTEM CONSOLE

##Presentation


##Guide to run Release 1

### First Make Sure ...w
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
