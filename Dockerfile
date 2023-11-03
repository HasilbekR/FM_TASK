FROM tomcat:latest
EXPOSE 8081
ARG WAR_FILE=target/FM_TASK-1.0-SNAPSHOT.war
COPY ${WAR_FILE} /usr/local/tomcat/webapps/ROOT.war
