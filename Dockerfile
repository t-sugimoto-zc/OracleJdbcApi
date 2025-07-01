# Build stage
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/oracle-jdbc-api-0.0.1-SNAPSHOT.jar app.jar
COPY lib/ojdbc11.jar lib/ojdbc11.jar

# JDBC接続情報の環境変数設定
ENV ORACLE_JDBC_URL=jdbc:oracle:thin:@//coe-temp-db.chwflugwvllv.ap-northeast-1.rds.amazonaws.com:1521/coetest
ENV ORACLE_JDBC_USER=coe_admin
ENV ORACLE_JDBC_PASSWORD=Outsystemsadmin1

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
