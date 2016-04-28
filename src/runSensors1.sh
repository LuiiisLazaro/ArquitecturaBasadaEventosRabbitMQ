xterm -T "TemperatureSensor" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release1.Sensors.TemperatureSensor" &
xterm -T "HumiditySensor" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release1.Sensors.HumiditySensor" &
xterm -T "AlarmWindowSensor" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release1.Sensors.AlarmWindowSensor" &
xterm -T "AlarmDoorSensor" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release1.Sensors.AlarmDoorSensor" &
xterm -T "AlarmMoveSensor" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release1.Sensors.AlarmMoveSensor" &
