sh compileAllFiles2.sh
#sh runControllers2.sh
#sh runSensors2.sh

xterm -T "CONSOLE" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar:AbsoluteLayout.jar Release2.Views.MainMenuConsoleView" &
