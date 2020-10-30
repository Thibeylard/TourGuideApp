FROM openjdk:8

ARG APP_WAR="build/libs/tourGuide-gpsUtil-1.0.0.war"

WORKDIR "/opt/app"

COPY ${APP_WAR} app.war

ENTRYPOINT ["java","jar","app.war"]

