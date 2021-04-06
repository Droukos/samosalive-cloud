FROM adoptopenjdk/openjdk15-openj9:alpine-slim
COPY ./build/libs/auth-service-0.0.1-SNAPSHOT.jar /auth-service.jar
#COPY apprun.sh /apprun.sh
#RUN chmod 770 /apprun.sh
#ENTRYPOINT ["/bin/bash", "/apprun.sh"]
ENTRYPOINT java $JAVA_OPTS -Xshareclasses -Xquickstart -jar /auth-service.jar
