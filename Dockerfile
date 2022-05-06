FROM openjdk:12-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY /build/libs/file-storage-s3-*.jar /file-storage-s3.jar
ENTRYPOINT ["java","-jar","/file-storage-s3.jar"]