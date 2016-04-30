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

### First Make Sure
Make sure that you have already all packages and files to compile the project. Check the packages:  
1: Controllers  
2: Sensors  
3: View  
Clone the repository with the command: `git clone https://github.com/LuiiisLazaro/ArquitecturaBasadaEventosRabbitMQ.git`

WARNING ... this project is only compile from linux system because we are not use windows, sorry.

### Second Compile and Run Releases
Now we are going to compile all files:  
To compile all files you can use the file `allRunX.sh`, X is the release. Execute the file and if all it is OK then continue to Thrid step, however if you have a problem with the file follow the next instructions.  
1: Check if you have all libraries to compile the files.  
2: Open a terminal  
3: Type the command  `sh allRunX.sh`


### RUN FIRST RELEASE 
To run first release type the command:
`sh allRun1.sh`  
and wait a few seconds to see the compoenents.

### RUN SECOND RELEASE 
To run second release type the command:
`sh allRun2.sh`  
and wait a few seconds to see the compoenents.

### RUN THIRD RELEASE 
To run third release type the command:
`sh allRun3.sh`  
and wait a few seconds to see the compoenents.