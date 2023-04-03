FROM amazoncorretto:11-alpine-jdk
#RUN apk add --no-cache tzdata
#ENV TZ=Europe/Moscow
VOLUME /tmp
COPY /target/*.jar /shareit/gateway.jar
WORKDIR /shareit
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /shareit/gateway.jar"]
