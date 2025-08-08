FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
COPY . .
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
ENV PAYMENT_PROCESSOR_URL_DEFAULT=http://payment-processor-default:8080
ENV PAYMENT_PROCESSOR_URL_FALLBACK=http://payment-processor-fallback:8080
ENV JAVA_OPTS="-server -XX:+UseG1GC -Xlog:disable -XX:-PrintGC -XX:-PrintGCDetails -XX:-FlightRecorder -XX:+DisableExplicitGC"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]