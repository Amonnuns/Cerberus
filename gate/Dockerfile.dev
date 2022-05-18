FROM maven:3.8.5-eclipse-temurin-17-alpine

COPY . /usr/src/gate

WORKDIR /usr/src/gate

#Add docker-compose-wait tool
RUN wget https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh
RUN chmod +x wait-for-it.sh

RUN mvn package spring-boot:repackage -pl . -Dmaven.test.skip

EXPOSE 8081

CMD [ "java", "-jar", "target/gate-1.0-SNAPSHOT.jar" ]