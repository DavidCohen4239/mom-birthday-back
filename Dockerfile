# שלב 1: בניית האפליקציה (Build Stage)
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# העתקת קבצי ההגדרות והקוד
COPY pom.xml .
COPY src ./src

# בניית ה-JAR תוך דילוג על טסטים כדי לחסוך זמן ומשאבים בענן
RUN mvn clean package -DskipTests

# שלב 2: הרצת האפליקציה (Runtime Stage)
FROM eclipse-temurin:17-jre
WORKDIR /app

# העתקת קובץ ה-JAR שנבנה בשלב הקודם
# השם נגזר בדיוק מה-ArtifactID וה-Version ב-pom.xml שלך
COPY --from=build /app/target/mom-birthday-site-0.0.1-SNAPSHOT.jar app.jar

# חשיפת הפורט הסטנדרטי
EXPOSE 8080

# הרצת האפליקציה
ENTRYPOINT ["java", "-jar", "app.jar"]