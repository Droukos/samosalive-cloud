FROM adoptopenjdk/openjdk15-openj9:alpine-slim
COPY ./build/libs/aed-service-0.0.1-SNAPSHOT.jar /aed-service.jar
ENTRYPOINT java $JAVA_OPTS -Xshareclasses -Xquickstart -jar /aed-service.jar