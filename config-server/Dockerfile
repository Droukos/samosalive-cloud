FROM openjdk:15
COPY ./build/libs/config-server-0.0.1-SNAPSHOT.jar /config-server.jar
COPY apprun.sh /apprun.sh
RUN chmod 770 /apprun.sh
ENTRYPOINT ["/bin/bash", "/apprun.sh"]