echo "Run Temperature controller"
xterm -T "TemperatureController" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release2.Controllers.TemperatureController" &

echo "Run humidity controller"
xterm -T "HumidityController" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release2.Controllers.HumidityController" &

echo "Run Alarm Controller"
xterm -T "AlarmController" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release2.Controllers.AlarmController" &
