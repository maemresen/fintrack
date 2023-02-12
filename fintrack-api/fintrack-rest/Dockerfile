FROM openjdk:17-oracle

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} fintrack-rest.jar

EXPOSE 8080

CMD java -jar fintrack-rest.jar
