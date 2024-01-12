# Game of Three

### Description
- Simple game of three which communicates via the HTTP REST Endpoint.
### Programming language, technologies and libraries

- Java 17
- Gradle
- Spring Boot
- lombok

### Prerequisites

Java 17 and gradle should be installed

### How to run the project

Go to the project directory and run the following commands:
- build the project

```shellscript
gradle clean build   or ./gradlew clean build
```
- to run the first player application
```shellscript
java -jar build/libs/game-of-three-1.0.0-SNAPSHOT.jar
```
- to run the second player application
```shellscript
java -jar build/libs/game-of-three-1.0.0-SNAPSHOT.jar  --spring.config.location=src/main/resources/player2_application.yml
```
- start the game from the postman or commandlibe by typing one of the following URLs depends on the following options:
1. first player start the game with automatic selection of the initial number
```
http://localhost:8080/game/start-game/automatic/false 
```
2. first player start the game with manual selection
```
curl --location 'localhost:8080/game/start-game/manual' \
--data '{
    "startNumber" : 34
}'
```
3. second player start the game with automatic selection of the initial number
```
http://localhost:8081/threegame/start-game/automatic/false 
```
4. second player start the game with manual selection of the initial number (100 for example)
```
curl --location 'localhost:8080/game/start-game/manual' \
--data '{
    "startNumber" : 340
}'
```
