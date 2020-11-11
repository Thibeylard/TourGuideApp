FROM openjdk:13-oracle

ARG APP_WAR="./build/libs/tourGuide-rewards-1.0.0.war"

ARG WAIT_FOR_IT="./src/main/resources/libs/wait-for-it.sh"

WORKDIR "/rewards/app/"

COPY ${APP_WAR} "app.war"

COPY ${WAIT_FOR_IT} "wait-for-it.sh"

ENTRYPOINT ["./wait-for-it.sh", "gps:8081", "-t", "0", "--", "java","-jar","app.war"]

