FROM openjdk:15
COPY ./build/libs/cdn-service-0.0.1-SNAPSHOT.jar /cdn-service.jar
COPY apprun.sh /apprun.sh
RUN chmod 770 /apprun.sh
ENTRYPOINT ["/bin/bash", "/apprun.sh"]