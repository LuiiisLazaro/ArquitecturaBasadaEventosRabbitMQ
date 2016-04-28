echo "Compile Controllers"
javac -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release1/Controllers/*.java

echo "Compile Sensors"
javac -cp .:rabbitmq-client.jar:log4j-1.2.17.jar Release1/Sensors/*.java

echo "Compile View"
javac -cp .:rabbitmq-client.jar:log4j-1.2.17.jar:AbsoluteLayout.jar Release1/Views/*.java
