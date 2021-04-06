FROM adoptopenjdk/openjdk15-openj9:alpine-slim
COPY ./build/libs/user-service-0.0.1-SNAPSHOT.jar /user-service.jar
ENTRYPOINT java $JAVA_OPTS -Xshareclasses -Xquickstart -jar /user-service.jar