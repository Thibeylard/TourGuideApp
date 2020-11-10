FROM openjdk:13-oracle

ARG APP_WAR="./build/libs/tourGuide-main-1.0.0.war"

WORKDIR "/tour-guide/app/"

COPY ${APP_WAR} "app.war"

ENTRYPOINT ["java","-jar","app.war"]

