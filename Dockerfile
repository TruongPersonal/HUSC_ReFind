FROM tomcat:10.1-jdk21-temurin AS builder

WORKDIR /app
COPY . .

RUN mkdir -p web/WEB-INF/classes web/WEB-INF/lib && \
    cp lib/*.jar web/WEB-INF/lib/

RUN javac -encoding UTF-8 -cp "lib/*:/usr/local/tomcat/lib/*" -d web/WEB-INF/classes $(find src/java -name "*.java")

RUN jar -cvf ROOT.war -C web .

FROM tomcat:10.1-jdk21-temurin

WORKDIR /usr/local/tomcat

RUN rm -rf webapps/*

COPY --from=builder /app/ROOT.war webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
