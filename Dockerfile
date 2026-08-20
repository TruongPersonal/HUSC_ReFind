# Stage 1: Build the WAR file using Ant & Tomcat 10 Libraries
FROM tomcat:10.1-jdk21-temurin AS builder

# Install Ant
RUN apt-get update && apt-get install -y ant && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . .

# Compile and package WAR
RUN ant -Dj2ee.server.home=/usr/local/tomcat -Djavac.source=21 -Djavac.target=21 dist

# Stage 2: Production Runtime Tomcat 10 Container
FROM tomcat:10.1-jdk21-temurin

WORKDIR /usr/local/tomcat

# Clean default Tomcat web applications
RUN rm -rf webapps/ROOT webapps/ROOT.war webapps/docs webapps/examples

# Deploy application at root context path (/)
COPY --from=builder /app/dist/*.war webapps/ROOT.war

# Create local fallback upload folder
RUN mkdir -p webapps/ROOT/assets/uploads/items

EXPOSE 8080

CMD ["catalina.sh", "run"]
