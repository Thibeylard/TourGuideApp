FROM openjdk:13-oracle

ARG APP_WAR="./build/libs/tourGuide-tripPricer-1.0.0.war"

WORKDIR "/tripPricer/app/"

COPY ${APP_WAR} "app.war"

ENTRYPOINT ["java","-jar","app.war"]

