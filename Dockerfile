FROM tomcat:10.1-jdk21-temurin AS builder

WORKDIR /app
COPY . .

# Chuẩn bị cấu trúc WEB-INF và copy toàn bộ thư viện JAR vào webapp
RUN mkdir -p web/WEB-INF/classes web/WEB-INF/lib && \
    cp lib/*.jar web/WEB-INF/lib/

# Biên dịch mã nguồn Java thuần bằng JDK 21 và thư viện Tomcat 10
RUN javac -encoding UTF-8 -cp "lib/*:/usr/local/tomcat/lib/*" -d web/WEB-INF/classes $(find src/java -name "*.java")

# Đóng gói thành file ROOT.war
RUN jar -cvf ROOT.war -C web .

# Runtime Tomcat 10 Container
FROM tomcat:10.1-jdk21-temurin

WORKDIR /usr/local/tomcat

# Xóa các ứng dụng mặc định của Tomcat
RUN rm -rf webapps/*

# Triển khai file ROOT.war lên gốc trang web (/)
COPY --from=builder /app/ROOT.war webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
