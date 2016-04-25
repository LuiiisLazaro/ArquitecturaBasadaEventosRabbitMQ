xterm -T "TemperatureSensor" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release1.Sensors.TemperatureSensor" &
xterm -T "HumiditySensor" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release1.Sensors.HumiditySensor" &
