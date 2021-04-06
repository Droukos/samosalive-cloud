FROM adoptopenjdk/openjdk15-openj9:alpine-slim
COPY ./build/libs/news-service-0.0.1-SNAPSHOT.jar /news-service.jar
ENTRYPOINT java $JAVA_OPTS -Xshareclasses -Xquickstart -jar /news-service.jar