FROM openjdk:10-jre
VOLUME /tmp
ADD distillehr-document-viewer-service-0.0.1-SNAPSHOT.jar distillehr-document-viewer-service-0.0.1-SNAPSHOT.jar
RUN sh -c 'touch /distillehr-document-viewer-service-0.0.1-SNAPSHOT.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS --add-modules java.se.ee -jar /distillehr-document-viewer-service-0.0.1-SNAPSHOT.jar" ]