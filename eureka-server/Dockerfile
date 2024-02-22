FROM eclipse-temurin:17-jre as builder
WORKDIR workspace
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} ./build/libs/eureka-server-0.0.1-SNAPSHOT.jar
RUN java -Djarmode=layertools -jar ./build/libs/eureka-server-0.0.1-SNAPSHOT.jar extract

FROM eclipse-temurin:21-jre
WORKDIR workspace
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]