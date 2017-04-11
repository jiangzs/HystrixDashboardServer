FROM java:8-jre
VOLUME /tmp
ADD hystrix_server.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 9898
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]