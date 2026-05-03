# שלב 1: בניית הפרויקט עם Maven ו-Java 17
FROM maven:3.9.6-eclipse-temurin-17-jammy AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# שלב 2: הרצת הפרויקט עם סביבת ריצה קלה של Java 17
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]