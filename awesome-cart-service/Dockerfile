FROM openjdk:8-jdk-alpine
ADD target/awesome-cart-service-0.0.1-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["java","-jar","/app.jar"]