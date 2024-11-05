FROM eclipse-temurin:21
RUN addgroup -system spring && adduser -system spring -ingroup spring
USER spring:spring
ARG JAR_FILE=build/libs/*.jar
ARG FIREBASE_KEY=firebase-key.json
COPY ${JAR_FILE} app.jar
COPY ${FIREBASE_KEY} firebase-key.json
ENTRYPOINT ["java","-jar","/app.jar"]