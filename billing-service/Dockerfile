# getting maven env from maven image which uses java 21
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# working directory
WORKDIR /app

# copies our pom.xml to folder i.e /app
COPY pom.xml .

# dependency:go-offline -B means cache the dependencies
# unless a change has been detected
RUN mvn dependency:go-offline -B

# copying our src to /app/src in the container
COPY src ./src

# we tell maven to create a new package from out springboot app
# i.e a jar file
RUN mvn clean package

# to run our build code we use openjdk
FROM openjdk:21-jdk AS runner

WORKDIR /app

# copies the patient-service snapshot jar created at the builder step
# and paste it to the runner step and paste it at ./app.jar
COPY --from=builder ./app/target/billing-service-0.0.1-SNAPSHOT.jar ./app.jar

# specify the port on which our app runs on
EXPOSE 4001
# GRPC port
EXPOSE 9001

# command for our run step to run our code
ENTRYPOINT ["java","-jar","app.jar"]