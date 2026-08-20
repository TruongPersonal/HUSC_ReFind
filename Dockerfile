# Stage 1: Build the WAR file using Ant & Tomcat 10 Libraries
FROM tomcat:10.1-jdk21-temurin AS builder

RUN apt-get update && apt-get install -y ant && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . .

RUN ant -Dj2ee.server.home=/usr/local/tomcat -Djavac.source=21 -Djavac.target=21 dist

FROM tomcat:10.1-jdk21-temurin

WORKDIR /usr/local/tomcat

RUN rm -rf webapps/ROOT webapps/ROOT.war webapps/docs webapps/examples

COPY --from=builder /app/dist/*.war webapps/ROOT.war

RUN mkdir -p webapps/ROOT/assets/uploads/items

EXPOSE 8080

CMD ["catalina.sh", "run"]
