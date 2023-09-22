FROM openjdk:17-alpine

COPY ./build/libs/spofo-stock-0.0.1-SNAPSHOT.jar /usr/src/myapp/
CMD java -jar /usr/src/myapp/spofo-stock-0.0.1-SNAPSHOT.jar