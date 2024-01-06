# Console Chat

## Overview
This application is built on top of Java Sockets and acts as both server and client.</br>
When first instance of the program is launched it serves as a server. When the second one is it serves as a client.
Both client and server support input command prompt. </br>
The chatUsers are stored on the server side in Java Collections. </br>
Each client has a sync copy of the chatUser object from the server. </br>
All messages are stored in /chat-history folder. </br>

### Pre requirements
- Installed JDK 11 version or above
- Installed Gradle version 8.4 or above


## Client Commands
```
- NAME <nickname>                           Changes a nickname of the current user. Example: NAME roman
- ECHO [ON | OFF]                           Enables server echo messages. Is set to true by default. Example: ECHO OFF
- STATS                                     Prints statistic about online users. Example: STATS
- MESG <nickname>#<id> "<message>"          Sends a message to the given client. Example: MESG client#1 "Hello client"
- BCST "<message>"                          Broadcasts messages to all connected clients. Example: BCST "Broadcast message"
- QUIT                                      Disconnects a user and exits from the console. Example: QUIT
- TIME                                      Prints a time measurements of current session duration. Example: TIME
- HELP                                      Shows information about available commands and its syntax. Example: HELP
```

## Server Commands
```
- SEND_BROADCAST                           Sends broadcast messages to all online users. Example: SEND_BROADCAST
- CLOSE                                    Shutdowns the server, disconects all chatUser clients. Example: CLOSE
```

## Simple Message Exchanging Protocol

Client prompt consists of chatUser nickname and id.</br>
The prompt precedes the user input and is reset when a message was received <br>.
The server responds to the user requests with server command and body.
Each command message be it from server or a client starts from descriptor prompt.
For a chatUser the prompt is: `{nickname}#{id}>` - the id is used further in server request commands to identify the sender user.
For the server it's: `server>`

### Client prompt
```
client#1>echo off
server><SERVER_COMAND>:{<BODY>}
...
client#1>name roman
server>UPDATE_USER:{"id":1,"nickname":"roman","lunaUser":false,"sessionStart":"2024-01-06T21:34:16.801977900Z"}
roman#1>

roman#1>quit
Disconnected
server>DISCONNECT_USER:
```

### Server prompt
```
server>
server>CLOSE
Disconnecting chat users...
Chat users have been disconnected.
Closing server socket...
Server socket has been successfully closed.
Server shutdown.
```

## Technology stack
- Java 11
- Java Sockets (java.net)
- Gradle 8.4
- Spring 5.3.22
- Slf4j 2.0.3
- Log4j12 2.0.3
- Jackson 2.13.4
- Junit 4.13.2
- AssertJ 1.10.19 
- Mockito 1.10.19

## Build
You can build the code with the following gradle command.
> `gradlew clean build`

## Run
You can run the project either with IDE means or by running the following command in a terminal.
> `java -jar app/build/libs/console-chat-1.0.jar`

