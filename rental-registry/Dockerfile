FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY target/rental-registry-*.jar rental-registry.jar
EXPOSE 8082
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar rental-registry.jar