xterm -T "TemperatureSensor" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release2.Sensors.TemperatureSensor" &
xterm -T "HumiditySensor" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release2.Sensors.HumiditySensor" &
xterm -T "AlarmWindowSensor" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release2.Sensors.AlarmWindowSensor" &
xterm -T "AlarmDoorSensor" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release2.Sensors.AlarmDoorSensor" &
xterm -T "AlarmMoveSensor" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release2.Sensors.AlarmMoveSensor" &
