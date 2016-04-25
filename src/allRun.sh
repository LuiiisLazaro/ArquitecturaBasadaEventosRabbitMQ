sh runControllers.sh
sh runSensors.sh
xterm -T "MainMenu" -e "java -jar ../dist/ArquitecturaBasadaEventosRabbitMQ.jar" &
