FROM openjdk:17
COPY target/MyHouse24-Rest.jar myhouse24-rest.jar
ENTRYPOINT ["java","-jar", "myhouse24-rest.jar"]