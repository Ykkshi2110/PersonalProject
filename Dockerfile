#stage build
FROM eclipse-temurin:22-jdk AS builder
WORKDIR /peter/jobhunter
COPY . /peter/jobhunter
RUN ./mvnw -Dmaven.test.skip=true clean package

#stage runtime
FROM eclipse-temurin:22-jre
WORKDIR /peter/jobhunter
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/peter/jobhunter/myproject.jar"]
COPY --from=builder /peter/jobhunter/target/*.jar myproject.jar