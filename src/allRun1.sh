sh compileAllFiles1.sh
#sh runControllers1.sh
#sh runSensors1.sh

xterm -T "CONSOLE" -e "java -cp .:rabbitmq-client.jar:log4j-1.2.17.jar:AbsoluteLayout.jar Release1.Views.MainMenuConsoleView" &
