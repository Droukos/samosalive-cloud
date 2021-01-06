FROM openjdk:15
COPY ./build/libs/auth-service-0.0.1-SNAPSHOT.jar /auth-service.jar
COPY apprun.sh /apprun.sh
RUN chmod 770 /apprun.sh
ENTRYPOINT ["/bin/bash", "/apprun.sh"]