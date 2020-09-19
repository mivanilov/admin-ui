FROM openjdk:11-jre-slim

RUN apt-get -q update && apt-get -qy install netcat

COPY ./target/admin-ui-1.0.0-SNAPSHOT.jar ./docker/wait-for.sh /
RUN chmod +x /wait-for.sh

CMD java ${JAVA_OPTS} -jar /admin-ui-1.0.0-SNAPSHOT.jar