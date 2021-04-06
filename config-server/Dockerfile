FROM adoptopenjdk/openjdk15-openj9:alpine-slim
COPY ./build/libs/config-server-0.0.1-SNAPSHOT.jar /config-server.jar
ENTRYPOINT java $JAVA_OPTS -Xshareclasses -Xquickstart -jar /config-server.jar