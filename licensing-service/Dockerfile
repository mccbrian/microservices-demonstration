#stage 1
#Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim AS build

# Add Maintainer Info
LABEL maintainer=""

# The application's jar file - defines the JAR_FILE variable set by dockerfile-maven-plugin
ARG JAR_FILE

# Add the application's jar to the container - copies the JAR file to the filesystem of the image name app.jar
COPY ${JAR_FILE} app.jar

#unpackage jar file - unpacks the app.jar copied previously into the filesystem of the build image
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)

#stage 2
#Same Java runtime - but the new image contains the different layers of a Spring Boot app instead of the complete JAR file
FROM openjdk:17-jdk-slim

#Add volume pointing to /tmp
VOLUME /tmp

#Copy unpackaged application to new container - copies the different layers from the first image named build
ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

#execute the application - targets the licensing service in the image when the container is created
ENTRYPOINT ["java","-cp","app:app/lib/*","com.optimagrowth.license.LicenseServiceApplication"]