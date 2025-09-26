# ---- Build stage ----
FROM maven:3.9.6-eclipse-temurin-8 AS build
WORKDIR /app
COPY . .
# build the jar inside the container
RUN mvn -q -DskipTests package

# ---- Runtime stage ----
FROM eclipse-temurin:8-jre-jammy
WORKDIR /
# copy the built jar (whatever the snapshot name is)
COPY --from=build /app/target/*SNAPSHOT.jar /app.jar

# this env will be overridden in K8s
ENV SA_LOGIC_API_URL=http://localhost:5000
EXPOSE 8080
CMD ["sh","-c","java -jar /app.jar --sa.logic.api.url=${SA_LOGIC_API_URL}"]
